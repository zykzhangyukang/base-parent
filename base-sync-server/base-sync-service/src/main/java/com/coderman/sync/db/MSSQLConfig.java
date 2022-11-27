package com.coderman.sync.db;

public class MSSQLConfig extends JdbcConfig{

    public static final String DRIVER_JTDS = "net.sourceforge.jtds.jdbc.Driver";
    public static final String DRIVER_MICROSOFT = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    private String driverClassName = DRIVER_JTDS;

    private String logAbandoned;
    private String removeAbandoned;
    private String removeAbandonedTimeout;


    public static String getDriverJtds() {
        return DRIVER_JTDS;
    }

    public static String getDriverMicrosoft() {
        return DRIVER_MICROSOFT;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getLogAbandoned() {
        return logAbandoned;
    }

    public void setLogAbandoned(String logAbandoned) {
        this.logAbandoned = logAbandoned;
    }

    public String getRemoveAbandoned() {
        return removeAbandoned;
    }

    public void setRemoveAbandoned(String removeAbandoned) {
        this.removeAbandoned = removeAbandoned;
    }

    public String getRemoveAbandonedTimeout() {
        return removeAbandonedTimeout;
    }

    public void setRemoveAbandonedTimeout(String removeAbandonedTimeout) {
        this.removeAbandonedTimeout = removeAbandonedTimeout;
    }
}
