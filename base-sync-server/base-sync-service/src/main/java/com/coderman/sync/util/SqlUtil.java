package com.coderman.sync.util;

import com.coderman.sync.pair.SyncPair;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SqlUtil {


    public static String getFieldName(String field) {


        if("sync_time".equals(field)){

            return "syncTime";
        }


        if("sync_uuid".equals(field)){

            return "syncUUid";
        }




        if("src_project".equals(field)){

            return "srcProject";
        }



        if("dest_project".equals(field)){

            return "destProject";
        }


        if("db_name".equals(field)){

            return "dbName";
        }


        if("msg_content".equals(field)){

            return "msgContent";
        }


        if("create_time".equals(field)){

            return "createTime";
        }


        if("send_time".equals(field)){

            return "sendTime";
        }

        if("ack_time".equals(field)){

            return "ackTime";
        }

        if("repeat_count".equals(field)){

            return "repeatCount";
        }

        if("send_status".equals(field)){

            return "sendStatus";
        }

        if("deal_count".equals(field)){

            return "dealCount";
        }

        if("deal_status".equals(field)){

            return "dealStatus";
        }

        return field;
    }

    public static String getCollectionName(String table) {

        if ("pub_callback".equals(table)) {

            return "callbackModel";
        } else if ("pub_mq_message".equals(table)) {

            return "mqMessageModel";
        }

        return table;
    }

    public static List<String> makeMongoSql(List<SyncPair<String, Integer>> inList, List<String> eqList, List<String> ltList) {

        List<String> whereStrList = new ArrayList<>();


        if(CollectionUtils.isNotEmpty(inList)){


            for (SyncPair<String, Integer> param : inList) {

                String replaceStr = StringUtils.repeat("?",",",param.getValue());
                whereStrList.add("{\""+SqlUtil.getFieldName(param.getKey())+"\":{\"$in\":["+replaceStr+"]}}");
            }

        }

        if(CollectionUtils.isNotEmpty(eqList)){

            for (String param : eqList) {

                whereStrList.add("{\""+SqlUtil.getFieldName(param)+"\":?}");
            }
        }



        if(CollectionUtils.isNotEmpty(ltList)){

            for (String param : ltList) {

                whereStrList.add("{\""+SqlUtil.getFieldName(param)+"\":{\"$lt\":?}}");
            }
        }

        return whereStrList;
    }
}
