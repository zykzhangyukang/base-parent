package com.coderman.sync.plan.meta;

import org.apache.commons.collections4.CollectionUtils;

import javax.swing.table.TableModel;
import java.util.ArrayList;

public class PlanBuilder {

    private PlanMeta planMeta;

    private PlanBuilder(String code,String name,String content){

        planMeta =  new PlanMeta();
        planMeta.setCode(code);
        planMeta.setName(name);
        planMeta.setContent(content);
    }

    public static PlanBuilder create(String code,String name,String content){
        return new PlanBuilder(code,name,content);
    }


    public PlanBuilder addDbMeta(String src,String dest){

        DbMeta dbMeta = new DbMeta();

        dbMeta.setSrcDb(src);
        dbMeta.setDestDb(dest);

        planMeta.setDbMeta(dbMeta);
        return this;
    }


    public PlanBuilder addProjectMeta(String src,String dest){

        ProjectMeta projectMeta = new ProjectMeta();

        projectMeta.setSrcProject(src);
        projectMeta.setDestProject(dest);

        planMeta.setProjectMeta(projectMeta);
        return this;
    }


    public PlanBuilder addCallbackMeta(String project,String desc){

      if(CollectionUtils.isEmpty(planMeta.getCallbackMetas())){

          planMeta.setCallbackMetas(new ArrayList<>());
      }

      planMeta.getCallbackMetas().add(new CallbackMeta(project,desc));

        return this;
    }

    public TableMeta addTableMeta(String code, String src, String dest, String type){

        if(CollectionUtils.isEmpty(planMeta.getTableMetas())){

            planMeta.setTableMetas(new ArrayList<>());
        }

        TableMeta tableMeta = new TableMeta();

        tableMeta.setCode(code);
        tableMeta.setSrc(src);
        tableMeta.setDest(dest);
        tableMeta.setType(type);

        planMeta.getTableMetas().add(tableMeta);

        return tableMeta;
    }


    public PlanMeta build(){

        return planMeta;
    }





}
