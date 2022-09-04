package com.coderman.auth.controller;

import com.coderman.api.constant.CommonConstant;
import com.coderman.api.constant.RedisDbConstant;
import com.coderman.api.constant.ResultConstant;
import com.coderman.api.util.ResultUtil;
import com.coderman.api.vo.ResultVO;
import com.coderman.auth.service.user.UserService;
import com.coderman.erp.api.RescApi;
import com.coderman.service.config.PropertyConfig;
import com.coderman.service.dict.ConstItems;
import com.coderman.service.redis.RedisService;
import com.coderman.erp.vo.AuthUserVO;
import com.coderman.swagger.annotation.ApiReturnParam;
import com.coderman.swagger.annotation.ApiReturnParams;
import com.coderman.swagger.constant.SwaggerConstant;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@SuppressWarnings("all")
@RequestMapping(value = "/${domain}")
public class AuthController {

    @Autowired
    private RedisService redisService;


    @Autowired
    private RescApi rescApi;


    @Autowired
    private UserService userService;


    /**
     * 获取权限系统所有资源信息
     *
     * @param project 项目名称
     * @return
     */
    @ApiOperation(httpMethod = SwaggerConstant.METHOD_POST, value = "获取权限系统所有资源信息api")
    @PostMapping(value = "/api/resc/all")
    public ResultVO<Map<String, Set<Integer>>> sysRescAll(@RequestParam(value = "domain") String project,
                                                          @RequestParam(value = "securityCode", required = false) String securityCode) {

        if (StringUtils.isBlank(securityCode) || !StringUtils.equals(securityCode, PropertyConfig.getConfigValue("auth-security-code"))) {

            return ResultUtil.getFail("非法访问,securityCode错误");

        }else {
            return this.rescApi.getSystemAllRescMap(project);
        }

    }


    @ApiOperation(httpMethod = SwaggerConstant.METHOD_POST, value = "获取用户信息api")
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "AuthUserVO", value = {"realName", "rescIdList", "deptCode", "username", "token"}),
    })
    @PostMapping(value = "/api/user/info")
    public ResultVO<AuthUserVO> info(@RequestHeader(value = CommonConstant.USER_TOKEN_NAME, required = false) String token,
                                     @RequestHeader(value = "securityCode", required = false) String securityCode) {

        if (StringUtils.isBlank(securityCode) || !StringUtils.equals(securityCode, PropertyConfig.getConfigValue("auth-security-code"))) {

            return ResultUtil.getFail("非法访问,securityCode错误");
        } else {
            return this.userService.getUserByToken(token);
        }
    }


    /**
     * 系统常量获取
     *
     * @return
     */
    @GetMapping(value = "/const/all")
    public ResultVO<Map<String, List<ConstItems>>> constAll() {

        Map<String, List<ConstItems>> resultMap = new HashMap<>();


        this.redisService.getRedisTemplate().execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {

                StringRedisSerializer s1 = new StringRedisSerializer();
                GenericJackson2JsonRedisSerializer s2 = new GenericJackson2JsonRedisSerializer();

                connection.select(RedisDbConstant.REDIS_DB_DEFAULT);

                byte[] hashKey = s1.serialize("auth.const.all");

                Set<byte[]> fields = connection.hKeys(hashKey);

                for (byte[] field : fields) {

                    String project = new String(field);
                    List<ConstItems> constItems = (List<ConstItems>) s2.deserialize(connection.hGet(hashKey, field));

                    resultMap.put(project, constItems);
                }

                return null;
            }
        });


        ResultVO<Map<String, List<ConstItems>>> resultVO = new ResultVO<>();
        resultVO.setCode(ResultConstant.RESULT_CODE_200);
        resultVO.setResult(resultMap);
        return resultVO;
    }


}
