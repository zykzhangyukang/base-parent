package com.coderman.sync.db;

public class OracleConfig extends JdbcConfig{

    public static final String DRIVER_ORACLE = "oracle.jdbc.driver.OracleDriver";

    private String validationQuery = "select 1 from dual";

    private String driverClassName = DRIVER_ORACLE;

    public OracleConfig() {
        super.setValidationQuery(validationQuery);
    }

    public OracleConfig(String validationQuery, String driverClassName) {
        this.validationQuery = validationQuery;
        this.driverClassName = driverClassName;
    }

    public static String getDriverOracle() {
        return DRIVER_ORACLE;
    }

    @Override
    public String getValidationQuery() {
        return validationQuery;
    }

    public String getDriverClassName() {
        return driverClassName;
    }


    @Override
    public void setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }
}
