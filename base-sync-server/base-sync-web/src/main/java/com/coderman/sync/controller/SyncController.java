package com.coderman.sync.controller;

import com.coderman.api.util.ResultUtil;
import com.coderman.api.vo.ResultVO;
import com.coderman.service.util.UUIDUtils;
import com.coderman.sync.util.MsgBuilder;
import com.coderman.sync.util.ProjectEnum;
import com.coderman.sync.util.SyncUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

@RestController
public class SyncController {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @GetMapping(value = "/sync")
    @SuppressWarnings("all")
    public ResultVO<Void> sync() {

        String sql = "insert into db1_user(username,age,create_time) values(?,?,?)";

        PreparedStatementCreator preparedStatementCreator = connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setObject(1, UUIDUtils.getPrimaryValue());
            preparedStatement.setObject(2, new Random().nextInt());
            preparedStatement.setObject(3,new Date());
            return preparedStatement;
        };

        // 是 KeyHolder 接口的实现类 用来接受返回的组件
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        // 执行
        jdbcTemplate.update(preparedStatementCreator,generatedKeyHolder);

        int userId = (int) Objects.requireNonNull(generatedKeyHolder.getKey()).longValue();

        SyncUtil.sync(
                MsgBuilder.create("insert_datasource1_datasource2_user", ProjectEnum.SYS1, ProjectEnum.SYS2)
                        .addIntList("insert_datasource1_datasource2_user", Arrays.asList(userId))
//                        .addIntList("update_datasource1_datasource2_user",  Arrays.asList(userId))
                        .build()
        );

        return ResultUtil.getSuccess();
    }
}
