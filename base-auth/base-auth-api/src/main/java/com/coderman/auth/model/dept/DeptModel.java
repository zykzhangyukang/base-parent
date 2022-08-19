package com.coderman.auth.model.dept;

import com.coderman.api.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
@ApiModel(value="DeptModel", description = "")
public class DeptModel extends BaseModel {
    

    @ApiModelProperty(value = "")
    private Integer deptId;

    @ApiModelProperty(value = "")
    private String deptCode;

    @ApiModelProperty(value = "")
    private String deptName;

    @ApiModelProperty(value = "")
    private Date createTime;

    @ApiModelProperty(value = "")
    private Date updateTime;
}