package com.coderman.log.alarm;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author coderman
 */
public class DingDingUtil {

    private static final Logger log  = LoggerFactory.getLogger(DingDingUtil.class);

    public static void sendTextMessage(String accessToken, String secret, String msg) {
        try {
            Message message = new Message();
            message.setMsgtype("text");
            message.setText(new MessageInfo(msg));
            Long timestamp = System.currentTimeMillis();
            String sign = getSign(secret, timestamp);
            String url = "https://oapi.dingtalk.com/robot/send?access_token=" + accessToken
                    + "&timestamp="+timestamp
                    + "&sign="+sign;
            post(url, JsonUtils.toJson(message));
        } catch (Exception e) {
            log.error("ding ding sendTextMessage error:{}",e.getMessage(),e);
        }
    }


    public static void post(String url, String jsonBody) {
        HttpURLConnection conn = null;
        try {
            URL httpUrl = new URL(url);
            conn = (HttpURLConnection) httpUrl.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Type", "application/Json; charset=UTF-8");
            conn.connect();
            OutputStream out = conn.getOutputStream();
            byte[] data = jsonBody.getBytes();
            out.write(data);
            out.flush();
            out.close();
            InputStream in = conn.getInputStream();
            byte[] dataArr = new byte[in.available()];
            int read = in.read(dataArr);

            log.info("ding ding msg:{},read:{}",new String(dataArr),read);

        } catch (Exception e) {

            log.info("ding ding send error:{}",e.getMessage());

        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private static String getSign(String secret, Long timestamp) {
        try {
            String stringToSign = timestamp + "\n" + secret;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
            return URLEncoder.encode(new String(Base64.getEncoder().encode(signData)),"UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("钉钉发送消息签名失败");
        }
    }


    static
    class Message {
        private String msgtype;
        private MessageInfo text;

        public String getMsgtype() {
            return msgtype;
        }

        public void setMsgtype(String msgtype) {
            this.msgtype = msgtype;
        }

        public MessageInfo getText() {
            return text;
        }

        public void setText(MessageInfo text) {
            this.text = text;
        }
    }
    static
    class MessageInfo {
        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public MessageInfo(String content) {
            this.content = content;
        }

        public MessageInfo() {
        }
    }


}
