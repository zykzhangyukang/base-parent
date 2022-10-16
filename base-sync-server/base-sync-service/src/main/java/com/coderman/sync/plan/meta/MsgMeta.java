package com.coderman.sync.plan.meta;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.coderman.api.model.BaseModel;
import com.coderman.sync.constant.SyncConstant;
import com.coderman.sync.context.SyncContext;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * MQ消息元数据
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MsgMeta extends BaseModel {

    /**
     * 同步计划编号
     */
    private String planCode;

    /**
     * 消息创建时间
     */
    private Date createDate;


    /**
     * 消息类型
     */
    private String msgType;


    /**
     * 消息来源项目
     */
    private String srcProject;

    /**
     * 消息uuid
     */
    private String msgId;

    /**
     * 消息内容
     */
    private String msg;

    /**
     * 要执行的任务
     */
    private List<MsgTableMeta> tableMetaList;


    public static MsgMeta build(String msg) {

        JSONObject msgJson = (JSONObject) JSONObject.parse(msg, Feature.OrderedField);

        String planCode = msgJson.getString("plan");

        PlanMeta planMeta = SyncContext.getContext().getPlanMeta(planCode);

        List<MsgTableMeta> tableMetaList = new ArrayList<>();

        MsgMeta msgMeta = new MsgMeta();
        msgMeta.setMsg(msg);
        msgMeta.setPlanCode(planCode);

        // 消息类型
        if (msgJson.containsKey("type")) {

            msgMeta.setMsgType(msgJson.getString("type"));
        } else {

            msgMeta.setMsgType(SyncConstant.MSG_TYPE_SYNC);
        }

        // MQ消息id
        if (msgJson.containsKey("msgId")) {

            msgMeta.setMsgId(msgJson.getString("msgId"));
        }

        // 消息来源
        if (msgJson.containsKey("src")) {

            msgMeta.setSrcProject(msgJson.getString("src"));
        }

        // 创建时间
        if (msgJson.containsKey("createTime")) {

            msgMeta.setCreateDate(msgJson.getDate("createTime"));
        }

        JSONArray tablesJson = msgJson.getJSONArray("tables");

        for (int i = 0; i < tablesJson.size(); i++) {


            JSONObject json = tablesJson.getJSONObject(i);

            List<Object> uniqueList = new ArrayList<>();

            MsgTableMeta tableMeta = new MsgTableMeta();
            tableMeta.setUniqueList(uniqueList);


            // 结果影响行数
            if (json.containsKey("affectNum")) {

                tableMeta.setAffectNum(Integer.valueOf(json.getString("affectNum")));
            }

            // 获取计划中的指定的unique数据类型
            String originUnitType = null;

            try {

                if (planMeta != null) {
                    originUnitType = planMeta.getUniqueTypeByCode(json.getString("code"));
                }
            } catch (Exception ignored) {

            }

            for (int j = 0; j < ((JSONArray) msgJson.get("unique")).size(); j++) {

                String unique = ((JSONArray) msgJson.get("unique")).get(j).toString();

                if (!"int".equals(tableMeta.getUniqueType()) && StringUtils.isNumeric(unique)) {
                    tableMeta.setUniqueType("int");
                } else {
                    tableMeta.setUniqueType("string");
                }

                if (originUnitType != null) {

                    tableMeta.setUniqueType(originUnitType);
                }

                if (!uniqueList.contains(unique)) {
                    uniqueList.add(unique);
                }
            }

            tableMetaList.add(tableMeta);
        }

        msgMeta.setTableMetaList(tableMetaList);

        return msgMeta;
    }
}
