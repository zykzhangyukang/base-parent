package com.coderman.sync.vo;

import com.coderman.api.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResultVO extends BaseModel {
    private Date startTime;
    private Date endTime;
    private String keywords;
    private String planCode;
    private String syncStatus;
    private String msgSrc;
    private String srcProject;
    private String destProject;
    private String planSrc;
    private Integer repeatCount;
}
