package com.coderman.auth.model.func;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode
public class FuncModel {
    private Integer funcId;

    private String funcName;

    private String funcKey;

    private String funcType;

    private String funcIcon;

    private Integer funcSort;

    private Boolean dirHide;

    private Integer parentId;

    private Date createTime;

    private Date updateTime;
}