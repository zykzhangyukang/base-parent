package com.coderman.log.entity;


/**
 * 告警参数
 * token: 0bbcbadbfc05e6ef9d93657a43c51df7d1811e91cf806ffa0d7316a3dabb15a7
 * secret: SECc8e5b8c7b4d098bd5464747eaaa82ca65e8be3b2968be8fae399112f6c17bd5a
 * @author coderman
 */
public class AlarmMessage {

    /**
     * 告警名称，区分唯一性，方便控制告警时间间隔
     */
    private String alarmName;

    /**
     * 告警类型
     */
    private AlarmTypeEnum alarmType;

    /**
     * 告警消息
     */
    private String message;

    /**
     * 钉钉机器人access_token
     */
    private String accessToken;

    /**
     * 钉钉机器人secret
     */
    private String accessKeySecret;

    /**
     * 对接外部API地址
     */
    private String apiUrl;

    /**
     * 告警时间间隔，单位分钟
     */
    private int alarmTimeInterval = 1;

    public String getAlarmName() {
        return alarmName;
    }

    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
    }

    public AlarmTypeEnum getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(AlarmTypeEnum alarmType) {
        this.alarmType = alarmType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public int getAlarmTimeInterval() {
        return alarmTimeInterval;
    }

    public void setAlarmTimeInterval(int alarmTimeInterval) {
        this.alarmTimeInterval = alarmTimeInterval;
    }
}
