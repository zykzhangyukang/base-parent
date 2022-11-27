package com.coderman.sync.init;

import com.coderman.service.util.UUIDUtils;
import com.coderman.sync.context.SyncContext;
import com.coderman.sync.plan.PlanModel;
import com.coderman.sync.plan.meta.PlanMeta;
import com.coderman.sync.plan.parser.MetaParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Lazy(value = false)
@Component
@Slf4j
public class SyncPlanInitializer {


    public synchronized void init(){


        List<PlanModel> list = new ArrayList<>();
        list.add(
                new PlanModel(){{
                    setPlanCode("insert_order_sms_customer");
                    setSrcDb("ecp_order");
                    setDestDb("bos_sms");
                    setSrcProject("order");
                    setDescProject("sms");
                    setStatus("normal");
                    setUud(UUIDUtils.getPrimaryValue());
                    setUpdateTime(new Date());
                    setPlanContent("<sync_plan>\n" +
                            "    <code>insert_datasource1_datasource2_user</code>\n" +
                            "    <name>新增商品目录</name>\n" +
                            "    <database src=\"datasource1\" dest=\"datasource2\" />\n" +
                            "    <project src=\"sys1\" dest=\"sys2\" />\n" +
                            "\n" +
                            "    <table code=\"update_datasource1_datasource2_user\" src=\"db1_user\" dest=\"db2_user\" type=\"update\">\n" +
                            "        <unique src=\"user_id\" dest=\"user_id\"  />\n" +
                            "        <relate src=\"user_id\" dest=\"user_id\" />\n" +
                            "        <column src=\"user_id\" dest=\"user_id\" />\n" +
                            "        <column src=\"username\" dest=\"username\" />\n" +
                            "        <column src=\"age\" dest=\"age\" />\n" +
                            "        <column src=\"create_time\" dest=\"create_time\" />\n" +
                            "    </table>\n" +
                            "    <table code=\"insert_datasource1_datasource2_user\" src=\"db1_user\" dest=\"db2_user\" type=\"insert\">\n" +
                            "        <unique src=\"user_id\" dest=\"user_id\"  />\n" +
                            "        <relate src=\"user_id\" dest=\"user_id\" />\n" +
                            "        <column src=\"user_id\" dest=\"user_id\" />\n" +
                            "        <column src=\"username\" dest=\"username\" />\n" +
                            "        <column src=\"age\" dest=\"age\" />\n" +
                            "        <column src=\"create_time\" dest=\"create_time\" />\n" +
                            "    </table>\n" +
                            "    <table code=\"delete_datasource1_datasource2_user\" src=\"db1_user\" dest=\"db2_user\" type=\"delete\">\n" +
                            "        <unique src=\"user_id\" dest=\"user_id\" />\n" +
                            "        <relate src=\"user_id\" dest=\"user_id\" />\n" +
                            "    </table>\n" +
                            "</sync_plan>");
                }}
        );

        List<PlanMeta> resultList = new ArrayList<>();

        for (PlanModel planModel : list) {


            try {

                PlanMeta planMeta = MetaParser.parse(planModel.getPlanContent());
                resultList.add(planMeta);

            }catch (Exception E){

                throw new RuntimeException("初始化同步计划失败");
            }
        }

        // 加锁,阻塞所有同步任务
        SyncContext.getContext().addSyncLocker();


        // 等待进行中的同步任务完成
        SyncContext.getContext().waitSyncEnd();


        // 清理上下文环境中的数据
        MetaParser.clearAllPlanInfo();

        // 更新上下文中的数据
        for (PlanMeta planMeta : resultList) {

            MetaParser.putPlanToContext(planMeta);
        }

        // 释放锁
        SyncContext.getContext().releaseSyncLocker();
    }

}
