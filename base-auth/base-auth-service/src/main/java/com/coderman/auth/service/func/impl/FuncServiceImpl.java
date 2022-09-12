package com.coderman.auth.service.func.impl;

import com.coderman.api.constant.ResultConstant;
import com.coderman.api.exception.BusinessException;
import com.coderman.api.util.ResultUtil;
import com.coderman.api.vo.PageVO;
import com.coderman.api.vo.ResultVO;
import com.coderman.auth.constant.AuthConstant;
import com.coderman.auth.dao.func.FuncDAO;
import com.coderman.auth.dao.func.FuncRescDAO;
import com.coderman.auth.dao.role.RoleFuncDAO;
import com.coderman.auth.dao.user.UserRoleDAO;
import com.coderman.auth.model.func.FuncExample;
import com.coderman.auth.model.func.FuncModel;
import com.coderman.auth.model.func.FuncRescExample;
import com.coderman.auth.model.role.RoleFuncExample;
import com.coderman.auth.model.role.RoleFuncModel;
import com.coderman.auth.model.user.UserRoleExample;
import com.coderman.auth.model.user.UserRoleModel;
import com.coderman.auth.service.func.FuncService;
import com.coderman.auth.vo.func.FuncQueryVO;
import com.coderman.auth.vo.func.FuncTreeVO;
import com.coderman.auth.vo.func.FuncVO;
import com.coderman.auth.vo.func.MenuVO;
import com.coderman.auth.vo.resc.RescVO;
import com.coderman.service.anntation.LogError;
import com.coderman.service.anntation.LogErrorParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author coderman
 * @Title: 功能服务
 * @Description: TOD
 * @date 2022/3/1915:40
 */
@Service
public class FuncServiceImpl implements FuncService {

    @Autowired
    private FuncDAO funcDAO;

    @Autowired
    private RoleFuncDAO roleFuncDAO;

    @Autowired
    private FuncRescDAO funcRescDAO;

    @Autowired
    private UserRoleDAO userRoleDAO;


    @Override
    @LogError(value = "获取功能树")
    public ResultVO<FuncVO> listTree() {

        FuncVO funcVO = new FuncVO();

        // 获取所有功能转成VO
        FuncExample example = new FuncExample();
        example.setOrderByClause("func_sort asc");
        List<FuncModel> funcModels = this.funcDAO.selectByExample(example);

        List<FuncTreeVO> funcTreeVOList = funcModels.stream().map(funcModel -> {
            FuncTreeVO funcTreeVO = new FuncTreeVO();
            BeanUtils.copyProperties(funcModel, funcTreeVO);
            return funcTreeVO;
        }).collect(Collectors.toList());

        // 转成Map结构
        Map<Integer, FuncTreeVO> funcVOMap = funcTreeVOList.stream().collect(Collectors.toMap(FuncTreeVO::getFuncId, e -> e));
        for (FuncTreeVO treeVo : funcTreeVOList) {
            if (funcVOMap.containsKey(treeVo.getParentId())) {
                funcVOMap.get(treeVo.getParentId()).getChildrenList().add(treeVo);
            }
        }

        // 获取所有父级节点
        List<FuncTreeVO> rootFunVoList = funcTreeVOList.stream().filter(e -> !funcVOMap.containsKey(e.getParentId())).collect(Collectors.toList());
        funcVO.setFuncTreeVOList(rootFunVoList);
        funcVO.setFuncVOList(funcTreeVOList);
        return ResultUtil.getSuccess(FuncVO.class, funcVO);
    }


    @Override
    @LogError(value = "功能列表")
    public ResultVO<PageVO<List<FuncVO>>> page(Integer currentPage, Integer pageSize, FuncQueryVO queryVO) {

        PageHelper.startPage(currentPage, pageSize);
        List<FuncVO> funcVOList = this.funcDAO.page(queryVO);


        PageInfo<FuncVO> pageInfo = new PageInfo<>(funcVOList);
        List<FuncVO> list = pageInfo.getList();
        return ResultUtil.getSuccessPage(FuncVO.class, new PageVO<>(pageInfo.getTotal(), list, currentPage, pageSize));
    }

    @Override
    @LogError(value = "保存功能")
    public ResultVO<Void> save(FuncVO funcVO) {

        Integer parentId = funcVO.getParentId();
        String funcName = funcVO.getFuncName();
        String funcKey = funcVO.getFuncKey();
        String funcType = funcVO.getFuncType();
        Integer funcSort = funcVO.getFuncSort();
        Boolean dirHide = funcVO.getDirHide();

        if (parentId == null) {

            return ResultUtil.getWarn("父级功能不能为空");
        }

        if (StringUtils.isBlank(funcName) || StringUtils.length(funcName) > 15) {

            return ResultUtil.getWarn("功能名称不能为空，且在15个字符之内");
        }

        if (StringUtils.isBlank(funcKey) || StringUtils.length(funcKey) > 30) {

            return ResultUtil.getWarn("功能key不能为空,且在30个字符之内");
        }

        if (StringUtils.isBlank(funcType)) {

            return ResultUtil.getWarn("功能类型不能为空！");
        }

        if (funcSort == null || funcSort < 0 || funcSort > 100) {
            return ResultUtil.getWarn("功能排序不能为空，请输入0-100之间的整数");
        }

        if (AuthConstant.func_type_dir.equals(funcType) && null == dirHide) {
            return ResultUtil.getWarn("请选择目录是显示还是隐藏");
        }

        // 功能key唯一性校验
        FuncExample example = new FuncExample();
        example.createCriteria().andFuncKeyEqualTo(funcKey);
        long count = this.funcDAO.countByExample(example);

        if (count > 0) {

            throw new BusinessException("存在重复功能key");
        }

        FuncModel insert = new FuncModel();
        insert.setFuncKey(funcKey);
        insert.setFuncName(funcName);
        insert.setCreateTime(new Date());
        insert.setUpdateTime(new Date());
        insert.setParentId(parentId);
        insert.setFuncType(funcType);
        insert.setFuncSort(funcSort);
        insert.setDirHide(dirHide);

        this.funcDAO.insertSelectiveReturnKey(insert);


        // 保存功能-资源关联
        if (!CollectionUtils.isEmpty(funcVO.getRescIdList())) {

            this.funcRescDAO.insertBatchByFuncId(insert.getFuncId(), funcVO.getRescIdList());
        }


        return ResultUtil.getSuccess();
    }

    @Override
    @LogError(value = "更新功能")
    public ResultVO<Void> update(FuncVO funcVO) {

        Integer funcId = funcVO.getFuncId();
        String funcName = funcVO.getFuncName();
        String funcKey = funcVO.getFuncKey();
        String funcType = funcVO.getFuncType();
        Integer funcSort = funcVO.getFuncSort();
        Boolean dirHide = funcVO.getDirHide();

        FuncModel funcModel = this.funcDAO.selectByPrimaryKey(funcId);
        if (null == funcModel) {
            throw new BusinessException("功能id不能为空");
        }

        if (StringUtils.isBlank(funcKey) || StringUtils.length(funcKey) > 30) {

            return ResultUtil.getWarn("功能key不能为空,且在30个字符之内");
        }

        if (StringUtils.isBlank(funcName) || StringUtils.length(funcName) > 15) {

            return ResultUtil.getWarn("功能名称不能为空，且在15个字符之内");
        }


        if (funcSort == null || funcSort < 0 || funcSort > 100) {
            return ResultUtil.getWarn("功能排序不能为空，请输入0-100之间的整数");
        }


        if (StringUtils.isBlank(funcType)) {

            return ResultUtil.getWarn("功能类型不能为空！");
        }


        if (AuthConstant.func_type_dir.equals(funcType) && null == dirHide) {
            return ResultUtil.getWarn("请选择目录是显示还是隐藏");
        }


        // 功能key唯一性校验
        FuncExample example = new FuncExample();
        example.createCriteria().andFuncKeyEqualTo(funcKey).andFuncIdNotEqualTo(funcId);
        Long count = this.funcDAO.countByExample(example);

        if (count > 0) {
            throw new BusinessException("存在重复的功能key");
        }

        // 更新功能
        FuncModel update = new FuncModel();
        update.setFuncId(funcId);
        update.setFuncKey(funcKey);
        update.setFuncName(funcName);
        update.setFuncType(funcType);
        update.setFuncSort(funcSort);
        update.setDirHide(dirHide);
        this.funcDAO.updateByPrimaryKeySelective(update);


        // 删除原来的功能-资源绑定
        FuncRescExample funcRescExample = new FuncRescExample();
        funcRescExample.createCriteria().andFuncIdEqualTo(funcId);
        this.funcRescDAO.deleteByExample(funcRescExample);


        // 批量增加现在的功能-资源绑定
        if (!CollectionUtils.isEmpty(funcVO.getRescIdList())) {

            this.funcRescDAO.insertBatchByFuncId(funcId, funcVO.getRescIdList());
        }

        return ResultUtil.getSuccess();
    }

    @Override
    @LogError(value = "删除功能")
    public ResultVO<Void> delete(Integer funcId) {

        FuncModel funcModel = this.funcDAO.selectByPrimaryKey(funcId);
        if (null == funcModel) {
            throw new BusinessException("功能不存在");
        }

        // 校验是否有功能-资源关联
        FuncRescExample example = new FuncRescExample();
        example.createCriteria().andFuncIdEqualTo(funcId);
        long funcResCount = this.funcRescDAO.countByExample(example);
        if (funcResCount > 0) {
            return ResultUtil.getWarn("功能已经绑定了资源,请先清空资源!");
        }


        // 校验是否有用户绑定了该功能
        RoleFuncExample roleFuncModelExample = new RoleFuncExample();
        roleFuncModelExample.createCriteria().andFuncIdEqualTo(funcId);
        List<Integer> roleIds = this.roleFuncDAO.selectByExample(roleFuncModelExample).stream().map(RoleFuncModel::getRoleId)
                .distinct().collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(roleIds)) {

            UserRoleExample userRoleModelExample = new UserRoleExample();
            userRoleModelExample.createCriteria().andRoleIdIn(roleIds);
            List<UserRoleModel> userRoleModels = this.userRoleDAO.selectByExample(userRoleModelExample);
            if (userRoleModels.size() > 0) {
                return ResultUtil.getWarn("功能已经授权给了用户,请先解绑用户.");
            }
        }

        // 校验功能是否存在子功能
        FuncExample funcModelExample = new FuncExample();
        funcModelExample.createCriteria().andParentIdEqualTo(funcId);
        long childrenCount = this.funcDAO.countByExample(funcModelExample);
        if (childrenCount > 0) {
            throw new BusinessException("功能存在子功能,无法删除");
        }

        // 删除功能
        this.funcDAO.deleteByPrimaryKey(funcId);
        return ResultUtil.getSuccess();
    }

    @Override
    @LogError(value = "获取功能")
    public ResultVO<FuncVO> select(@LogErrorParam Integer funcId) {

        FuncVO funcVO = this.funcDAO.selectFuncInfo(funcId);
        if (null == funcVO) {
            throw new BusinessException("功能不存在");
        }

        List<RescVO> rescVOList = funcVO.getRescVOList();
        if (!CollectionUtils.isEmpty(rescVOList)) {
            funcVO.setRescIdList(rescVOList.stream().map(RescVO::getRescId).collect(Collectors.toList()));
        }

        return ResultUtil.getSuccess(FuncVO.class, funcVO);
    }


    @Override
    @LogError(value = "功能解绑用户")
    public ResultVO<Void> deleteUserBind(@LogErrorParam Integer funcId) {

        // 递归查询出所有的功能id,包括子功能id
        List<Integer> funcIdList = new ArrayList<>();
        ResultVO<Void> resultVO = this.getDeepFuncIds(funcIdList, funcId);

        if(!ResultConstant.RESULT_CODE_200.equals(resultVO.getCode())){

            return resultVO;
        }

        if (CollectionUtils.isEmpty(funcIdList)) {
            return ResultUtil.getWarn("解绑的功能不存在!");
        }

        // 所谓功能解绑用户,即删除所有该功能-角色的绑定
        RoleFuncExample example = new RoleFuncExample();
        example.createCriteria().andFuncIdIn(funcIdList);
        this.roleFuncDAO.deleteByExample(example);


        return ResultUtil.getSuccess();
    }

    /**
     * 递归查询出所有子功能
     *
     * @param funcIdList 功能id集合
     * @param rootFuncId 父级id
     * @return
     */
    private ResultVO<Void> getDeepFuncIds(List<Integer> funcIdList, Integer rootFuncId) {

        FuncModel rootNode = this.funcDAO.selectByPrimaryKey(rootFuncId);

        if (StringUtils.equals(rootNode.getFuncType(), AuthConstant.func_type_dir)) {

            return ResultUtil.getWarn("目录功能不支持解绑用户");
        }

        if (AuthConstant.func_root_parent_id.equals(rootNode.getParentId())) {

            return ResultUtil.getWarn("不允许解绑最顶级的功能!");
        }


        funcIdList.add(rootFuncId);
        FuncExample example = new FuncExample();
        example.createCriteria().andParentIdEqualTo(rootFuncId);
        List<FuncModel> funcModels = this.funcDAO.selectByExample(example);

        if (!CollectionUtils.isEmpty(funcModels)) {

            for (FuncModel funcModel : funcModels) {

                getDeepFuncIds(funcIdList, funcModel.getFuncId());
            }
        }

        return ResultUtil.getSuccess();
    }


    @Override
    @LogError(value = "功能解绑资源")
    public ResultVO<Void> deleteResourceBind(@LogErrorParam Integer funcId) {

        FuncVO funcVO = this.funcDAO.selectFuncInfo(funcId);
        if (null == funcVO) {

            return ResultUtil.getWarn("功能不存在");
        }

        if (StringUtils.equals(funcVO.getFuncType(), AuthConstant.func_type_dir)) {

            return ResultUtil.getWarn("目录功能不支持解绑资源");
        }

        if (AuthConstant.func_root_parent_id.equals(funcVO.getParentId())) {

            return ResultUtil.getWarn("顶级的功能不允许解绑资源!");
        }


        // 所谓功能解绑资源,即删除所有该功能-资源的绑定
        FuncRescExample example = new FuncRescExample();
        example.createCriteria().andFuncIdEqualTo(funcId);
        this.funcRescDAO.deleteByExample(example);

        return ResultUtil.getSuccess();
    }

    @Override
    @LogError(value = "查询菜单树")
    public ResultVO<List<MenuVO>> selectMenusTreeByUserId(@LogErrorParam Integer userId) {

        // 获取所有的菜单类型的功能
        List<MenuVO> allMenus = this.funcDAO.selectAllMenusByUserId(userId);

        // 转成Map结构
        Map<Integer, MenuVO> funcVOMap = allMenus.stream().collect(Collectors.toMap(MenuVO::getFuncId, e -> e));
        for (MenuVO menuVO : allMenus) {
            if (funcVOMap.containsKey(menuVO.getParentId())) {
                funcVOMap.get(menuVO.getParentId()).getChildren().add(menuVO);
            }
        }

        // 获取所有父级节点
        List<MenuVO> rootFunVoList = allMenus.stream().filter(e -> !funcVOMap.containsKey(e.getParentId())).collect(Collectors.toList());
        return ResultUtil.getSuccessList(MenuVO.class, rootFunVoList);
    }


    @Override
    @LogError(value = "查询功能按钮key")
    public ResultVO<List<String>> selectFuncKeyListByUserId(@LogErrorParam Integer userId) {
        List<String> funcKeys = this.funcDAO.selectFuncKeyListByUserId(userId);
        return ResultUtil.getSuccessList(String.class, funcKeys);
    }
}





































