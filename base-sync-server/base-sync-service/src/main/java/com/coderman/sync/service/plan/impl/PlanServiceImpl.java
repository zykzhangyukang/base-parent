package com.coderman.sync.service.plan.impl;

import com.coderman.api.util.ResultUtil;
import com.coderman.api.vo.PageVO;
import com.coderman.api.vo.ResultVO;
import com.coderman.service.anntation.LogError;
import com.coderman.service.redis.RedisService;
import com.coderman.service.util.UUIDUtils;
import com.coderman.sync.config.PlanRefreshConfig;
import com.coderman.sync.constant.PlanConstant;
import com.coderman.sync.plan.meta.PlanMeta;
import com.coderman.sync.plan.parser.MetaParser;
import com.coderman.sync.service.plan.PlanService;
import com.coderman.sync.vo.PlanVO;
import com.google.common.base.CaseFormat;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PlanServiceImpl implements PlanService {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
    private RedisService redisService;


    @Override
    @LogError(value = "同步计划新增")
    public ResultVO<Void> savePlan(PlanVO planVO) {

        String planContent = planVO.getPlanContent();

        if (StringUtils.isBlank(planContent)) {

            return ResultUtil.getWarn("同步计划内容不能为空.");
        }

        String planCode, srcDb, destDb, srcProject, destProject;

        try {

            // 解析同步计划
            PlanMeta planMeta = MetaParser.parse(planContent);

            planCode = planMeta.getCode();
            destProject = planMeta.getProjectMeta().getDestProject();
            srcProject = planMeta.getProjectMeta().getSrcProject();
            srcDb = planMeta.getDbMeta().getSrcDb();
            destDb = planMeta.getDbMeta().getDestDb();

        } catch (Exception E) {

            return ResultUtil.getWarn("同步计划解析错误");
        }

        Assert.notNull(planCode, "planCode is null");
        Assert.notNull(destDb, "destDb is null");
        Assert.notNull(srcDb, "srcDb is null");
        Assert.notNull(destProject, "destProject is null");
        Assert.notNull(srcProject, "srcProject is null");

        List<PlanVO> planVOByCode = this.getPlanVOByCode(planCode);

        if (CollectionUtils.isNotEmpty(planVOByCode)) {

            return ResultUtil.getWarn("已存在编号为 " + planCode + " 的同步计划");
        }

        int count = jdbcTemplate.update(
                "insert into pub_sync_plan(uuid,plan_code,src_db,dest_db,src_project,dest_project,plan_content,status,create_time,update_time) " +
                        "values(?,?,?,?,?,?,?,?,?,?)",
                preparedStatement -> {
                    preparedStatement.setString(1, UUIDUtils.getPrimaryValue());
                    preparedStatement.setString(2, planCode);
                    preparedStatement.setString(3, srcDb);
                    preparedStatement.setString(4, destDb);
                    preparedStatement.setString(5, srcProject);
                    preparedStatement.setString(6, destProject);
                    preparedStatement.setString(7, planContent);
                    preparedStatement.setString(8, PlanConstant.STATUS_NORMAL);
                    preparedStatement.setObject(9, new Date());
                    preparedStatement.setObject(10, new Date());

                });

        if (count <= 0) {

            throw new RuntimeException("新增同步计划失败");
        }

        this.publishToRedis();

        return ResultUtil.getSuccess();
    }

    @Override
    @LogError(value = "同步计划删除")
    public ResultVO<Void> deletePlan(String uuid) {

        List<PlanVO> planVOByUuid = this.getPlanVOByUuid(uuid);

        if (CollectionUtils.isEmpty(planVOByUuid)) {

            return ResultUtil.getWarn("同步计划不存在,请刷新重试!");
        }

        PlanVO planVO = planVOByUuid.get(0);

        if (!StringUtils.equals(planVO.getStatus(), PlanConstant.STATUS_FORBID)) {

            return ResultUtil.getWarn("启用状态不允许删除!");
        }

        List<Object> params = new ArrayList<>();

        params.add(uuid);

        int rowCount = this.jdbcTemplate.update("delete from pub_sync_plan where uuid=?", params.toArray());

        if (rowCount <= 0) {

            return ResultUtil.getWarn("删除同步计划失败!");
        }

        this.publishToRedis();

        return ResultUtil.getSuccess();
    }

    @Override
    @LogError(value = "同步计划更新")
    public ResultVO<Void> updatePlan(PlanVO planVO) {

        String uuid = planVO.getUuid();

        if (StringUtils.isBlank(uuid)) {

            return ResultUtil.getWarn("uuid不能为空");
        }

        String planContent = planVO.getPlanContent();

        if (StringUtils.isBlank(planContent)) {

            return ResultUtil.getWarn("同步计划内容不能为空");
        }

        String planCode, srcDb, destDb, destProject, srcProject;

        try {

            // 解析同步计划
            PlanMeta planMeta = MetaParser.parse(planContent);

            planCode = planMeta.getCode();
            srcProject = planMeta.getProjectMeta().getSrcProject();
            destProject = planMeta.getProjectMeta().getDestProject();
            srcDb = planMeta.getDbMeta().getSrcDb();
            destDb = planMeta.getDbMeta().getDestDb();

        } catch (Exception E) {

            return ResultUtil.getWarn("同步计划解析错误");
        }

        Assert.notNull(planCode, "planCode is null");
        Assert.notNull(srcDb, "srcDb is null");
        Assert.notNull(destDb, "destDb is null");
        Assert.notNull(destProject, "destProject is null");
        Assert.notNull(srcProject, "srcProject is null");

        List<PlanVO> planVOByCode = this.getPlanVOByCode(planCode);

        if (CollectionUtils.isNotEmpty(planVOByCode)) {

            Optional<PlanVO> optional = planVOByCode.stream().filter(e -> !StringUtils.equals(e.getUuid(), uuid)).findAny();
            if (optional.isPresent()) {

                return ResultUtil.getWarn("已存在编号为 " + planCode + " 的同步计划");
            }
        }


        int count = this.jdbcTemplate.update("update pub_sync_plan set plan_content=? ,plan_code = ?,src_db=?,dest_db=?,src_project=?,dest_project=?,update_time=?" +
                " where uuid=?", planContent, planCode, srcDb, destDb, srcProject, destProject, new Date(), uuid);

        if (count <= 0) {

            throw new RuntimeException("更新同步计划失败");
        }

        this.publishToRedis();

        return ResultUtil.getSuccess();
    }


    @LogError(value = "广播消息到redis")
    public void publishToRedis(){
        this.redisService.getRedisTemplate().convertAndSend(PlanRefreshConfig.PLAN_REFRESH_KEY,String.valueOf(System.currentTimeMillis()));
    }

    @Override
    @LogError(value = "启用/禁用 同步内容")
    public ResultVO<Void> updateStatus(String uuid) {

        if (StringUtils.isBlank(uuid)) {

            return ResultUtil.getWarn("uuid不能为空");
        }

        List<PlanVO> list = getPlanVOByUuid(uuid);

        if (CollectionUtils.isEmpty(list)) {

            return ResultUtil.getWarn("同步计划不存在");
        }

        PlanVO planVO = list.get(0);

        int count = this.jdbcTemplate.update("update pub_sync_plan set status=?,update_time=?" +
                " where uuid=?", StringUtils.equals(planVO.getStatus(), PlanConstant.STATUS_NORMAL) ?
                PlanConstant.STATUS_FORBID : PlanConstant.STATUS_NORMAL, new Date(), uuid);

        if (count <= 0) {

            throw new RuntimeException("更新状态失败");
        }

        this.publishToRedis();

        return ResultUtil.getSuccess();
    }


    private List<PlanVO> getPlanVOByUuid(String uuid) {

        List<Object> params = new ArrayList<>();

        params.add(uuid);

        return this.jdbcTemplate.query("select plan_content,status from pub_sync_plan where uuid=?",
                new BeanPropertyRowMapper<>(PlanVO.class), params.toArray());
    }


    private List<PlanVO> getPlanVOByCode(String code) {

        List<Object> params = new ArrayList<>();

        params.add(code);

        return this.jdbcTemplate.query("select uuid,plan_content,status from pub_sync_plan where plan_code=?",
                new BeanPropertyRowMapper<>(PlanVO.class), params.toArray());
    }


    /**
     * @param currentPage 当前页
     * @param pageSize    分页大小
     * @param queryVO     查询参数
     * @return
     */
    @Override
    public ResultVO<PageVO<List<PlanVO>>> page(Integer currentPage, Integer pageSize, String sort, String order, PlanVO queryVO) {

        StringBuilder countSql = new StringBuilder("select count(1) ");
        StringBuilder realSql = new StringBuilder("select uuid,plan_code,src_db,dest_db,src_project,dest_project,plan_content,status,create_time,update_time,plan_content");
        StringBuilder sql = new StringBuilder(" from pub_sync_plan where 1=1");

        if (currentPage == null) {

            currentPage = 1;
        }

        if (pageSize == null) {

            pageSize = 10;
        }

        // 参数
        List<Object> params = new ArrayList<>();


        if (StringUtils.isNotBlank(queryVO.getPlanCode())) {

            sql.append(" and plan_code like ?");
            params.add("%" + queryVO.getPlanCode() + "%");
        }

        if (StringUtils.isNotBlank(queryVO.getStatus())) {

            sql.append(" and status=?");
            params.add(queryVO.getStatus());
        }

        if (StringUtils.isNotBlank(queryVO.getSrcDb())) {

            sql.append(" and src_db=?");
            params.add(queryVO.getSrcDb());
        }

        if (StringUtils.isNotBlank(queryVO.getDestDb())) {

            sql.append(" and dest_db=?");
            params.add(queryVO.getDestDb());
        }

        if (StringUtils.isNotBlank(queryVO.getStatus())) {

            sql.append(" and status=?");
            params.add(queryVO.getStatus());
        }

        countSql.append(sql);

        // 总条数
        Integer count = jdbcTemplate.queryForObject(countSql.toString(), Integer.class, params.toArray());

        realSql.append(sql);

        // 驼峰转下划线
        String dbField = StringUtils.EMPTY;

        if (StringUtils.isNotBlank(sort)) {

            dbField = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, sort);
        }

        if (StringUtils.equals(dbField, "create_time")) {

            realSql.append(" order by create_time ").append(order);

        } else if (StringUtils.equals(dbField, "update_time")) {

            realSql.append(" order by update_time ").append(order);

        } else {
            realSql.append(" order by create_time ").append("desc");
        }

        realSql.append(" limit ?,? ");

        params.add((currentPage - 1) * pageSize);
        params.add(pageSize);

        List<PlanVO> list = this.jdbcTemplate.query(realSql.toString(), new BeanPropertyRowMapper<>(PlanVO.class), params.toArray());

        try {
            if (CollectionUtils.isNotEmpty(list)) {

                for (PlanVO planVO : list) {

                    if (StringUtils.isNotBlank(planVO.getPlanContent())) {

                        String desc = MetaParser.parse(planVO.getPlanContent()).getName();
                        planVO.setDescription(desc);
                    }
                }
            }
        } catch (Exception ignored) {

        }

        Assert.notNull(count, "count is null");
        return ResultUtil.getSuccessPage(PlanVO.class, new PageVO<>(count, list, currentPage, pageSize));
    }

    @Override
    @LogError(value = "查看同步内容")
    public ResultVO<String> selectContent(String uuid) {

        if (StringUtils.isBlank(uuid)) {

            return ResultUtil.getWarn("uuid不能为空");
        }

        List<PlanVO> list = getPlanVOByUuid(uuid);

        if (CollectionUtils.isEmpty(list)) {

            return ResultUtil.getWarn("同步计划不存在");
        }

        return ResultUtil.getSuccess(String.class, list.get(0).getPlanContent());
    }

}