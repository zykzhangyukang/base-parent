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

@Slf4j
@Data
public class UpdateBuilder {

    private String dbType;

    private String table;

    private List<String> columnList;

    private List<SyncPair<String, Integer>> incList;

    private List<String> eqList;

    private List<SyncPair<String, Integer>> inList;

    private List<String> ltList;

    private Integer groupCount;

    private UpdateBuilder(String dbType) {

        this.dbType = dbType;
        this.groupCount = 1;
    }

    public static UpdateBuilder create(String dbType) {

        return new UpdateBuilder(dbType);
    }

    public UpdateBuilder table(String table) {

        this.table = table;
        return this;
    }

    public UpdateBuilder whereEq(String where) {

        if (null == this.eqList) {
            this.eqList = new ArrayList<>();
        }

        this.eqList.add(where);
        return this;
    }

    public UpdateBuilder whereIn(String where, int paramNum) {

        if (paramNum == 1) {

            return this.whereEq(where);
        }

        if (null == this.inList) {

            this.inList = new ArrayList<>();
        }

        this.inList.add(new SyncPair<>(where, paramNum));
        return this;
    }


    public UpdateBuilder inc(String column, int step) {

        if (null == this.incList) {

            this.incList = new ArrayList<>();
        }
        this.incList.add(new SyncPair<>(column, step));
        return this;
    }

    public UpdateBuilder wherelt(String where) {

        if (null == this.ltList) {

            this.ltList = new ArrayList<>();
        }

        this.ltList.add(where);

        return this;
    }

    public UpdateBuilder column(String column) {

        if (null == this.columnList) {

            this.columnList = new ArrayList<>();
        }
        this.columnList.add(column);
        return this;
    }

    public UpdateBuilder groupCount(Integer groupCount) {

        this.groupCount = groupCount;
        return this;
    }

    @SuppressWarnings("all")
    public String sql() {

        if (CollectionUtils.isEmpty(this.columnList) && CollectionUtils.isEmpty(this.incList) || (CollectionUtils.isEmpty(this.inList) && CollectionUtils.isEmpty(this.eqList)) || StringUtils.isBlank(table)) {

            return StringUtils.EMPTY;
        }

        StringBuilder builder = new StringBuilder();
        if (Arrays.asList(SyncConstant.DB_TYPE_MYSQL, SyncConstant.DB_TYPE_MSSQL, SyncConstant.DB_TYPE_ORACLE).contains(this.dbType)) {

            builder.append("update ");
            builder.append(table);
            builder.append(" set ");

            List<String> updateStrList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(this.columnList)) {

                List<String> tempList = new ArrayList<>();

                for (String column : this.columnList) {

                    tempList.add(column + " = ?");
                }

                updateStrList.add(StringUtils.join(tempList, ", "));
            }

            if (CollectionUtils.isNotEmpty(this.incList)) {

                List<String> incStrList = new ArrayList<>();
                for (SyncPair<String, Integer> incParam : this.incList) {

                    incStrList.add(incParam.getKey() + " = " + incParam.getKey() + " + " + incParam.getValue());
                }
                updateStrList.add(StringUtils.join(incStrList, ","));
            }

            builder.append(StringUtils.join(updateStrList, " , "));

            builder.append(" where ");

            List<String> whereStrList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(this.inList)) {


                for (SyncPair<String, Integer> param : this.incList) {

                    String replaceStr = StringUtils.repeat("?", "?", param.getValue());
                    whereStrList.add(param.getKey() + " in (" + replaceStr + ")");
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
            if (!this.dbType.equals(SyncConstant.DB_TYPE_ORACLE)) {

                builder.append(";");
            }
        } else if (SyncConstant.DB_TYPE_MONGO.equals(this.dbType)) {

            builder.append("{");
            builder.append("\"update\":\"").append(SqlUtil.getCollectionName(this.table)).append("\",");
            builder.append("\"ordered\":true,");
            builder.append("\"updates\":[");

            StringBuilder tempBuilder = new StringBuilder();

            tempBuilder.append("{\"u\":{");
            List<String> updateStrList = new ArrayList<>();

            if (CollectionUtils.isNotEmpty(this.columnList)) {

                List<String> columnStrList = new ArrayList<>();
                for (String column : this.columnList) {

                    columnStrList.add("\"" + SqlUtil.getFieldName(column) + "\":?");
                }

                updateStrList.add("\"$set\":{" + StringUtils.join(columnStrList, ",") + "}");
            }

            if (CollectionUtils.isNotEmpty(this.incList)) {

                List<String> incStrList = new ArrayList<>();
                for (SyncPair<String, Integer> incParam : this.incList) {

                    incStrList.add("\"" + SqlUtil.getFieldName(incParam.getKey()) + "\":" + incParam.getValue());
                }
                updateStrList.add("\"$inc\":{" + StringUtils.join(incStrList, ",") + "}");
            }

            tempBuilder.append(StringUtils.join(updateStrList, ","));

            tempBuilder.append("},");
            tempBuilder.append("\"multi\":true,");
            tempBuilder.append("\"upsert\":false,");
            tempBuilder.append("\"q\":{\"$and\":[");
            tempBuilder.append(StringUtils.join(SqlUtil.makeMongoSql(this.inList, this.eqList, this.ltList), ","));
            tempBuilder.append("]}}");

            builder.append(StringUtils.repeat(tempBuilder.toString(), ",", this.groupCount)).append("]}");
        }

        String resultStr = builder.toString();

        log.info("构建SQL执行语句->" + resultStr);
        return resultStr;
    }
}
