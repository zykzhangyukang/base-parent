package com.coderman.sync.sql;

import com.coderman.sync.constant.SyncConstant;
import com.coderman.sync.util.SqlUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

@Slf4j
@Data
public class InsertBuilder {

    private String dbType;

    private String table;

    private List<String> columnList;

    private Integer groupCount;

    public InsertBuilder(String dbType) {

        this.dbType = dbType;
        this.groupCount = 1;
    }

    public static InsertBuilder create(String dbType) {

        return new InsertBuilder(dbType);
    }


    public InsertBuilder table(String table) {

        this.table = table;
        return this;
    }

    public InsertBuilder column(String column) {

        if (null == this.columnList) {

            this.columnList = new ArrayList<>();
        }

        this.columnList.add(column);

        return this;
    }


    public InsertBuilder groupCount(Integer groupCount) {

        this.groupCount = groupCount;
        return this;
    }


    public String sql() {


        // 检查必要元素
        if (CollectionUtils.isEmpty(this.columnList) || StringUtils.isBlank(this.table)) {

            return StringUtils.EMPTY;
        }


        StringBuilder builder = new StringBuilder();

        if (Arrays.asList(SyncConstant.DB_TYPE_MYSQL, SyncConstant.DB_TYPE_MSSQL, SyncConstant.DB_TYPE_ORACLE).contains(dbType)) {

            builder.append("insert into ");
            builder.append(this.table);
            builder.append("(");
            builder.append(StringUtils.join(this.columnList, ","));
            builder.append(")");
            builder.append(" values ");
            builder.append("(");
            builder.append(StringUtils.repeat("?", ",", this.columnList.size()));

            if (this.dbType.equals(SyncConstant.DB_TYPE_ORACLE)) {
                builder.append(")");
            } else {
                builder.append(");");
            }
        } else if (SyncConstant.DB_TYPE_MONGO.equals(this.dbType)) {


            builder.append("{");
            builder.append("\"insert\": \"").append(SqlUtil.getCollectionName(this.table));
            builder.append("\"documents\": [");

            List<String> tmpList = new ArrayList<>();

            for (int i = 0; i < this.groupCount; i++) {


                String tempStr = "[";

                List<String> insertStrList = new ArrayList<>();

                for (String column : this.columnList) {


                    insertStrList.add("\"" + SqlUtil.getFieldName(column) + "\":?");
                }

                tempStr += StringUtils.join(insertStrList, ",") + "}";
                tmpList.add(tempStr);
            }

            builder.append(StringUtils.join(tmpList, ","));

            builder.append("]}");
        }

        String resultStr = builder.toString();


        log.info("构建SQL执行语句->" + resultStr);

        return resultStr;
    }

}
