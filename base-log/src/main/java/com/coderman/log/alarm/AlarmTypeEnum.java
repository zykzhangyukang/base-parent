package com.coderman.log.alarm;

/**
 * 告警类型
 *
 * @author coderman
 */
public enum AlarmTypeEnum {
    /**
     * 钉钉
     */
    DING_TALK("DingTalk"),
    /**
     * 外部系统
     */
    EXTERNAL_SYSTEM("ExternalSystem");

    AlarmTypeEnum(String type) {
        this.type = type;
    };

    private final String type;

    public String getType() {
        return type;
    }
}
