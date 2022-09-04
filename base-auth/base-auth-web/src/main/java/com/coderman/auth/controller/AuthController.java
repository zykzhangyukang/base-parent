package com.coderman.auth.controller;

import com.coderman.api.constant.RedisDbConstant;
import com.coderman.api.constant.ResultConstant;
import com.coderman.api.util.ResultUtil;
import com.coderman.api.vo.ResultVO;
import com.coderman.service.api.RescApi;
import com.coderman.service.dict.ConstItem;
import com.coderman.service.dict.ConstItems;
import com.coderman.service.redis.RedisService;
import com.mysql.cj.x.protobuf.MysqlxDatatypes;
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


    /**
     * 获取权限系统所有资源信息
     *
     * @param project 项目名称
     * @return
     */
    @PostMapping(value = "/api/resc/all")
    public ResultVO< Map<String, Set<Integer>>> sysRescAll(@RequestParam(value = "domain") String project){
        return this.rescApi.getSystemAllRescMap(project);
    }


    /**
     * 系统常量获取
     *
     * @return
     */
    @GetMapping(value = "/const/all")
    public ResultVO<Map<String,List<ConstItems>>> constAll() {

        Map<String,List<ConstItems>> resultMap = new HashMap<>();


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

                    resultMap.put(project,constItems);
                }

                return null;
            }
        });


        ResultVO<Map<String,List<ConstItems>>> resultVO = new ResultVO<>();
        resultVO.setCode(ResultConstant.RESULT_CODE_200);
        resultVO.setResult(resultMap);
        return resultVO;
    }


}
