package com.coderman.sync.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.coderman.api.exception.BusinessException;
import com.coderman.service.util.SpringContextUtil;
import com.coderman.service.util.UUIDUtils;
import com.coderman.sync.constant.Constant;
import com.coderman.sync.producer.RocketMQOrderProducer;
import com.coderman.sync.producer.RocketMQProducer;
import com.coderman.sync.vo.MsgBody;
import com.coderman.sync.vo.PlanMsg;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;


public class SyncUtil {


    private final static Logger logger = LoggerFactory.getLogger(SyncUtil.class);


    private final static Queue<MsgBody> taskQueue = new ConcurrentLinkedDeque<MsgBody>();


    private final static Integer QUEUE_SIZE_ALERT_NUM = 2000;


    private final static ThreadLocal<List<MsgBody>> LOCAL_MAP = new ThreadLocal<>();


    private final static ThreadLocal<SimpleDateFormat> SIMPLE_DATE_FORMAT_THREAD_LOCAL = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));


    private final static String INIT_SQL = "select mq_message_id,uuid,msg_content from pub_mq_message where send_status = '" + Constant.MSG_SEND_STATUS_WAIT + "';";
    private final static String INSERT_SQL = "insert into pub_mq_message(uuid,msg_content,src_project,dest_project,create_time,send_status,deal_status,deal_count) values(?,?,?,?,?,?,?,?);";
    private final static String UPDATE_SENDING_BY_PK_SQL = "update pub_mq_message set deal_count = 0,send_status='" + Constant.MSG_SEND_STATUS_SENDING + "'where send_status='" + Constant.MSG_SEND_STATUS_WAIT + "'and mq_message_id=?;";
    private final static String UPDATE_SENDING_SQL = "update pub_mq_message set deal_count = 0,send_status='" + Constant.MSG_SEND_STATUS_SENDING + "'where send_status='" + Constant.MSG_SEND_STATUS_WAIT + "'and uuid=?;";
    private final static String UPDATE_FINAL_BY_PK_SQL = "update pub_mq_message set send_status = ?,mid=?,send_time = ? where send_status='" + Constant.MSG_SEND_STATUS_SENDING + "' and mq_message_id = ?";
    private final static String UPDATE_FINAL_SQL = "update pub_mq_message set send_status = ?,mid=?,send_time = ? where send_status='" + Constant.MSG_SEND_STATUS_SENDING + "' and uuid = ?";


    private static JdbcTemplate jdbcTemplate;

    private static RocketMQProducer rocketMQProducer;

    private static RocketMQOrderProducer rocketMQOrderProducer;

    static {

        try {


            jdbcTemplate = SpringContextUtil.getBean(JdbcTemplate.class);
            rocketMQProducer = SpringContextUtil.getBean(RocketMQProducer.class);
            rocketMQOrderProducer = SpringContextUtil.getBean(RocketMQOrderProducer.class);


            // 初始化队列
            List<Map<String, Object>> resultList = jdbcTemplate.queryForList(INIT_SQL);

            if (CollectionUtils.isNotEmpty(resultList)) {


                for (Map<String, Object> resultMap : resultList) {

                    String content = resultMap.get("msg_content").toString();
                    String uuid = resultMap.get("uuid").toString();
                    Integer mqMessageId = resultMap.get("mq_message_id") == null ? null : (Integer) resultMap.get("mq_message_id");


                    taskQueue.add(new MsgBody(uuid, content, SyncUtil.create(content).getPlanCode(), mqMessageId));
                }

            }

        } catch (Exception e) {

            logger.error("初始化同步系统队列失败", e);
        }


        // 处理队列任务
        dealTask();

    }


    /**
     * 从队列里消费消息
     */
    private static void dealTask() {

        Thread taskThread = new Thread(() -> {


            while (true) {

                try {

                    if (taskQueue.isEmpty()) {

                        TimeUnit.SECONDS.sleep(1);

                        logger.debug("消息发送线程运行中...");
                        continue;
                    }


                    // 发送消息
                    SyncUtil.sendMsgBySql(taskQueue.poll());


                } catch (InterruptedException e) {

                    logger.error("MQ消息发送队列异常", e);
                }
            }


        });


        // 异常处理
        taskThread.setDaemon(true);
        taskThread.setName("MQ消息发送线程");
        taskThread.setUncaughtExceptionHandler((t, e) -> {

            logger.error("线程异常退出,name->{}", t.getName(), e);

            if ("MQ消息发送线程".equalsIgnoreCase(t.getName())) {

                dealTask();
            }

        });


        taskThread.start();

    }


    /**
     * 发送消息
     */
    public static void submit() {

        if (null != LOCAL_MAP.get()) {


            if (taskQueue.size() > QUEUE_SIZE_ALERT_NUM) {

                logger.error("同步系统队列长度警报,MQ消息队列已经超过" + QUEUE_SIZE_ALERT_NUM + ",请及时处理");
            }


            for (MsgBody item : LOCAL_MAP.get()) {


                logger.info("消息队列,将消息放入发送线程队列,uuid:{}", item.getMsgId());
                taskQueue.add(item);
            }

        }

        LOCAL_MAP.remove();
    }

    /**
     * 同步消息: 插入消息到数据库,并且把消息体放到ThreadLocal中
     *
     * @param planMsg 计划消息
     */
    public static void sync(final PlanMsg planMsg) {


        if (null == LOCAL_MAP.get()) {

            LOCAL_MAP.set(new ArrayList<>());
        }


        final String msgId = UUIDUtils.getPrimaryValue();
        final String createTime = formatTime(new Date());
        final String msg = createMsg(planMsg, createTime, msgId);

        Integer mqMessageId = null;

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        int insertRows = jdbcTemplate.update(connection -> {


            PreparedStatement ps = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, msgId);
            ps.setString(2, msg);
            ps.setString(3, planMsg.getSrcProject());
            ps.setString(4, planMsg.getDescProject());
            ps.setString(5, createTime);
            ps.setString(6, Constant.MSG_SEND_STATUS_WAIT);
            ps.setString(7, Constant.MSG_DEAL_STATUS_WAIT);
            ps.setString(8, Integer.toString(0));

            return ps;
        }, keyHolder);


        if (insertRows > 0) {

            mqMessageId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        }


        LOCAL_MAP.get().add(new MsgBody(msgId, msg, planMsg.getPlanCode(), mqMessageId));
    }


    /**
     * 时间格式化
     *
     * @param date 当前时间
     * @return
     */
    public static String formatTime(Date date) {
        return SIMPLE_DATE_FORMAT_THREAD_LOCAL.get().format(date);
    }


    /**
     * 将计划信息转换为对象
     *
     * @param msg 消息
     * @return
     */
    private static PlanMsg create(String msg) {

        JSONObject jsonObject = (JSONObject) JSONObject.parse(msg);


        String planCode = jsonObject.getString("plan");
        MsgBuilder msgBuilder = MsgBuilder.create(planCode, ProjectEnum.class.getEnumConstants()[0], ProjectEnum.class.getEnumConstants()[0]);
        JSONArray jsonArray = jsonObject.getJSONArray("tables");

        for (int i = 0; i < jsonArray.size(); i++) {


            JSONObject json = jsonArray.getJSONObject(i);


            if (json.get("unique") instanceof JSONArray) {


                for (int j = 0; j < ((JSONArray) json.get("unique")).size(); j++) {

                    msgBuilder.add(json.getString("code"), ((JSONArray) json.get("unique")).getString(j));
                }

            } else {

                msgBuilder.add(json.getString("code"), json.getString("unique"));
            }

        }

        String orderlyMsgKey = jsonObject.getString("orderlyMsgKey");

        PlanMsg planMsg = msgBuilder.build();
        planMsg.setSrcProject(StringUtils.EMPTY);
        planMsg.setDescProject(StringUtils.EMPTY);
        planMsg.setOrderlyMsgKey(orderlyMsgKey);
        return planMsg;
    }


    /**
     * 发送到mq
     *
     * @param msgBody 消息体内容
     */
    private static void sendMsgBySql(MsgBody msgBody) {

        String resultStr = Constant.MSG_SEND_STATUS_SUCCESS;
        String mid = StringUtils.EMPTY;

        try {

            int resultNum = -1;

            // 先把消息的发送状态改为发送中
            if (msgBody.getMqMessageId() != null) {

                resultNum = jdbcTemplate.update(UPDATE_SENDING_BY_PK_SQL, msgBody.getMqMessageId());

            } else {

                resultNum = jdbcTemplate.update(UPDATE_SENDING_SQL, msgBody.getMsgId());
            }

            if (resultNum == 0) {

                return;
            }

            SendResult sendResult = null;

            String orderlyMsgKey = create(msgBody.getMsg()).getOrderlyMsgKey();

            // 发送到队列,如果返回的结果不为空,则认为发送的消息已经到了队列中,将发送状态改为成功
            if (rocketMQOrderProducer != null && orderlyMsgKey != null) {

                Message message = new Message(rocketMQOrderProducer.getSyncOrderTopic(), StringUtils.EMPTY, msgBody.getPlanCode(), msgBody.getMsg().getBytes(StandardCharsets.UTF_8));
                sendResult = rocketMQOrderProducer.send(message, orderlyMsgKey);

            } else {

                Message message = new Message(rocketMQProducer.getSyncTopic(), StringUtils.EMPTY, msgBody.getPlanCode(), msgBody.getMsg().getBytes(StandardCharsets.UTF_8));
                sendResult = rocketMQProducer.send(message);
            }


            if (null != sendResult) {

                mid = sendResult.getMsgId();
            }


        } catch (Exception e) {

            resultStr = Constant.MSG_SEND_STATUS_FAIL;

            logger.error("向同步系统发送mq消息失败," + msgBody.getMsg(), e);

        } finally {

            // 更新发送状态
            if (msgBody.getMqMessageId() != null) {

                jdbcTemplate.update(UPDATE_FINAL_BY_PK_SQL, resultStr, mid, formatTime(new Date()), msgBody.getMqMessageId());
            } else {

                jdbcTemplate.update(UPDATE_FINAL_SQL, resultStr, mid, formatTime(new Date()), msgBody.getMsgId());
            }

        }
    }


    /**
     * 构建消息体内容
     *
     * @param planMsg    同步计划
     * @param createTime 创建时间
     * @param msgId      消息id
     * @return
     */
    public static String createMsg(PlanMsg planMsg, String createTime, String msgId) {


        if (CollectionUtils.isEmpty(planMsg.getMsgItemList())) {

            throw new BusinessException("同步计划内容不能为空:" + planMsg.getPlanCode());
        }

        StringBuilder builder = new StringBuilder();

        builder.append("{\"plan\":\"");
        builder.append(planMsg.getPlanCode());
        builder.append("\",\"tables\":[");

        for (int i = 0; i < planMsg.getMsgItemList().size(); i++) {


            com.coderman.sync.vo.MsgItem msgItem = planMsg.getMsgItemList().get(i);

            builder.append("{\"code\":\"");
            builder.append(msgItem.getCode()).append("\",\"unique\":[");


            for (int j = 0; j < msgItem.getUnique().size(); j++) {


                builder.append("\"").append(msgItem.getUnique().get(j)).append("\"");

                if (j != msgItem.getUnique().size() - 1) {

                    builder.append(",");
                }

            }

            builder.append("]");


            if (msgItem.getMustAffectRows() != null) {

                builder.append(",\"affectNum\":\"").append(msgItem.getMustAffectRows()).append("\"");
            }

            builder.append("}");


            if (i != planMsg.getMsgItemList().size() - 1) {

                builder.append(",");
            }

        }


        builder.append("],");

        if(StringUtils.isNotBlank(planMsg.getOrderlyMsgKey())){

            builder.append("\"orderlyMsgKey\":\"").append(planMsg.getOrderlyMsgKey()).append("\",");
        }

        builder.append("\"createTime\":\"").append(createTime).append("\",");
        builder.append("\"src\":\"").append(System.getProperty("domain")).append("\",");
        builder.append("\"msgId\":\"").append(msgId).append("\"}");

        return builder.toString();
    }


    /**
     * 清空消息
     */
    public static void clear() {

        if (logger.isDebugEnabled()) {


            StringBuilder sb = new StringBuilder();

            if (null != LOCAL_MAP.get()) {


                for (MsgBody item : LOCAL_MAP.get()) {
                    sb.append(item.getMsgId()).append(",");
                }

            }

            StringBuilder stringBuilder = new StringBuilder();

            StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
            for (StackTraceElement stackTraceElement : stackTraceElements) {

                String content = stackTraceElement.toString();
                stringBuilder.append(content).append("\r\n\t");
            }

            logger.debug("消息队列,清除消息,uuid:{}", sb.toString() + "\r\n\t" + stringBuilder.toString());
        }

        LOCAL_MAP.remove();
    }
}
