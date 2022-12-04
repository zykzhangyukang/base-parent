package com.coderman.sync.plan.parser;

import com.coderman.sync.context.SyncContext;
import com.coderman.sync.pair.SyncPair;
import com.coderman.sync.plan.meta.ColumnMeta;
import com.coderman.sync.plan.meta.PlanBuilder;
import com.coderman.sync.plan.meta.PlanMeta;
import com.coderman.sync.plan.meta.TableMeta;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class MetaParser {


    public static PlanMeta parse(String content) {


        Document document = Jsoup.parse(content, "", Parser.xmlParser());


        // 解析同步计划
        PlanBuilder planBuilder = PlanBuilder.create(document.selectFirst("code").text().trim(), document.selectFirst("name").text().trim(), content);

        // 项目名称
        Element projectEle = document.selectFirst("project");
        planBuilder.addProjectMeta(projectEle.attr("src").trim(), projectEle.attr("dest").trim());

        // 数据库名称
        Element dbEle = document.selectFirst("database");
        planBuilder.addDbMeta(dbEle.attr("src").trim(), dbEle.attr("dest").trim());

        // 回调
        Element callbacksEle = document.selectFirst("callbacks");
        if (null != callbacksEle) {

            Elements callbacks = callbacksEle.select("callback");
            for (Element callback : callbacks) {

                planBuilder.addCallbackMeta(callback.attr("dest").trim(), callback.attr("desc").trim());
            }
        }

        // table
        Elements tables = document.select("table");

        List<String> tableCodeList = new ArrayList<>();

        for (Element table : tables) {


            String tableCode = table.attr("code").trim();

            TableMeta tableMeta = planBuilder.addTableMeta(
                    tableCode,
                    table.attr("src").trim(),
                    table.attr("dest").trim(),
                    table.attr("type").trim()
            );


            tableCodeList.add(tableCode);


            // 同步字段
            Element unique = table.selectFirst("unique");
            tableMeta.setUnique(new SyncPair<>(unique.attr("src").trim(), unique.attr("dest").trim()));


            if (unique.hasAttr("type")) {

                tableMeta.setUniqueType(unique.attr("type").trim());
            }


            // 关联字段
            Element relate = table.selectFirst("relate");
            tableMeta.setRelate(new SyncPair<>(relate.attr("src").trim(), relate.attr("dest").trim()));


            // 同步内容
            Elements columns = table.select("column");

            if (columns.size() > 0) {

                for (Element column : columns) {


                    ColumnMeta columnMeta = tableMeta.addColumnMeta(column.attr("src").trim(), column.attr("dest").trim());

                    if (column.hasAttr("type")) {

                        columnMeta.setType(column.attr("type").trim());
                    }

                }
            }
        }

        PlanMeta planMeta = planBuilder.build();
        planMeta.setTableCodeList(tableCodeList);
        return planMeta;
    }


    public static void clearAllPlanInfo() {

        SyncContext.getContext().clearProjectDb();


        SyncContext.getContext().clearPlanMeta();
    }

    public static void putPlanToContext(PlanMeta planMeta) {

        SyncContext.getContext().relateProjectDb(planMeta.getProjectMeta().getSrcProject(), planMeta.getDbMeta().getSrcDb());
        SyncContext.getContext().relateProjectDb(planMeta.getProjectMeta().getDestProject(), planMeta.getDbMeta().getDestDb());

        // 放入上下文
        SyncContext.getContext().addPlanMeta(planMeta.getCode(), planMeta);
    }
}
