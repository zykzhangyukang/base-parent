package com.coderman.sync.util;

import com.coderman.sync.vo.MsgItem;
import com.coderman.sync.vo.PlanMsg;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;

public class MsgBuilder {

    private PlanMsg planMsg;


    private Map<String, MsgItem> dataMap;


    private MsgBuilder(String planCode,ProjectEnum srcProject,ProjectEnum destProject){

        this.planMsg = new PlanMsg(planCode,srcProject.getKey(),destProject.getKey());
        this.dataMap = new LinkedHashMap<>();
    }

    private MsgBuilder(String planCode,ProjectEnum srcProject,ProjectEnum destProject,String orderlyMsgKey){

        this.planMsg = new PlanMsg(planCode,srcProject.getKey(),destProject.getKey(),orderlyMsgKey);
        this.dataMap = new LinkedHashMap<>();
    }


    /**
     * 创建同步计划
     *
     * @param planCode
     * @param srcProject
     * @param destProject
     * @return
     */
    public static MsgBuilder create(String planCode,ProjectEnum srcProject,ProjectEnum destProject){

        return new MsgBuilder(planCode,srcProject,destProject);
    }

    /**
     * 顺序同步计划
     * @param planCode
     * @param destProject
     * @param businessKey
     * @return
     */
    public static MsgBuilder createOrderlyMsg(String planCode,ProjectEnum srcProject,ProjectEnum destProject,String businessKey){

        return new MsgBuilder(planCode,srcProject,destProject,businessKey);
    }


    /**
     * 添加同步信息
     *
     * @param code
     * @param uniqueList
     * @return
     */
    public MsgBuilder addIntList(String code, List<Integer> uniqueList){

        if(CollectionUtils.isEmpty(uniqueList)){


            throw new RuntimeException("uniqueList不允许为空");
        }

        if(!dataMap.containsKey(code)){

            this.dataMap.put(code,new MsgItem(code));
        }

        for (Integer unique : uniqueList) {

            if(unique == null){

                throw new RuntimeException("unique不允许为空");
            }

            this.dataMap.get(code).getUnique().add(unique.toString());
        }

        return this;
    }


    /**
     * 添加同步信息
     *
     * @param code
     * @return
     */
    public MsgBuilder add(String code, String... uniques){

        List<String> uniqueList = Arrays.asList(uniques);

        if(CollectionUtils.isEmpty(uniqueList)){


            throw new RuntimeException("uniqueList不允许为空");
        }

        if(!dataMap.containsKey(code)){

            this.dataMap.put(code,new MsgItem(code));
        }

        for (String unique : uniqueList) {

            if(unique == null){

                throw new RuntimeException("unique不允许为空");
            }

            this.dataMap.get(code).getUnique().add(unique);
        }

        return this;
    }

    public MsgBuilder addWithMustAffectRows(String code,String unique,Integer mustAffectRows){

        ArrayList<String> uniques = new ArrayList<>(1);
        uniques.add(unique);
        return this.addWithMustAffectRows(code,uniques,mustAffectRows);
    }

    public MsgBuilder addWithMustAffectRows(String code,Number unique,Integer mustAffectRows){

        ArrayList<Number> uniques = new ArrayList<>(1);
        uniques.add(unique);
        return this.addWithMustAffectRows(code,uniques,mustAffectRows);
    }



    @SuppressWarnings("all")
    private MsgBuilder addWithMustAffectRows(String code, Collection uniques,Integer mustAffectRows){

        if(CollectionUtils.isEmpty(uniques)){

            throw new RuntimeException("同步计划unique不允许为空");
        }

        if(!this.dataMap.containsKey(code)){

            this.dataMap.put(code,new MsgItem(code,mustAffectRows));
        }

        for (Object unique : uniques) {

            if(!(unique instanceof String) && !(unique instanceof Integer) && !(unique instanceof Long)){

                throw new RuntimeException("unique数据类型错误");
            }

            this.dataMap.get(code).getUnique().add(unique.toString());
        }

        return this;
    }


    public PlanMsg build(){

        for (String key : dataMap.keySet()) {


            this.planMsg.getMsgItemList().add(dataMap.get(key));
        }


        return this.planMsg;
    }

    public PlanMsg getPlanMsg() {
        return planMsg;
    }

    public void setPlanMsg(PlanMsg planMsg) {
        this.planMsg = planMsg;
    }

    public Map<String, MsgItem> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<String, MsgItem> dataMap) {
        this.dataMap = dataMap;
    }
}
