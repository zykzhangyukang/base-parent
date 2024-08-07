package com.coderman.callback;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author coderman
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SyncMsgItem {

    private String code;

    private List<String> unique;

}
