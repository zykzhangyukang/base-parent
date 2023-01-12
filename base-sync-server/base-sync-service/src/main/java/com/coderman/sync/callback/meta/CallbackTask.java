package com.coderman.sync.callback.meta;

import com.coderman.sync.task.base.BaseTask;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CallbackTask extends BaseTask {

    private String uuid;

    private String msg;

    private String db;

    private String project;

    private boolean first;


}
