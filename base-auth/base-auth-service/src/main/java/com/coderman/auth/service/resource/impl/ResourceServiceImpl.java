package com.coderman.auth.service.resource.impl;

import com.coderman.api.exception.BusinessException;
import com.coderman.api.util.ResultUtil;
import com.coderman.api.vo.PageVO;
import com.coderman.api.vo.ResultVO;
import com.coderman.auth.dao.func.FuncResourceDAO;
import com.coderman.auth.dao.resource.ResourceDAO;
import com.coderman.auth.model.func.FuncResourceExample;
import com.coderman.auth.model.resource.ResourceExample;
import com.coderman.auth.model.resource.ResourceModel;
import com.coderman.auth.service.resource.ResourceService;
import com.coderman.auth.vo.resource.ResourceVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author coderman
 * @Description: TOD
 * @date 2022/3/199:13
 */
@Service
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private ResourceDAO resourceDAO;


    @Autowired
    private FuncResourceDAO funcResourceDAO;

    @Override
    public ResultVO<PageVO<List<ResourceVO>>> page(Integer currentPage, Integer pageSize, ResourceVO queryVO) {

        PageHelper.startPage(currentPage, pageSize);
        List<ResourceVO> resourceVOList = this.resourceDAO.page(queryVO);

        PageInfo<ResourceVO> pageInfo = new PageInfo<>(resourceVOList);
        PageVO<List<ResourceVO>> pageVO = new PageVO<>(pageInfo.getTotal(), pageInfo.getList());
        return ResultUtil.getSuccessPage(ResourceVO.class, pageVO);
    }

    @Override
    @Transactional
    public ResultVO<Void> save(ResourceVO resourceVO) {

        String resourceName = resourceVO.getResourceName();
        String resourceUrl = resourceVO.getResourceUrl();
        String resourceDomain = resourceVO.getResourceDomain();
        String methodType = resourceVO.getMethodType();

        if(StringUtils.isBlank(resourceName) || StringUtils.length(resourceName) > 20){
            return ResultUtil.getWarn("资源名称不能为空,且在20个字符以内.");
        }

        if(StringUtils.isBlank(resourceUrl) || StringUtils.length(resourceUrl) > 50){
            return ResultUtil.getWarn("资源url不能为空,且在50个字符以内.");
        }

        if(StringUtils.isBlank(resourceDomain)){
            return ResultUtil.getWarn("所属项目不能为空");
        }

        if(StringUtils.isBlank(methodType)){
            return ResultUtil.getWarn("请求方式不能为空");
        }

        // 资源url唯一性校验
        ResourceExample example = new ResourceExample();
        example.createCriteria().andResourceUrlEqualTo(resourceVO.getResourceUrl());
        long count = this.resourceDAO.countByExample(example);
        if(count>0){

            throw new BusinessException("资源: [ "+resourceUrl+" ],已经存在!");
        }

        ResourceModel insert  = new ResourceModel();
        insert.setCreateTime(new Date());
        insert.setUpdateTime(new Date());
        insert.setResourceDomain(resourceDomain);
        insert.setResourceUrl(resourceUrl.trim());
        insert.setResourceName(resourceName);
        insert.setMethodType(methodType);

        this.resourceDAO.insertSelective(insert);
        return ResultUtil.getSuccess();
    }

    @Override
    public ResultVO<Void> update(ResourceVO resourceVO) {

        String resourceName = resourceVO.getResourceName();
        Integer resourceId = resourceVO.getResourceId();
        String resourceUrl = resourceVO.getResourceUrl();
        String resourceDomain = resourceVO.getResourceDomain();
        String methodType = resourceVO.getMethodType();


        if(StringUtils.isBlank(resourceDomain)){
            return ResultUtil.getWarn("所属项目不能为空");
        }

        if(StringUtils.isBlank(resourceName) || StringUtils.length(resourceName) > 20){
            return ResultUtil.getWarn("资源名称不能为空,且在20个字符以内.");
        }

        if(StringUtils.isBlank(resourceUrl) || StringUtils.length(resourceUrl) > 50){
            return ResultUtil.getWarn("资源url不能为空,且在50个字符以内.");
        }

        if(StringUtils.isBlank(methodType)){
            return ResultUtil.getWarn("请求方式不能为空");
        }

        ResourceExample example = new ResourceExample();
        example.createCriteria().andResourceUrlEqualTo(resourceUrl).andResourceIdNotEqualTo(resourceId);
        long count = this.resourceDAO.countByExample(example);

        if(count>0){

            throw new BusinessException("资源: [ "+resourceUrl+" ],已经存在!");
        }

        ResourceModel update = new ResourceModel();
        update.setResourceId(resourceId);
        update.setResourceName(resourceName);
        update.setResourceDomain(resourceDomain);
        update.setResourceUrl(resourceUrl);
        update.setUpdateTime(new Date());
        update.setMethodType(methodType);

        this.resourceDAO.updateByPrimaryKeySelective(update);
        return ResultUtil.getSuccess();
    }

    @Override
    @Transactional
    public ResultVO<Void> delete(Integer resourceId) {


        // 校验该资源是否绑定了功能.
        FuncResourceExample example = new FuncResourceExample();
        example.createCriteria().andResourceIdEqualTo(resourceId);
        long count = this.funcResourceDAO.countByExample(example);

        if(count>0){
            throw new BusinessException("该资源已经绑定功能,请解绑后进行删除.");
        }

        this.resourceDAO.deleteByPrimaryKey(resourceId);

        return ResultUtil.getSuccess();
    }


    @Override
    public ResultVO<ResourceVO> select(Integer resourceId) {
        ResourceModel resourceModel = this.resourceDAO.selectByPrimaryKey(resourceId);
        ResourceVO resourceVO = new ResourceVO();
        BeanUtils.copyProperties(resourceModel, resourceVO);
        return ResultUtil.getSuccess(ResourceVO.class, resourceVO);
    }


    @Override
    public ResultVO<List<ResourceVO>> search(String keyword) {
        List<ResourceVO> resourceVOList = this.resourceDAO.selectByKeyword(keyword);
        return ResultUtil.getSuccessList(ResourceVO.class,resourceVOList);
    }

    @Override
    public List<ResourceVO> selectResourceListByUsername(String username) {
        return this.resourceDAO.selectResourceListByUsername(username);
    }
}
