package com.coderman.sync.sql;

import com.coderman.sync.constant.SyncConstant;
import com.coderman.sync.pair.SyncPair;
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


    @SuppressWarnings("all")
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
        }


        String resultStr = builder.toString();


        log.debug("构建SQL执行语句->" + resultStr);
        return resultStr;
    }


}
