package com.coderman.sync.service.impl;

import com.coderman.api.util.ResultUtil;
import com.coderman.api.vo.PageVO;
import com.coderman.api.vo.ResultVO;
import com.coderman.service.anntation.LogError;
import com.coderman.sync.service.PlanService;
import com.coderman.sync.vo.PlanVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class PlanServiceImpl implements PlanService {

    @Resource
    private JdbcTemplate jdbcTemplate;


    /**
     * @param currentPage 当前页
     * @param pageSize    分页大小
     * @param queryVO     查询参数
     * @return
     */
    @Override
    public List<PlanVO> page(Integer currentPage, Integer pageSize, PlanVO queryVO) {

        StringBuilder countSql = new StringBuilder("select count(1) ");
        StringBuilder realSql = new StringBuilder("select uuid,plan_code,src_db,dest_db,src_project,dest_project,status,create_time,update_time,plan_content");
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


        realSql.append(sql).append(" order by create_time desc limit ?,? ");
        params.add((currentPage - 1) * pageSize);
        params.add(pageSize);

        // 查询
        return this.jdbcTemplate.query(realSql.toString(), new BeanPropertyRowMapper<>(PlanVO.class), params.toArray());
    }

    @Override
    @LogError(value = "查看同步内容")
    public ResultVO<String> selectContent(String uuid) {

        if(StringUtils.isBlank(uuid)){

            return ResultUtil.getWarn("uuid不能为空");
        }

        String content = this.jdbcTemplate.queryForObject("select plan_content from pub_sync_plan where uuid=?", String.class, uuid);
        return ResultUtil.getSuccess(String.class,content);
    }
}
