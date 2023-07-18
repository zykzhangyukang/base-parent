package com.coderman.sync.executor;

import com.coderman.sync.constant.SyncConstant;
import com.coderman.sync.exception.ErrorCodeEnum;
import com.coderman.sync.exception.SyncException;
import com.coderman.sync.sql.meta.SqlMeta;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class MongoExecutor extends AbstractExecutor {

    @Override
    @SuppressWarnings("all")
    public List<SqlMeta> execute() throws Throwable {

        MongoTemplate mongoTemplate = super.getMongoTemplate();

        if (null == mongoTemplate) {

            throw new SyncException(ErrorCodeEnum.DB_NOT_CONNECT);
        }

        for (SqlMeta sqlMeta : super.getSqlList()) {


            log.info("执行SQL语句->" + sqlMeta.getSql());

            Document commandResult  = mongoTemplate.executeCommand(sqlMeta.getSql());
            if (!commandResult .getBoolean("ok")) {

                throw new SyncException(ErrorCodeEnum.DB_MONGO_ERROR, "mongo执行异常," + commandResult .getString("errmsg"));
            }

            if (commandResult .containsKey("writeErrors")) {

                Document writeErrors = ((List<Document>) commandResult .get("writeErrors")).get(0);
                String errmsg = writeErrors.getString("errmsg");

                if (errmsg.contains("E11000 duplicate key error")) {

                    throw new SyncException(ErrorCodeEnum.DB_KEY_DUPLICATE, "键值重复," + errmsg);
                } else {

                    throw new SyncException(ErrorCodeEnum.DB_MONGO_ERROR, "mongo执行异常," + errmsg);
                }
            }

            if (SyncConstant.OPERATE_TYPE_SELECT.equalsIgnoreCase(sqlMeta.getSqlType())) {

                BasicDBList dbList = (BasicDBList) ((BasicDBObject) commandResult.get("cursor")).get("firstBatch");
                List<Map<String, Object>> result = new ArrayList<>();

                for (Object resultObj : dbList) {

                    BasicDBObject dbObject = (BasicDBObject) resultObj;
                    result.add(dbObject.toMap());
                }

                sqlMeta.setResultList(result);
            } else {

                int affectedRows = commandResult.getInteger("n");
                sqlMeta.setAffectNum(affectedRows);
            }

            return super.getSqlList();
        }

        return null;
    }


}
