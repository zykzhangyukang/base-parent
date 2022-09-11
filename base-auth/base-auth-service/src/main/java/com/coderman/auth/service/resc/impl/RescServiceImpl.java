package com.coderman.auth.service.resc.impl;

import com.coderman.api.constant.ResultConstant;
import com.coderman.api.exception.BusinessException;
import com.coderman.api.util.ResultUtil;
import com.coderman.api.vo.PageVO;
import com.coderman.api.vo.ResultVO;
import com.coderman.auth.dao.func.FuncRescDAO;
import com.coderman.auth.dao.resc.RescDAO;
import com.coderman.auth.model.func.FuncRescExample;
import com.coderman.auth.model.resc.RescExample;
import com.coderman.auth.model.resc.RescModel;
import com.coderman.auth.service.resc.RescService;
import com.coderman.auth.vo.resc.RescVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author coderman
 * @date 2022/3/199:13
 */
@Service
public class RescServiceImpl implements RescService {


    @Autowired
    private RescDAO rescDAO;


    @Autowired
    private FuncRescDAO funcRescDAO;

    @Override
    public ResultVO<PageVO<List<RescVO>>> page(Integer currentPage, Integer pageSize, RescVO queryVO) {

        PageHelper.startPage(currentPage, pageSize);
        List<RescVO> rescVOList = this.rescDAO.page(queryVO);

        PageInfo<RescVO> pageInfo = new PageInfo<>(rescVOList);
        PageVO<List<RescVO>> pageVO = new PageVO<>(pageInfo.getTotal(), pageInfo.getList(),currentPage,pageSize);
        return ResultUtil.getSuccessPage(RescVO.class, pageVO);
    }

    @Override
    @Transactional
    public ResultVO<Void> save(RescVO rescVO) {

        String rescName = rescVO.getRescName();
        String rescUrl = rescVO.getRescUrl();
        String rescDomain = rescVO.getRescDomain();
        String methodType = rescVO.getMethodType();

        if(StringUtils.isBlank(rescName) || StringUtils.length(rescName) > 20){
            return ResultUtil.getWarn("资源名称不能为空,且在20个字符以内.");
        }

        if(StringUtils.isBlank(rescUrl) || StringUtils.length(rescUrl) > 50){
            return ResultUtil.getWarn("资源url不能为空,且在50个字符以内.");
        }

        if(StringUtils.isBlank(rescDomain)){
            return ResultUtil.getWarn("所属项目不能为空");
        }

        if(StringUtils.isBlank(methodType)){
            return ResultUtil.getWarn("请求方式不能为空");
        }

        // 资源url唯一性校验
        RescExample example = new RescExample();
        example.createCriteria().andRescUrlEqualTo(rescVO.getRescUrl());
        long count = this.rescDAO.countByExample(example);
        if(count>0){

            throw new BusinessException("资源: [ "+rescUrl+" ],已经存在!");
        }

        RescModel insert  = new RescModel();
        insert.setCreateTime(new Date());
        insert.setUpdateTime(new Date());
        insert.setRescDomain(rescDomain);
        insert.setRescUrl(rescUrl.trim());
        insert.setRescName(rescName);
        insert.setMethodType(methodType);

        this.rescDAO.insertSelective(insert);
        return ResultUtil.getSuccess();
    }

    @Override
    public ResultVO<Void> update(RescVO rescVO) {

        String rescName = rescVO.getRescName();
        Integer rescId = rescVO.getRescId();
        String rescUrl = rescVO.getRescUrl();
        String rescDomain = rescVO.getRescDomain();
        String methodType = rescVO.getMethodType();


        if(StringUtils.isBlank(rescDomain)){
            return ResultUtil.getWarn("所属项目不能为空");
        }

        if(StringUtils.isBlank(rescName) || StringUtils.length(rescName) > 20){
            return ResultUtil.getWarn("资源名称不能为空,且在20个字符以内.");
        }

        if(StringUtils.isBlank(rescUrl) || StringUtils.length(rescUrl) > 50){
            return ResultUtil.getWarn("资源url不能为空,且在50个字符以内.");
        }

        if(StringUtils.isBlank(methodType)){
            return ResultUtil.getWarn("请求方式不能为空");
        }

        RescExample example = new RescExample();
        example.createCriteria().andRescUrlEqualTo(rescUrl).andRescIdNotEqualTo(rescId);
        long count = this.rescDAO.countByExample(example);

        if(count>0){

            return ResultUtil.getFail("资源: [ "+rescUrl+" ],已经存在!");
        }

        RescModel update = new RescModel();
        update.setRescId(rescId);
        update.setRescName(rescName);
        update.setRescDomain(rescDomain);
        update.setRescUrl(rescUrl);
        update.setUpdateTime(new Date());
        update.setMethodType(methodType);

        this.rescDAO.updateByPrimaryKeySelective(update);
        return ResultUtil.getSuccess();
    }

    @Override
    @Transactional
    public ResultVO<Void> delete(Integer rescId) {


        // 校验该资源是否绑定了功能.
        FuncRescExample example = new FuncRescExample();
        example.createCriteria().andRescIdEqualTo(rescId);
        long count = this.funcRescDAO.countByExample(example);

        if(count>0){
            throw new BusinessException("该资源已经绑定功能,请解绑后进行删除.");
        }

        this.rescDAO.deleteByPrimaryKey(rescId);

        return ResultUtil.getSuccess();
    }


    @Override
    public ResultVO<RescVO> select(Integer rescId) {
        RescModel resourceModel = this.rescDAO.selectByPrimaryKey(rescId);
        RescVO rescVO = new RescVO();
        BeanUtils.copyProperties(resourceModel, rescVO);
        return ResultUtil.getSuccess(RescVO.class, rescVO);
    }


    @Override
    public ResultVO<List<RescVO>> search(String keyword) {
        List<RescVO> rescVOList = this.rescDAO.selectByKeyword(keyword);
        return ResultUtil.getSuccessList(RescVO.class, rescVOList);
    }

    @Override
    public List<RescVO> selectRescListByUsername(String username) {
        return this.rescDAO.selectRescListByUsername(username);
    }

    @Override
    public ResultVO<Map<String, Set<Integer>>> getSystemAllRescMap(String project) {

        Map<String, Set<Integer>> map =  new HashMap<>();

        RescExample example = new RescExample();

        if(StringUtils.isNotBlank(project)){
            example.createCriteria().andRescDomainEqualTo(project);
        }

        List<RescModel> resourceModels = this.rescDAO.selectByExample(example);
        Set<Integer> rescIds;

        for (RescModel resc : resourceModels) {


            if(map.containsKey(resc.getRescUrl())){

                rescIds = map.get(resc.getRescUrl());
            }else {

                rescIds = new HashSet<>();
            }


            rescIds.add(resc.getRescId());
            map.put(resc.getRescUrl(),rescIds);

        }

        ResultVO<Map<String, Set<Integer>>> resultVO =  new ResultVO<>();
        resultVO.setCode(ResultConstant.RESULT_CODE_200);
        resultVO.setResult(map);

        return resultVO;
    }
}
