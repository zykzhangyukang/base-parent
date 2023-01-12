package com.coderman.sync.callback.meta;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CallBackNode {

    private List<String> availableList = new ArrayList<>();

    private List<String> unavailableList = new ArrayList<>();

    public String getCallbackUrl(){

        if(null == this.availableList || this.availableList.size() == 0){

            return null;
        }

        int num = this.availableList.size();

        if(num == 1){

            return this.getAvailableList().get(0);
        }

        int random = (int) (Math.random() * 100);
        int randomIndex = random % num;

        return this.availableList.get(randomIndex);
    }
}
