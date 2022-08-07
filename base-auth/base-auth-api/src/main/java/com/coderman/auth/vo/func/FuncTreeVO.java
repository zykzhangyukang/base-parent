package com.coderman.auth.vo.func;

import com.coderman.auth.model.func.FuncModel;
import com.coderman.auth.vo.resource.ResourceVO;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author coderman
 * @Title: 功能树
 * @Description: TOD
 * @date 2022/3/1915:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FuncTreeVO extends FuncModel {

    @JsonProperty(value = "value")
    private Integer funcId;

    @JsonProperty(value = "title")
    private String funcName;

    @JsonProperty(value = "key")
    private String funcKey;

    @ApiModelProperty(value = "资源信息")
    private List<ResourceVO> resourceVOList;


    private Integer parentId;

    private Date createTime;

    private Date updateTime;

    /**
     * 子功能
     */
    @JsonProperty(value = "children")
    private List<FuncTreeVO> childrenList =  new ArrayList<>();
}
