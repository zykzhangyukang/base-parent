package com.coderman.sync.sql;

import com.coderman.sync.constant.SyncConstant;
import com.coderman.sync.pair.SyncPair;
import com.coderman.sync.util.SqlUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

@Slf4j
@Data
public class SelectBuilder {

    private String dbType;

    private String table;

    private List<String> columnList;

    private List<String> eqList;

    private List<SyncPair<String, Integer>> inList;

    private List<String> ltList;

    private List<String> gtList;


    public SelectBuilder(String dbType) {

        this.dbType = dbType;
    }


    public static SelectBuilder create(String dbType) {

        return new SelectBuilder(dbType);
    }

    public SelectBuilder table(String table) {

        this.table = table;
        return this;
    }


    public SelectBuilder whereEq(String where) {


        if (null == this.eqList) {

            this.eqList = new ArrayList<>();
        }

        this.eqList.add(where);

        return this;
    }

    public SelectBuilder whereIn(String where, int paramNum) {


        if (paramNum == 1) {

            return this.whereEq(where);
        }

        if (null == this.inList) {

            this.inList = new ArrayList<>();
        }

        this.inList.add(new SyncPair<>(where, paramNum));

        return this;
    }


    public SelectBuilder whereLt(String where) {


        if (null == this.ltList) {

            this.ltList = new ArrayList<>();
        }

        this.ltList.add(where);

        return this;
    }


    public SelectBuilder whereGt(String where) {


        if (null == this.gtList) {

            this.gtList = new ArrayList<>();
        }

        this.gtList.add(where);

        return this;
    }

    public SelectBuilder column(String column) {

        if (null == this.columnList) {

            this.columnList = new ArrayList<>();
        }

        if (!this.columnList.contains(column)) {

            this.columnList.add(column);
        }

        return this;
    }


    public String sql() {


        if (CollectionUtils.isEmpty(this.columnList) || (CollectionUtils.isEmpty(this.inList) && CollectionUtils.isEmpty(this.eqList)) || StringUtils.isBlank(this.table)) {

            return StringUtils.EMPTY;
        }

        StringBuilder builder = new StringBuilder();

        if (Objects.equals(SyncConstant.DB_TYPE_MYSQL, this.dbType)) {

            builder.append("select ");
            builder.append(StringUtils.join(this.columnList, ", "));
            builder.append(" from ");
            builder.append(this.table);
            builder.append(" where ");

            List<String> whereStrList = new ArrayList<>();

            if (CollectionUtils.isNotEmpty(this.inList)) {


                for (SyncPair<String, Integer> param : inList) {

                    String repeatStr = StringUtils.repeat("?", ",", param.getValue());
                    whereStrList.add(param.getKey() + " in (" + repeatStr + ")");
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


            if (CollectionUtils.isNotEmpty(this.gtList)) {

                for (String param : this.gtList) {

                    whereStrList.add(param + " > ?");
                }
            }


            builder.append(StringUtils.join(whereStrList, " and "));
            builder.append(";");
        } else if (SyncConstant.DB_TYPE_MONGO.equalsIgnoreCase(this.dbType)) {

            builder.append("{");
            builder.append("\"find\": \"").append(SqlUtil.getCollectionName(this.table)).append("\",");
            builder.append("\"filter\":{\"$and\":[");

            List<String> whereStrList = new ArrayList<>();

            if (CollectionUtils.isNotEmpty(this.inList)) {

                for (SyncPair<String, Integer> param : this.inList) {

                    String replaceStr = StringUtils.repeat("?", ",", param.getValue());
                    whereStrList.add("{\"" + SqlUtil.getFieldName(param.getKey()) + "\":{\"$in\":[" + replaceStr + "]}}");
                }
            }

            if (CollectionUtils.isNotEmpty(this.eqList)) {

                for (String param : this.eqList) {

                    whereStrList.add("{\"" + SqlUtil.getFieldName(param) + "\":?}");
                }
            }

            if (CollectionUtils.isNotEmpty(this.ltList)) {

                for (String param : this.ltList) {

                    whereStrList.add("{\"" + SqlUtil.getFieldName(param) + "\":{\"$lt\":?}}");
                }
            }

            if (CollectionUtils.isNotEmpty(this.gtList)) {

                for (String param : this.gtList) {

                    whereStrList.add("{\"" + SqlUtil.getFieldName(param) + "\":{\"$gt\":?}}");
                }
            }

            builder.append(StringUtils.join(whereStrList, ","));
            builder.append("]},");

            builder.append("\"projection\":{");

            List<String> columnStrList = new ArrayList<>();

            for (String column : this.columnList) {

                if ("_id".equals(column)) {

                    continue;
                }

                columnStrList.add("\"" + SqlUtil.getFieldName(column) + "\":1");
            }

            if (this.columnList.contains("_id")) {

                columnStrList.add("\"_id\":1");
            } else {
                columnStrList.add("\"_id\":0");
            }

            builder.append(StringUtils.join(columnStrList, ","));

            builder.append("}}");
        }


        String resultStr = builder.toString();


        log.debug("构建SQL执行语句->" + resultStr);
        return resultStr;
    }


}
