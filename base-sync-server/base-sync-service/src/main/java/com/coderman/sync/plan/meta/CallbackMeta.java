package com.coderman.sync.plan.meta;

import lombok.Data;

@Data
public class CallbackMeta {

    private String project;

    private String desc;

    public CallbackMeta() {
    }

    public CallbackMeta(String project, String desc) {
        this.project = project;
        this.desc = desc;
    }
}
