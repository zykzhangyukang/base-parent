package com.coderman.sync.callback;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SyncMsg {

    private String msgId;


    private String planCode;


    private List<SyncMsgItem> msgItemList;


    private String msg;


}
