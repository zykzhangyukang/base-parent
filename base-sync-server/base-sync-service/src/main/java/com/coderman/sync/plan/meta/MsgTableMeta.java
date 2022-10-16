package com.coderman.sync.plan.meta;

import com.coderman.api.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class MsgTableMeta extends BaseModel {

    /**
     * code 编号
     */
    private String code;

    /**
     * 影响行数
     */
    private Integer affectNum;

    /**
     * 关联关系
     */
    private List<Object> uniqueList;

    /**
     * 关联数据类型
     */
    private String uniqueType;
}
