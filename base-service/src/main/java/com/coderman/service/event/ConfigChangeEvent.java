package com.coderman.service.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author coderman
 */
public class ConfigChangeEvent extends ApplicationEvent {

    private String changeKey;
    private String oldValue;
    private String newValue;

    public ConfigChangeEvent(Object source,String changeKey,String oldValue,String newValue) {
        super(source);
        this.changeKey = changeKey;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public String getChangeKey() {
        return changeKey;
    }

    public void setChangeKey(String changeKey) {
        this.changeKey = changeKey;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }
}
