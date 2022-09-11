package com.coderman.auth.model.func;

import com.coderman.api.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@ApiModel(value="FuncModel", description = "")
public class FuncModel extends BaseModel {
    

    @ApiModelProperty(value = "主键")
    private Integer funcId;

    @ApiModelProperty(value = "功能名称")
    private String funcName;

    @ApiModelProperty(value = "功能key")
    private String funcKey;

    @ApiModelProperty(value = "功能类型(目录/功能)")
    private String funcType;

    @ApiModelProperty(value = "目录图标")
    private String funcIcon;

    @ApiModelProperty(value = "功能排序")
    private Integer funcSort;

    @ApiModelProperty(value = "是否隐藏")
    private Boolean dirHide;

    @ApiModelProperty(value = "父级功能id")
    private Integer parentId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}