package com.coderman.auth.service.user.impl;

import com.coderman.api.constant.RedisDbConstant;
import com.coderman.api.constant.ResultConstant;
import com.coderman.api.exception.BusinessException;
import com.coderman.api.util.PageUtil;
import com.coderman.api.util.ResultUtil;
import com.coderman.api.vo.AuthUserVO;
import com.coderman.api.vo.PageVO;
import com.coderman.api.vo.ResultVO;
import com.coderman.auth.constant.AuthConstant;
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
import com.coderman.auth.service.func.FuncService;
import com.coderman.auth.service.resource.ResourceService;
import com.coderman.auth.service.user.UserService;
import com.coderman.auth.vo.func.MenuVO;
import com.coderman.auth.vo.resource.ResourceVO;
import com.coderman.auth.vo.user.UserAssignVO;
import com.coderman.auth.vo.user.UserInfoVO;
import com.coderman.auth.vo.user.UserVO;
import com.coderman.service.anntation.LogError;
import com.coderman.service.anntation.LogErrorParam;
import com.coderman.service.redis.RedisService;
import com.coderman.service.service.BaseService;
import com.coderman.service.util.MD5Utils;
import com.coderman.service.util.UUIDUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author coderman
 * @Title: 用户服务实现
 * @Description: TOD
 * @date 2022/2/2711:41
 */
@Service
public class UserServiceImpl extends BaseService implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RoleDAO roleDAO;

    @Autowired
    private UserRoleDAO userRoleDAO;


    @Autowired
    private DeptDAO deptDAO;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ResourceService resourceService;


    @Autowired
    private FuncService funcService;


    @Override
    @LogError(value = "用户登入")
    public ResultVO<AuthUserVO> login(@LogErrorParam UserVO userVO) throws BusinessException {

        try {

            String username = userVO.getUsername();
            String password = userVO.getPassword();

            if (StringUtils.isBlank(username)) {
                return ResultUtil.getWarn("用户名不能为空");
            }


            if (StringUtils.isBlank(password)) {
                return ResultUtil.getWarn("登入密码不能为空");
            }

            UserExample example = new UserExample();
            example.createCriteria().andUsernameEqualTo(userVO.getUsername()).andUserStatusEqualTo(AuthConstant.USER_STATUS_ENABLE);
            Optional<UserModel> modelOptional = this.userDAO.selectByExample(example).stream().findFirst();

            if (!modelOptional.isPresent()) {

                return ResultUtil.getWarn("用户名或密码错误");
            }

            UserModel dbUser = modelOptional.get();

            // 密码比对
            if (!StringUtils.equals(MD5Utils.md5Hex(password.getBytes()), dbUser.getPassword())) {

                return ResultUtil.getWarn("用户名或密码错误");
            }

            // 生成令牌
            String token = UUIDUtils.getPrimaryValue();


            // 会话写入redis
            AuthUserVO authUserVO = new AuthUserVO();
            authUserVO.setUsername(username);
            authUserVO.setToken(token);
            authUserVO.setDeptCode(dbUser.getDeptCode());
            authUserVO.setRealName(dbUser.getRealName());
            authUserVO.setRescIdList(getUserResourceIds(dbUser.getUsername()));

            this.redisService.setObject(AuthConstant.AUTH_TOKEN_NAME + token, authUserVO, 12 * 60 * 60, RedisDbConstant.REDIS_DB_AUTH);

            return ResultUtil.getSuccess(AuthUserVO.class, authUserVO);

        } catch (Exception e) {

            logger.error("用户登入失败,username:{},msg:{}", userVO.getUsername(), e.getMessage(), e);

            return ResultUtil.getFail("登入失败,请联系技术人员处理.");
        }

    }

    /**
     * 获取用户拥有的资源id
     *
     * @param username 用户名
     * @return
     */
    @LogError(value = "获取用户拥有的资源id")
    private List<Integer> getUserResourceIds(String username) {

        return this.resourceService.selectResourceListByUsername(username).stream()
                .map(ResourceVO::getResourceId)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    @LogError(value = "获取用户信息")
    public ResultVO<UserInfoVO> info(String token) {

        if(StringUtils.isBlank(token)){
            return ResultUtil.getFail(ResultConstant.RESULT_CODE_401,"访问令牌为空");
        }

        AuthUserVO authUserVO = this.getUserByToken(token).getResult();

        if (null == authUserVO) {
            return ResultUtil.getFail(ResultConstant.RESULT_CODE_401,"用户会话已过期");
        }

        ResultVO<UserVO> voResultVO = this.selectUserByName(authUserVO.getUsername());
        UserVO userVO = voResultVO.getResult();
        if(null == userVO){
            return ResultUtil.getFail(ResultConstant.RESULT_CODE_401,"用户会话已过期");
        }


        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(userVO, userInfoVO);

        // 查询角色
        ResultVO<List<String>> roleNamesRes = this.selectRoleNames(userVO.getUserId());
        userInfoVO.setRoles(roleNamesRes.getResult());

        // 查询菜单
        ResultVO<List<MenuVO>> listResultVO = this.funcService.selectMenusTreeByUserId(userVO.getUserId());
        userInfoVO.setMenus(listResultVO.getResult());

        // 查询功能
        ResultVO<List<String>> resultVO = this.funcService.selectFuncKeyListByUserId(userVO.getUserId());
        userInfoVO.setFuncKeys(resultVO.getResult());
        return ResultUtil.getSuccess(UserInfoVO.class, userInfoVO);
    }

    @Override
    @LogError(value = "根据token获取用户信息")
    public ResultVO<AuthUserVO> getUserByToken(String token) {
        AuthUserVO authUserVO = this.redisService.getObject(AuthConstant.AUTH_TOKEN_NAME + token, AuthUserVO.class, RedisDbConstant.REDIS_DB_AUTH);
        return ResultUtil.getSuccess(AuthUserVO.class, authUserVO);
    }

    @Override
    @LogError(value = "用户退出登入")
    public ResultVO<Void> logout(@LogErrorParam String token) {

        // 删除token
        if (StringUtils.isNotBlank(token)) {
            this.redisService.expire(AuthConstant.AUTH_TOKEN_NAME + token, 0, RedisDbConstant.REDIS_DB_AUTH);
        }

        return ResultUtil.getSuccess();
    }

    @Override
    @LogError(value = "用户刷新登入")
    public ResultVO<String> refreshLogin(String token) {

        AuthUserVO oldAuthUserVO = this.getUserByToken(token).getResult();
        if(oldAuthUserVO == null){
            return ResultUtil.getFail(ResultConstant.RESULT_CODE_401,"会话已过期,请重新登入");
        }

        // 删除当前token
        this.redisService.expire(AuthConstant.AUTH_TOKEN_NAME + oldAuthUserVO.getToken(), 0, RedisDbConstant.REDIS_DB_AUTH);

        UserExample example = new UserExample();
        example.createCriteria().andUsernameEqualTo(oldAuthUserVO.getUsername()).andUserStatusEqualTo(AuthConstant.USER_STATUS_ENABLE);
        Optional<UserModel> first = this.userDAO.selectByExample(example).stream().findFirst();


        // 签发新的token
        String newToken = UUIDUtils.getPrimaryValue();

        if (!first.isPresent()) {
            return ResultUtil.getFail(ResultConstant.RESULT_CODE_401,"用户信息不存在");
        }

        UserModel dbUser = first.get();

        AuthUserVO newAuthUserVo = new AuthUserVO();
        newAuthUserVo.setUsername(dbUser.getUsername());
        newAuthUserVo.setDeptCode(dbUser.getDeptCode());
        newAuthUserVo.setRealName(dbUser.getRealName());
        newAuthUserVo.setToken(newToken);
        newAuthUserVo.setRescIdList(getUserResourceIds(dbUser.getUsername()));
        this.redisService.setObject(AuthConstant.AUTH_TOKEN_NAME + newToken, newAuthUserVo, 12 * 60 * 60, RedisDbConstant.REDIS_DB_AUTH);
        return ResultUtil.getSuccess(String.class,newToken);
    }


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

        Map<String, Object> conditionMap = new HashMap<>();

        if (StringUtils.isNotBlank(queryVO.getUsername())) {
            conditionMap.put("username", queryVO.getUsername());
        }

        if (StringUtils.isNotBlank(queryVO.getRealName())) {
            conditionMap.put("realName", queryVO.getRealName());
        }

        if (Objects.nonNull(queryVO.getUserStatus())) {
            conditionMap.put("userStatus", queryVO.getUserStatus());
        }

        if (StringUtils.isNotBlank(queryVO.getDeptCode())) {
            conditionMap.put("deptCode", queryVO.getDeptCode());
        }


        PageUtil.getConditionMap(conditionMap, currentPage, pageSize);

        // 总条数
        Long count = this.userDAO.countPage(conditionMap);

        // 分页
        List<UserVO> userVOList = this.userDAO.selectPage(conditionMap);

        return ResultUtil.getSuccessPage(UserVO.class, PageUtil.getPageVO(count, userVOList, currentPage, pageSize));
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
        insertModel.setPassword(MD5Utils.md5Hex(password.getBytes()));
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

        UserModel dbUserModel = this.userDAO.selectByPrimaryKey(userId);
        if(dbUserModel == null){

            return ResultUtil.getFail("用户信息不存在");
        }

        if(dbUserModel.getUserStatus().equals(AuthConstant.USER_STATUS_ENABLE)){

            return ResultUtil.getFail("启用状态的用户不能删除");
        }

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
    @LogError(value = "启用用户")
    public ResultVO<Void> updateEnable(Integer userId) {

        UserModel db = this.userDAO.selectByPrimaryKey(userId);
        if (db == null) {
            throw new BusinessException("用户不存在!");
        }

        if (AuthConstant.USER_STATUS_ENABLE.equals(db.getUserStatus())) {
            return ResultUtil.getWarn("用户已经是启用状态");
        }


        UserModel userModel = new UserModel();
        userModel.setUserId(userId);
        userModel.setUserStatus(AuthConstant.USER_STATUS_ENABLE);
        this.userDAO.updateByPrimaryKeySelective(userModel);
        return ResultUtil.getSuccess();
    }

    @Override
    public ResultVO<Void> updateDisable(Integer userId) {

        UserModel db = this.userDAO.selectByPrimaryKey(userId);
        if (db == null) {
            throw new BusinessException("用户不存在!");
        }

        if (AuthConstant.USER_STATUS_DISABLE.equals(db.getUserStatus())) {
            return ResultUtil.getWarn("用户已经是禁用状态");
        }

        UserModel userModel = new UserModel();
        userModel.setUserId(userId);
        userModel.setUserStatus(AuthConstant.USER_STATUS_DISABLE);
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
        record.setPassword(MD5Utils.md5Hex(password.getBytes()));
        this.userDAO.updateByPrimaryKeySelective(record);
        return ResultUtil.getSuccess();
    }


}
