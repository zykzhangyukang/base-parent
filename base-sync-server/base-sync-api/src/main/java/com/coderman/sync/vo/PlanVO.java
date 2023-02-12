package com.coderman.sync.vo;

import com.coderman.sync.plan.PlanModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PlanVO extends PlanModel {

    private String description;
}
