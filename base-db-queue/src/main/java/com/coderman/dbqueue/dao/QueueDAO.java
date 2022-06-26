package com.coderman.dbqueue.dao;


import com.coderman.dbqueue.model.QueueNode;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author coderman
 * @Title: DAO
 * @Description: TOD
 * @date 2022/6/1814:42
 */

@Component
public class QueueDAO {

    private static String PUSH_SQL = "insert into %spub_queue (queue_name,business_data,create_time,is_consumed) values(:queueName,:businessData,:createTime,0)";
    private static final String POP_SQL_FIRST = "select top";
    public static String POP_SQL_SECOND = "node_id as nodeId,business_data as businessData from %spub_queue where queue_name = :queueName and is_consumed=0 order by create_time asc";
    public static String DELETE_SQL = "delete from pub_queue where is_consumed=1 and create_time < dateadd(day,:clearDay,getutcdate())";
    public static String POP_SQL_MYSQL= "select node_id as nodeId,business_data as businessData from %spub_queue where queue_name =:queueName and is_consumed=0 order by create_time asc limit ";
    public static String DELETE_SQL_POSTGRESQL="delete from %spub_queue where is_consumed=1 and create_time <cast(now()+':clearDay day' as TIMESTAMP(0))";
    public static String CONSUME_SQL = "update %spub_queue set is_consumed =1 where node_id in (:idList)";
    public static final String SQL_TYPE_SQL_SERVER = "sqlServer";
    public static final String SQL_TYPE_MYSQL = "mysql";
    public static final String SQL_TYPE_POSTGRESQL="postgresql";

    public static String sqlType;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    static {

        String tableSchema = "";

        sqlType = System.getProperty("queue.db.sqlType");
        if(StringUtils.isEmpty(sqlType)){

            throw new RuntimeException("请配置queue.db.sqlType");
        }

        if(!SQL_TYPE_MYSQL.equals(sqlType) && !SQL_TYPE_SQL_SERVER.equals(sqlType) && !SQL_TYPE_POSTGRESQL.equals(sqlType)){
            throw new RuntimeException("请配置queue.db.sqlType 配置错误,目前只支持sqlserver,mysql,postgresql");
        }

        if(SQL_TYPE_POSTGRESQL.equals(sqlType)){
            tableSchema = System.getProperty("postgresql.tableSchema")+'.';
            DELETE_SQL = DELETE_SQL_POSTGRESQL;
        }

        PUSH_SQL = String.format(PUSH_SQL,tableSchema);
        POP_SQL_SECOND = String.format(POP_SQL_SECOND,tableSchema);
        POP_SQL_MYSQL = String.format(POP_SQL_MYSQL,tableSchema);
        CONSUME_SQL = String.format(CONSUME_SQL,tableSchema);
    }


    /**
     * 回写状态
     * @param idList
     * @return
     */
    public int updateStatus(List<Integer> idList){
        List<List<Integer>> partition = ListUtils.partition(idList, 2000);

        int count = 0;
        for (List<Integer> subList : partition) {

            MapSqlParameterSource parameterSource = new MapSqlParameterSource();
            parameterSource.addValue("idList",subList);

            int num = namedParameterJdbcTemplate.update(CONSUME_SQL,parameterSource);

            count += num;
        }

        return count;
    }

    /**
     * 获取一定数量的message
     * @param popNum
     * @param queueName
     * @return
     */
    public List<QueueNode> selectModelList(Integer popNum, String queueName){

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("queueName",queueName);
        return namedParameterJdbcTemplate.query(concatPopSql(popNum),mapSqlParameterSource,new BeanPropertyRowMapper<>(QueueNode.class));
    }

    /**
     *
     * @param popNum
     * @return
     */
    private String concatPopSql(Integer popNum) {
        if(SQL_TYPE_SQL_SERVER.equals(getDbType())){

            return POP_SQL_FIRST + popNum +POP_SQL_SECOND;
        }
        else {

            return POP_SQL_MYSQL + popNum;
        }
    }


    private String getDbType() {
        try {
            return sqlType;
        }catch (Exception e){
            throw new RuntimeException("获取数据库类型出错");
        }
    }

    /**
     * 持久化message
     * @param queueNodeList
     * @return
     */
    @SuppressWarnings("all")
    public int insertOfMap(List<QueueNode> queueNodeList){

        if(!CollectionUtils.isEmpty(queueNodeList)){

            List<Map<String,Object>> batchValues = new ArrayList<>(queueNodeList.size());

            for (QueueNode queueNode : queueNodeList) {
                batchValues.add(
                        new MapSqlParameterSource("queueName",queueNode.getQueueName())
                                .addValue("businessData",queueNode.getBusinessData())
                                .addValue("createTime",queueNode.getCreateTime())
                                .getValues()
                );
            }

            int[] result = namedParameterJdbcTemplate.batchUpdate(PUSH_SQL,batchValues.toArray(new Map[queueNodeList.size()]));
            return result.length;
        }

        return 0;
    }

    /**
     * 持久化message
     *
     * @param queueNodeList
     * @return
     */
    public int insert(List<QueueNode> queueNodeList) {

        int[] result = namedParameterJdbcTemplate.batchUpdate(PUSH_SQL, SqlParameterSourceUtils.createBatch(queueNodeList.toArray()));
        return result.length;
    }
}
