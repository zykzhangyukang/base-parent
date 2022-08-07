package com.coderman.auth.model.func;

import com.coderman.api.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class FuncResourceModel extends BaseModel {
    private Integer funcResourceId;

    private Integer funcId;

    private Integer resourceId;
}