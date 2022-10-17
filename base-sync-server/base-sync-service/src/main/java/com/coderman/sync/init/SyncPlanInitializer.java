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
                            "    <code>insert_sku_pim_catalog</code>\n" +
                            "    <name>新增商品目录</name>\n" +
                            "    <database src=\"bos_sku\" dest=\"ecp_pim\" />\n" +
                            "    <project src=\"sku\" dest=\"pim\" />\n" +
                            "    <callbacks>\n" +
                            "        <callback dest=\"pim\" desc=\"新增商品目录\"/>\n" +
                            "    </callbacks>\n" +
                            "    <table code=\"insert_sku_pim_catalog\" src=\"sku_catalog\" dest=\"ecp_catalog\" type=\"insert\">\n" +
                            "        <unique src=\"catalog_id\" dest=\"catalog_id\" />\n" +
                            "        <relate src=\"catalog_id\" dest=\"catalog_id\" />\n" +
                            "        <column src=\"catalog_id\" dest=\"catalog_id\" />\n" +
                            "        <column src=\"uuid\" dest=\"uuid\" />\n" +
                            "        <column src=\"catalog_code\" dest=\"catalog_code\" />\n" +
                            "        <column src=\"keyword\" dest=\"keyword\" />\n" +
                            "        <column src=\"catalog_name\" dest=\"catalog_name\" />\n" +
                            "        <column src=\"parent_id\" dest=\"parent_id\" />\n" +
                            "        <column src=\"sort\" dest=\"sort\" />\n" +
                            "    </table>\n" +
                            "    <table code=\"insert_sku_pim_catalog_intro\" src=\"sku_catalog_intro\" dest=\"ecp_catalog_intro\" type=\"insert\">\n" +
                            "        <unique src=\"catalog_id\" dest=\"catalog_id\" />\n" +
                            "        <relate src=\"catalog_id\" dest=\"catalog_id\" />\n" +
                            "        <column src=\"catalog_id\" dest=\"catalog_id\" />\n" +
                            "        <column src=\"catalog_intro\" dest=\"catalog_intro\" />\n" +
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
