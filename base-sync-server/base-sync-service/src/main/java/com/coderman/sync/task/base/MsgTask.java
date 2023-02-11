package com.coderman.sync.task.base;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MsgTask extends BaseTask{

    private String msg;

    private String source;
}
