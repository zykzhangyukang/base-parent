package com.coderman.sync.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

@ApiModel(value = "同步计划对象")
public class PlanMsg {

    @ApiModelProperty(value = "计划编号")
    private String planCode;

    @ApiModelProperty(value = "源系统")
    private String srcProject;

    @ApiModelProperty(value = "目标系统")
    private String descProject;

    @ApiModelProperty(value = "同步消息集合")
    private List<MsgItem> msgItemList;

    public PlanMsg() {
    }

    public PlanMsg(String planCode, String srcProject, String descProject) {
        this.planCode = planCode;
        this.srcProject = srcProject;
        this.descProject = descProject;
        this.msgItemList = new ArrayList<>();
    }

    public PlanMsg(String planCode, String srcProject, String descProject, List<MsgItem> msgItemList) {
        this.planCode = planCode;
        this.srcProject = srcProject;
        this.descProject = descProject;
        this.msgItemList = msgItemList;
    }

    public String getPlanCode() {
        return planCode;
    }

    public void setPlanCode(String planCode) {
        this.planCode = planCode;
    }

    public String getSrcProject() {
        return srcProject;
    }

    public void setSrcProject(String srcProject) {
        this.srcProject = srcProject;
    }

    public String getDescProject() {
        return descProject;
    }

    public void setDescProject(String descProject) {
        this.descProject = descProject;
    }

    public List<MsgItem> getMsgItemList() {
        return msgItemList;
    }

    public void setMsgItemList(List<MsgItem> msgItemList) {
        this.msgItemList = msgItemList;
    }
}
