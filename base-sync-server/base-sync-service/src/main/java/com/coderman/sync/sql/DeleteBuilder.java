package com.coderman.sync.sql;

import com.coderman.sync.constant.SyncConstant;
import com.coderman.sync.pair.SyncPair;
import com.coderman.sync.util.SqlUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@Slf4j
@SuppressWarnings("all")
public class DeleteBuilder {

    private String dbType;

    private String table;

    private List<String> eqList;

    private List<SyncPair<String, Integer>> inList;

    private List<String> ltList;

    private Integer groupCount;

    private DeleteBuilder(String dbType) {

        this.dbType = dbType;
        this.groupCount = 1;
    }

    public static DeleteBuilder create(String dbType) {

        return new DeleteBuilder(dbType);
    }

    public DeleteBuilder table(String table) {

        this.table = table;
        return this;
    }

    public DeleteBuilder whereEq(String where) {

        if (null == this.eqList) {

            this.eqList = new ArrayList<>();
        }

        this.eqList.add(where);
        return this;
    }


    public DeleteBuilder whereIn(String where, int paramNum) {

        if (1 == paramNum) {

            return this.whereEq(where);
        }

        if (null == this.inList) {

            this.inList = new ArrayList<>();
        }

        this.inList.add(new SyncPair<>(where, paramNum));
        return this;
    }

    public DeleteBuilder wherelt(String where) {

        if (null == this.ltList) {
            this.ltList = new ArrayList<>();
        }

        this.ltList.add(where);
        return this;
    }

    public DeleteBuilder groupCount(Integer groupCount) {
        this.groupCount = groupCount;
        return this;
    }

    public String sql() {


        if (CollectionUtils.isEmpty(this.inList) && CollectionUtils.isEmpty(this.eqList) && CollectionUtils.isEmpty(this.ltList) || StringUtils.isBlank(this.table)) {

            return StringUtils.EMPTY;
        }

        StringBuilder builder = new StringBuilder();

        if (Arrays.asList(SyncConstant.DB_TYPE_MYSQL, SyncConstant.DB_TYPE_MSSQL, SyncConstant.DB_TYPE_ORACLE).contains(this.dbType)) {

            builder.append("delete ");
            builder.append(" from ");
            builder.append(table);
            builder.append(" where ");

            List<String> whereStrList = new ArrayList<>();

            if (CollectionUtils.isNotEmpty(this.inList)) {


                for (SyncPair<String, Integer> param : this.inList) {

                    String replaceStr = StringUtils.repeat("?", ",", param.getValue());
                    whereStrList.add(param.getKey() + "in (" + replaceStr + ")");
                }
            }

            if (CollectionUtils.isNotEmpty(this.eqList)) {

                for (String param : this.eqList) {

                    whereStrList.add(param + " = ?");
                }
            }

            if (CollectionUtils.isNotEmpty(this.ltList)) {

                for (String param : this.ltList) {

                    whereStrList.add(param + " < ?");
                }
            }


            builder.append(StringUtils.join(whereStrList, " and "));

            if (!SyncConstant.DB_TYPE_ORACLE.equals(this.dbType)) {
                builder.append(";");
            }
        } else if (SyncConstant.DB_TYPE_MONGO.equals(this.dbType)) {


            builder.append("{");
            builder.append("\"delete\":\"" + SqlUtil.getCollectionName(this.table) + "\",");
            builder.append("\"ordered\":true,");
            builder.append("\"deletes\":[");

            StringBuilder tempBuilder = new StringBuilder();
            tempBuilder.append("{\"q\":{\"$and\":[");
            tempBuilder.append(StringUtils.join(SqlUtil.makeMongoSql(this.inList, this.eqList, this.ltList), ","));
            tempBuilder.append("]},");
            tempBuilder.append("\"limit\":0}");

            builder.append(StringUtils.repeat(tempBuilder.toString(), ",", this.groupCount));
            builder.append("]}");
        }

        String resultStr = builder.toString();
        log.debug("构建SQL执行语句->" + resultStr);

        return resultStr;
    }
}
