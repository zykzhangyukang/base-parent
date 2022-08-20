package com.coderman.auth.service.user.impl;

import com.coderman.api.exception.BusinessException;
import com.coderman.api.util.ResultUtil;
import com.coderman.api.vo.PageVO;
import com.coderman.api.vo.ResultVO;
import com.coderman.auth.constant.UserConstant;
import com.coderman.auth.dao.dept.DeptDAO;
import com.coderman.auth.dao.role.RoleDAO;
import com.coderman.auth.dao.user.UserDAO;
import com.coderman.auth.dao.user.UserRoleDAO;
import com.coderman.auth.model.dept.DeptExample;
import com.coderman.auth.model.dept.DeptModel;
import com.coderman.auth.model.role.RoleModel;
import com.coderman.auth.model.user.UserExample;
import com.coderman.auth.model.user.UserModel;
import com.coderman.auth.model.user.UserRoleExample;
import com.coderman.auth.model.user.UserRoleModel;
import com.coderman.auth.service.user.UserService;
import com.coderman.auth.vo.user.UserAssignVO;
import com.coderman.auth.vo.user.UserVO;
import com.coderman.service.anntation.LogError;
import com.coderman.service.util.MD5Utils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author coderman
 * @Title: 用户服务实现
 * @Description: TOD
 * @date 2022/2/2711:41
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RoleDAO roleDAO;

    @Autowired
    private UserRoleDAO userRoleDAO;


    @Autowired
    private DeptDAO deptDAO;


    /**
     * 用户列表
     *
     * @param currentPage
     * @param pageSize
     * @param queryVO
     * @return
     */
    @Override
    @LogError(value = "用户列表")
    public ResultVO<PageVO<List<UserVO>>> page(Integer currentPage, Integer pageSize, UserVO queryVO) {

        PageHelper.startPage(currentPage, pageSize);
        List<UserVO> userVOList = this.userDAO.page(queryVO);

        PageInfo<UserVO> pageInfo = new PageInfo<>(userVOList);
        PageVO<List<UserVO>> pageVO = new PageVO<>(pageInfo.getTotal(), pageInfo.getList(), currentPage, pageSize);

        return ResultUtil.getSuccessPage(UserVO.class, pageVO);
    }

    /**
     * 用户创建
     *
     * @param userVO
     * @return
     */
    @Override
    @LogError(value = "新增用户信息")
    public ResultVO<Void> save(UserVO userVO) {

        String username = userVO.getUsername();
        String realName = userVO.getRealName();
        String password = userVO.getPassword();

        if (StringUtils.isBlank(username)) {
            return ResultUtil.getWarn("用户账号不能为空");
        }

        if (StringUtils.length(username) < 5 || StringUtils.length(username) > 15) {

            return ResultUtil.getWarn("用户账号5-15个字符!");
        }

        if (StringUtils.isBlank(realName)) {
            return ResultUtil.getWarn("真实姓名不能为空!");
        }

        if (StringUtils.length(realName) < 2 || StringUtils.length(realName) > 10) {
            return ResultUtil.getWarn("真实姓名不能为空!");
        }

        if (StringUtils.isBlank(password)) {
            return ResultUtil.getWarn("登入密码不能为空!");
        }

        if (StringUtils.length(password) < 5 || StringUtils.length(username) > 15) {
            return ResultUtil.getWarn("登入密码5-15个字符!");
        }

        if (userVO.getUserStatus() == null) {
            return ResultUtil.getWarn("用户状态不能为空!");
        }

        if (userVO.getDeptCode() == null) {
            return ResultUtil.getWarn("用户部门不能为空!");
        }


        // 校验是否存在账号
        UserExample example = new UserExample();
        example.createCriteria().andUsernameEqualTo(username);
        long count = this.userDAO.countByExample(example);

        if (count > 0) {
            throw new BusinessException("存在重复的账号【" + username + "】!");
        }


        // 新增用户
        Date now = new Date();

        UserModel insertModel = new UserModel();
        insertModel.setUsername(username);
        insertModel.setRealName(realName);
        insertModel.setPassword(MD5Utils.encodeHexString(password.getBytes()));
        insertModel.setCreateTime(now);
        insertModel.setUpdateTime(now);
        insertModel.setUserStatus(userVO.getUserStatus());
        insertModel.setDeptCode(userVO.getDeptCode());

        this.userDAO.insert(insertModel);

        return ResultUtil.getSuccess();
    }

    /**
     * 删除用户
     *
     * @param userId
     * @return
     */
    @Override
    @LogError(value = "删除用户信息")
    public ResultVO<Void> delete(Integer userId) {

        // 删除用户-角色关联
        UserRoleExample example = new UserRoleExample();
        example.createCriteria().andUserIdEqualTo(userId);
        this.userRoleDAO.deleteByExample(example);

        // 删除用户
        this.userDAO.deleteByPrimaryKey(userId);
        return ResultUtil.getSuccess();
    }

    @Override
    @LogError(value = "更新用户信息")
    public ResultVO<Void> update(UserVO userVO) {

        String username = userVO.getUsername();
        String realName = userVO.getRealName();


        if (userVO.getDeptCode() == null) {
            throw new BusinessException("用户部门不能为空!");
        }

        if (StringUtils.isBlank(username)) {
            throw new BusinessException("用户账号不能为空");
        }

        if (userVO.getUserStatus() == null) {
            throw new BusinessException("用户状态不能为空!");
        }

        if (StringUtils.isBlank(realName)) {
            throw new BusinessException("真实姓名不能为空!");
        }

        if (StringUtils.length(username) < 5 || StringUtils.length(username) > 15) {

            throw new BusinessException("用户账号5-15个字符!");
        }

        if (StringUtils.length(realName) < 2 || StringUtils.length(realName) > 10) {
            throw new BusinessException("真实姓名2-10个字符!");
        }


        UserModel updateModel = new UserModel();


        // 更新
        updateModel.setUserId(userVO.getUserId());
        updateModel.setUpdateTime(new Date());
        updateModel.setRealName(userVO.getRealName());
        updateModel.setUserStatus(userVO.getUserStatus());
        updateModel.setDeptCode(userVO.getDeptCode());

        this.userDAO.updateByPrimaryKeySelective(updateModel);
        return ResultUtil.getSuccess();
    }

    @Override
    @LogError(value = "获取用户信息")
    public ResultVO<UserVO> select(Integer userId) {
        UserModel userModel = this.userDAO.selectByPrimaryKey(userId);
        if (null == userModel) {
            throw new BusinessException("用户不存在!");
        }

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel, userVO);
        userVO.setPassword(null); // 移除敏感信息
        return ResultUtil.getSuccess(UserVO.class, userVO);
    }

    @Override
    @LogError(value = "根据用户名获取用户信息")
    public ResultVO<UserVO> selectUserByName(String username) {

        UserExample example = new UserExample();
        example.createCriteria().andUsernameEqualTo(username);
        Optional<UserModel> first = this.userDAO.selectByExample(example).stream().findFirst();

        UserVO userVO = null;
        if (first.isPresent()) {
            userVO = new UserVO();
            BeanUtils.copyProperties(first.get(), userVO);

            // 设置部门信息
            DeptExample example1 = new DeptExample();
            example1.createCriteria().andDeptCodeEqualTo(first.get().getDeptCode());
            Optional<DeptModel> first1 = this.deptDAO.selectByExample(example1).stream().findFirst();

            if (first1.isPresent()) {

                userVO.setDeptName(first1.get().getDeptName());
            }

            // 设置角色信息
            List<String> roleNames = this.roleDAO.selectUserRoleList(userVO.getUserId()).stream().map(RoleModel::getRoleName)
                    .collect(Collectors.toList());
            userVO.setRoleList(roleNames);
        }

        return ResultUtil.getSuccess(UserVO.class, userVO);
    }

    @Override
    public ResultVO<Void> updateEnable(Integer userId) {

        UserModel db = this.userDAO.selectByPrimaryKey(userId);
        if (db == null) {
            throw new BusinessException("用户不存在!");
        }

        if (UserConstant.USER_STATUS_ENABLE.equals(db.getUserStatus())) {
            return ResultUtil.getWarn("用户已经是启用状态");
        }


        UserModel userModel = new UserModel();
        userModel.setUserId(userId);
        userModel.setUserStatus(UserConstant.USER_STATUS_ENABLE);
        this.userDAO.updateByPrimaryKeySelective(userModel);
        return ResultUtil.getSuccess();
    }

    @Override
    public ResultVO<Void> updateDisable(Integer userId) {

        UserModel db = this.userDAO.selectByPrimaryKey(userId);
        if (db == null) {
            throw new BusinessException("用户不存在!");
        }

        if (UserConstant.USER_STATUS_DISABLE.equals(db.getUserStatus())) {
            return ResultUtil.getWarn("用户已经是禁用状态");
        }

        UserModel userModel = new UserModel();
        userModel.setUserId(userId);
        userModel.setUserStatus(UserConstant.USER_STATUS_DISABLE);
        this.userDAO.updateByPrimaryKeySelective(userModel);
        return ResultUtil.getSuccess();
    }

    @Override
    public ResultVO<UserAssignVO> selectAssignInit(Integer userId) {
        UserAssignVO userAssignVO = new UserAssignVO();

        UserModel userModel = this.userDAO.selectByPrimaryKey(userId);
        if (userModel == null) {
            throw new BusinessException("需要分配的用户不存在!");
        }

        userAssignVO.setUserId(userId);

        // 查询全部角色信息
        List<RoleModel> roleModels = this.roleDAO.selectByExample(null);
        userAssignVO.setRoleList(roleModels);

        // 查询用户已有的角色
        UserRoleExample example = new UserRoleExample();
        example.createCriteria().andUserIdEqualTo(userId);
        List<UserRoleModel> userRoleModels = this.userRoleDAO.selectByExample(example);
        List<Integer> userRoleIds = userRoleModels.stream().map(UserRoleModel::getRoleId).collect(Collectors.toList());

        userAssignVO.setAssignedIdList(userRoleIds);

        return ResultUtil.getSuccess(UserAssignVO.class, userAssignVO);
    }

    @Override
    @Transactional
    public ResultVO<Void> updateAssign(Integer userId, List<Integer> assignedIdList) {


        UserModel userModel = this.userDAO.selectByPrimaryKey(userId);
        if (userModel == null) {
            throw new BusinessException("需要分配的用户不存在!");
        }

        // 清空之前的权限
        UserRoleExample example = new UserRoleExample();
        example.createCriteria().andUserIdEqualTo(userId);
        this.userRoleDAO.deleteByExample(example);

        // 批量新增
        if (!CollectionUtils.isEmpty(assignedIdList)) {

            this.userRoleDAO.insertBatchByUserId(userId, assignedIdList);
        }

        return ResultUtil.getSuccess();
    }

    @Override
    public ResultVO<List<String>> selectRoleNames(Integer userId) {

        UserModel userModel = this.userDAO.selectByPrimaryKey(userId);
        if (userModel == null) {
            throw new BusinessException("用户不存在!");
        }


        // 查询全部角色信息
        List<RoleModel> roleList = this.roleDAO.selectUserRoleList(userId);
        List<String> roleNames = roleList.stream().map(RoleModel::getRoleName).collect(Collectors.toList());

        return ResultUtil.getSuccessList(String.class, roleNames);
    }

    @Override
    public ResultVO<Void> updatePassword(Integer userId, String password) {

        UserModel userModel = this.userDAO.selectByPrimaryKey(userId);
        if (userModel == null) {
            throw new BusinessException("用户不存在!");
        }


        UserModel record = new UserModel();
        record.setUserId(userId);
        record.setPassword(MD5Utils.encodeHexString(password.getBytes()));
        this.userDAO.updateByPrimaryKeySelective(record);
        return ResultUtil.getSuccess();
    }


}
