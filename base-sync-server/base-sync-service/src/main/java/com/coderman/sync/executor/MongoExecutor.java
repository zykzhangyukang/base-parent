package com.coderman.sync.executor;

import com.coderman.sync.exception.ErrorCodeEnum;
import com.coderman.sync.exception.SyncException;
import com.coderman.sync.sql.meta.SqlMeta;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

@Slf4j
public class MongoExecutor extends AbstractExecutor{

    @Override
    public List<SqlMeta> execute() throws Throwable {

        MongoTemplate mongoTemplate = super.getMongoTemplate();

        if(null == mongoTemplate){

            throw new SyncException(ErrorCodeEnum.DB_NOT_CONNECT);
        }

        for (SqlMeta sqlMeta : super.getSqlList()) {


            log.debug("执行SQL语句->"+sqlMeta.getSql());

            Document document = mongoTemplate.executeCommand(sqlMeta.getSql());

        }

        return null;
    }



}
