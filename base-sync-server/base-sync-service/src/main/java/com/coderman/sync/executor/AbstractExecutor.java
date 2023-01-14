package com.coderman.sync.executor;

import com.coderman.service.util.SpringContextUtil;
import com.coderman.sync.constant.SyncConstant;
import com.coderman.sync.context.SyncContext;
import com.coderman.sync.exception.ErrorCodeEnum;
import com.coderman.sync.exception.SyncException;
import com.coderman.sync.plan.meta.MsgMeta;
import com.coderman.sync.sql.meta.SqlMeta;
import lombok.Data;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class AbstractExecutor {


    /**
     * 执行语句集合
     */
    private List<SqlMeta> sqlList;


    private JdbcTemplate jdbcTemplate;
    private MongoTemplate mongoTemplate;
    private TransactionTemplate transactionTemplate;
    private MsgMeta msgMeta;


    @SuppressWarnings("all")
    public static AbstractExecutor build(String dbName) {


        AbstractExecutor executor = null;

        String dbType = SyncContext.getContext().getDbType(dbName);


        if(SyncConstant.DB_TYPE_MONGO.equals(dbType)){

            executor = new MongoExecutor();
            Object obj = SpringContextUtil.getBean(dbName + "_mongoTempate");

            if(null == obj){

                throw new SyncException(ErrorCodeEnum.DB_NOT_CONFIG,"数据库[" + dbName + "]配置不存在");
            }

            executor.setMongoTemplate((MongoTemplate) obj);

        }else if (SyncConstant.DB_TYPE_MYSQL.equals(dbType) || SyncConstant.DB_TYPE_MSSQL.equals(dbType) || SyncConstant.DB_TYPE_ORACLE.equals(dbType)) {

            executor = new JdbcExecutor();

            Object ob1 = SpringContextUtil.getBean(dbName + "_template");

            if (ob1 == null) {

                throw new SyncException(ErrorCodeEnum.DB_NOT_CONNECT, "数据库[" + dbName + "]配置不存在");
            }


            Object ob2 = SpringContextUtil.getBean(dbName + "_trans");

            if (ob2 == null) {

                throw new SyncException(ErrorCodeEnum.DB_NOT_CONNECT, "数据库[" + dbName + "]配置不存在");
            }

            executor.setJdbcTemplate((JdbcTemplate) ob1);
            executor.setTransactionTemplate((TransactionTemplate) ob2);

        } else {

            throw new SyncException(ErrorCodeEnum.DB_NOT_CONFIG, "数据库[" + dbName + "]类型未知或配置不存在" + dbType);
        }


        return executor;
    }


    public void sql(SqlMeta sql) {

        if (null == sqlList) {

            this.sqlList = new ArrayList<>();
        }

        this.sqlList.add(sql);
    }


    /**
     * 执行语句返回结果
     *
     * @return
     * @throws Throwable
     */
    public abstract List<SqlMeta> execute() throws Throwable;


    public List<SqlMeta> getSqlList() {
        return sqlList;
    }

    public void setSqlList(List<SqlMeta> sqlList) {
        this.sqlList = sqlList;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public TransactionTemplate getTransactionTemplate() {
        return transactionTemplate;
    }

    public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

    public MsgMeta getMsgMeta() {
        return msgMeta;
    }

    public void setMsgMeta(MsgMeta msgMeta) {
        this.msgMeta = msgMeta;
    }

    public void clear() {
        this.sqlList = null;
    }
}
