package com.coderman.sync.executor;

import com.coderman.sync.constant.SyncConstant;
import com.coderman.sync.exception.ErrorCodeEnum;
import com.coderman.sync.exception.SyncException;
import com.coderman.sync.sql.meta.SqlMeta;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class MongoExecutor extends AbstractExecutor{

    @Override
    @SuppressWarnings("all")
    public List<SqlMeta> execute() throws Throwable {

        MongoTemplate mongoTemplate = super.getMongoTemplate();

        if(null == mongoTemplate){

            throw new SyncException(ErrorCodeEnum.DB_NOT_CONNECT);
        }

        for (SqlMeta sqlMeta : super.getSqlList()) {


            log.debug("执行SQL语句->"+sqlMeta.getSql());

            CommandResult commandResult = mongoTemplate.executeCommand(sqlMeta.getSql());
            if(!commandResult.ok()){

                throw new SyncException(ErrorCodeEnum.DB_MONGO_ERROR,"mongo执行异常,"+commandResult.getErrorMessage());
            }

            if(commandResult.containsField("writeErrors")){

                String errmsg = ((BasicDBObject) ((BasicDBList) commandResult.get("writeErrors")).get(0)).getString("errmsg");
                if(errmsg.contains("E11000 duplicate key error")){

                    throw new SyncException(ErrorCodeEnum.DB_MONGO_DUPLICATE,"键值重复,"+errmsg);
                }else {

                    throw new SyncException(ErrorCodeEnum.DB_MONGO_ERROR,"mongo执行异常,"+errmsg);
                }
            }

            if(SyncConstant.OPERATE_TYPE_SELECT.equalsIgnoreCase(sqlMeta.getSqlType())){

                BasicDBList dbList = (BasicDBList) ((BasicDBObject) commandResult.get("cursor")).get("firstBatch");
                List<Map<String,Object>> result = new ArrayList<>();

                for (Object resultObj : dbList) {

                    BasicDBObject dbObject = (BasicDBObject) resultObj;
                    result.add(dbObject.toMap());
                }

                sqlMeta.setResultList(result);
            }else {

                sqlMeta.setAffectNum(commandResult.getInt("n"));
            }

            return super.getSqlList();
        }

        return null;
    }



}
