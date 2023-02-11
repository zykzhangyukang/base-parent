package com.coderman.sync.callback.meta;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class CallBackNode {

    private List<String> availableList = new ArrayList<>();

    private List<String> unavailableList = new ArrayList<>();

    public String getCallbackUrl() {

        if (null == this.availableList || this.availableList.size() == 0) {

            return null;
        }

        int num = this.availableList.size();

        if (num == 1) {

            return this.getAvailableList().get(0);
        }

        int random = (int) (Math.random() * 100);
        int randomIndex = random % num;

        return this.availableList.get(randomIndex);
    }

    public void addCallbackUrl(String callbackUrl) {

        this.availableList.add(callbackUrl);
    }

    public void addNoneCallbackUrl(String callbackUrl) {

        this.unavailableList.add(callbackUrl);
    }

    public void resetAvailableNode() {

        if (this.availableList.size() == 0) {

            this.availableList.add(this.unavailableList.get(0));
        }
    }

    public void switchAvailableNode(String callbackUrl) {

        synchronized (this) {

            if (StringUtils.isNotBlank(callbackUrl)) {

                this.availableList.remove(callbackUrl);
                this.availableList.add(0, callbackUrl);
                this.unavailableList.remove(callbackUrl);
            }

        }
    }


    public void switchUnAvailableNode(String callbackUrl){

        synchronized (this) {

            if (StringUtils.isNotBlank(callbackUrl)) {

                if(this.availableList.size() > 1){
                    this.availableList.remove(callbackUrl);
                }

                this.unavailableList.remove(callbackUrl);
                this.unavailableList.add(callbackUrl);
            }
        }
    }
}
