package com.coderman.auth;

import com.coderman.service.anntation.ISpringBootApplication;
import com.coderman.sync.util.MsgBuilder;
import com.coderman.sync.util.ProjectEnum;
import com.coderman.sync.util.SyncUtil;
import com.coderman.sync.vo.PlanMsg;
import org.springframework.boot.SpringApplication;

import java.util.Arrays;

/**
 * @author coderman
 * @date 2022/7/17 14:23
 */
@ISpringBootApplication
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class,args);

        PlanMsg planMsg = MsgBuilder.create("insert_member_order_customer", ProjectEnum.MEMBER, ProjectEnum.ORDER)
                .addIntList("insert_member_order_customer", Arrays.asList(1,3,4,5,6,7))
                .build();
        SyncUtil.sync(planMsg);
    }
}
