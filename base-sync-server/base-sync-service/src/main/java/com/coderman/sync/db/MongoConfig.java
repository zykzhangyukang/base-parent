package com.coderman.sync.db;

public class MongoConfig extends AbstractDbConfig{

    private String db;

    private Integer connectionsPerHost;

    private Integer connectTimeout;

    private Integer maxWaitTime;

    private Integer serverSelectionTimeout;

    private String socketKeepAlive;

    private Integer socketTimeout;

    private Integer threadsAllowedToBlockForConnectionMultiplier;


    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public Integer getConnectionsPerHost() {
        return connectionsPerHost;
    }

    public void setConnectionsPerHost(Integer connectionsPerHost) {
        this.connectionsPerHost = connectionsPerHost;
    }

    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Integer getMaxWaitTime() {
        return maxWaitTime;
    }

    public void setMaxWaitTime(Integer maxWaitTime) {
        this.maxWaitTime = maxWaitTime;
    }

    public Integer getServerSelectionTimeout() {
        return serverSelectionTimeout;
    }

    public void setServerSelectionTimeout(Integer serverSelectionTimeout) {
        this.serverSelectionTimeout = serverSelectionTimeout;
    }

    public String getSocketKeepAlive() {
        return socketKeepAlive;
    }

    public void setSocketKeepAlive(String socketKeepAlive) {
        this.socketKeepAlive = socketKeepAlive;
    }

    public Integer getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(Integer socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public Integer getThreadsAllowedToBlockForConnectionMultiplier() {
        return threadsAllowedToBlockForConnectionMultiplier;
    }

    public void setThreadsAllowedToBlockForConnectionMultiplier(Integer threadsAllowedToBlockForConnectionMultiplier) {
        this.threadsAllowedToBlockForConnectionMultiplier = threadsAllowedToBlockForConnectionMultiplier;
    }
}
