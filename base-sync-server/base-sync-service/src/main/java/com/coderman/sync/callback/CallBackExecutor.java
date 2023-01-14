package com.coderman.sync.callback;

import com.alibaba.fastjson.JSONObject;
import com.coderman.service.config.PropertyConfig;
import com.coderman.sync.callback.meta.CallBackNode;
import com.coderman.sync.callback.meta.CallbackTask;
import com.coderman.sync.constant.PlanConstant;
import com.coderman.sync.constant.SyncConstant;
import com.coderman.sync.context.CallbackContext;
import com.coderman.sync.context.SyncContext;
import com.coderman.sync.executor.AbstractExecutor;
import com.coderman.sync.sql.SelectBuilder;
import com.coderman.sync.sql.UpdateBuilder;
import com.coderman.sync.sql.meta.SqlMeta;
import com.coderman.sync.task.SyncConvert;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Lazy(false)
@Slf4j
public class CallBackExecutor {

    private final ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
    private HttpClientBuilder httpClientBuilder = null;
    private RequestConfig requestConfig = null;
    private final Map<String, CloseableHttpClient> httpClientMap = new ConcurrentHashMap<>();

    private final Map<String, CallBackNode> callBackNodeMap = new ConcurrentHashMap<>();
    private final Map<String, AtomicInteger> unFailMap = new ConcurrentHashMap<>();


    @PostConstruct
    private void init() throws Exception {

        this.requestConfig = RequestConfig.custom()
                .setSocketTimeout(20000)
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000).build();

        SSLContextBuilder builder = new SSLContextBuilder();

        builder.loadTrustMaterial(null, new TrustStrategy() {
            @Override
            public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                return true;
            }
        });

        builder.setProtocol("TLSv1.2");

        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(builder.build(), new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        });

        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", new PlainConnectionSocketFactory()).register("https", socketFactory).build();

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        connectionManager.setMaxTotal(20);
        connectionManager.setDefaultMaxPerRoute(4);

        this.httpClientBuilder = HttpClients.custom();
        this.httpClientBuilder.setConnectionManager(connectionManager);
        this.httpClientBuilder.setSSLSocketFactory(socketFactory);
        this.httpClientBuilder.setMaxConnTotal(20);
        this.httpClientBuilder.disableAutomaticRetries();
        this.httpClientBuilder.evictIdleConnections(3, TimeUnit.SECONDS);

        // 初始化回调地址
        initCallbackUrl();

        // 节点可用性线程启动
        initCheckNodeThread();
    }

    private void initCallbackUrl() {

        Map<String, String> callbackMap = PropertyConfig.getDictMap("callback");

        for (String project : callbackMap.keySet()) {

            String[] hosts = callbackMap.get(project).split(",");

            CallBackNode callBackNode = new CallBackNode();

            for (String host : hosts) {

                String callbackUrl = host + "/" + project + "/callback/notify";

                boolean result = this.checkNodeAvailable(callbackUrl, this.getClient(callbackUrl));

                if (result) {

                    callBackNode.addCallbackUrl(callbackUrl);

                } else {

                    callBackNode.addNoneCallbackUrl(callbackUrl);
                }
            }

            callBackNode.resetAvailableNode();

            this.callBackNodeMap.put(project, callBackNode);
        }
    }

    private void initCheckNodeThread() {

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                while (true) {

                    log.info("线程可用性检测开始.......");

                    for (String key : callBackNodeMap.keySet()) {

                        CallBackNode callBackNode = callBackNodeMap.get(key);

                        if (CollectionUtils.isNotEmpty(callBackNode.getUnavailableList())) {

                            for (String callbackUrl : callBackNode.getUnavailableList()) {

                                boolean result = checkNodeAvailable(callbackUrl, getClient(callbackUrl));

                                if (result) {

                                    callBackNode.switchAvailableNode(callbackUrl);
                                }
                            }
                        }
                    }

                    try {

                        TimeUnit.SECONDS.sleep(120);

                    } catch (InterruptedException e) {

                        log.error("线程中断:{}", e.getMessage(), e);
                    }
                }
            }
        });

        thread.setName("节点可用性线程");
        thread.setDaemon(true);
        thread.start();

        thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {

                if ("节点可用性线程".equals(t.getName())) {

                    initCheckNodeThread();
                }
            }
        });

        log.info("节点可用性线程启动...");
    }


    /**
     * 回调节点检测可用性
     *
     * @param callbackUrl 回调地址
     * @param client      客户端
     * @return
     */
    private boolean checkNodeAvailable(String callbackUrl, CloseableHttpClient client) {

        boolean result = false;
        CloseableHttpResponse response = null;

        try {

            HttpPost post = new HttpPost(callbackUrl);

            post.setConfig(this.requestConfig);
            post.setEntity(new UrlEncodedFormEntity(
                    Collections.singletonList(new BasicNameValuePair("msg", "ping")), "UTF-8"
            ));

            response = client.execute(post);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

                result = true;
            }

        } catch (Exception e) {

            log.error("回调检测失败:{},callbackUrl:{}", e.getMessage(), callbackUrl);

        } finally {

            if (null != response) {

                try {
                    response.close();

                } catch (IOException e) {

                    log.error("关闭响应流失败:{}", e.getMessage(), e);
                }
            }

        }

        return result;
    }


    /**
     * 获取http客户端
     *
     * @param url 回调地址
     * @return
     */
    public CloseableHttpClient getClient(String url) {

        if (!this.httpClientMap.containsKey(url)) {

            this.httpClientMap.put(url, this.httpClientBuilder.build());
        }

        return this.httpClientMap.get(url);
    }

    /**
     * 加入回调任务队列
     *
     * @param callbackTask 回调任务
     */
    public void addTask(final CallbackTask callbackTask) {

        threadPoolExecutor.execute(() -> {

            try {

                boolean result = dealWithTask(callbackTask);

                if (!result) {
                    CallbackContext.getCallbackContext().addTaskToDelayQueue(callbackTask);
                }

            } catch (Throwable throwable) {


                log.error("回调失败", throwable);
            }
        });
    }

    /**
     * 处理回调任务
     *
     * @param callback 回调任务
     * @return
     * @throws Throwable
     */
    private boolean dealWithTask(CallbackTask callback) throws Throwable {

        // 标记回调开始
        if (!this.markCallbackStart(callback)) {

            return true;
        }

        // 开始回调
        boolean result = this.sendPost(callback);

        // 标记回调结果
        this.markCallbackEnd(callback, result);

        return result;
    }

    /**
     * 标记回调完成
     *
     * @param callback 回调任务
     * @param success  回调结果
     * @throws Throwable
     */
    public void markCallbackEnd(CallbackTask callback, boolean success) throws Throwable {

        AbstractExecutor executor = AbstractExecutor.build(callback.getDb());

        UpdateBuilder updateBuilder = UpdateBuilder.create(SyncContext.getContext().getDbType(callback.getDb()));
        updateBuilder.table("pub_callback").column("status").column("ack_time");
        updateBuilder.whereIn("uuid", 1);

        if (!callback.isFirst()) {

            updateBuilder.inc("repeat_count", 1);
        }

        List<Object> paramList = new ArrayList<>();

        if (success) {

            paramList.add(PlanConstant.CALLBACK_STATUS_SUCCESS);
        } else {
            paramList.add(PlanConstant.CALLBACK_STATUS_FAIL);
        }

        paramList.add(new Date());
        paramList.add(callback.getUuid());

        SqlMeta sqlMeta = new SqlMeta();
        sqlMeta.setSqlType(SyncConstant.OPERATE_TYPE_UPDATE);
        sqlMeta.setSql(updateBuilder.sql());
        sqlMeta.setParamList(SyncConvert.toArrayList(paramList));

        executor.sql(sqlMeta);

        executor.execute();

    }

    /**
     * 标记回调进行中
     *
     * @param callback 回调任务
     * @return
     * @throws Throwable
     */
    public boolean markCallbackStart(CallbackTask callback) throws Throwable {

        boolean success = false;

        AbstractExecutor executor = AbstractExecutor.build(callback.getDb());

        boolean isMongoDB = executor.getMongoTemplate() != null;

        Object callbackId = null;

        if (!isMongoDB) {

            SelectBuilder selectBuilder = SelectBuilder.create(SyncContext.getContext().getDbType(callback.getDb()));
            selectBuilder.table("pub_callback").column("callback_id");
            selectBuilder.whereEq("uuid");

            SqlMeta sqlMeta = new SqlMeta();
            sqlMeta.setSql(selectBuilder.sql());
            sqlMeta.setParamList(SyncConvert.toArrayList(Collections.singletonList(callback.getUuid())));
            sqlMeta.setSqlType(SyncConstant.OPERATE_TYPE_SELECT);

            executor.sql(sqlMeta);

            List<SqlMeta> sqlMetaList = executor.execute();

            if (sqlMetaList != null && sqlMetaList.size() == 1 && sqlMetaList.get(0).getResultList() != null && sqlMetaList.get(0).getResultList().size() > 0) {

                callbackId = sqlMetaList.get(0).getResultList().get(0).get("callback_id");

            } else {

                return false;
            }

            executor.clear();

        }

        UpdateBuilder updateBuilder = UpdateBuilder.create(SyncContext.getContext().getDbType(callback.getDb()));
        updateBuilder.table("pub_callback").column("status").column("send_time");

        List<Object> paramList = new ArrayList<>();
        paramList.add(PlanConstant.CALLBACK_STATUS_ING);
        paramList.add(new Date());

        if (!isMongoDB) {

            updateBuilder.whereIn("callback_id", 1).whereIn("status", 1);
            paramList.add(callbackId);
        } else {
            updateBuilder.whereIn("uuid", 1).whereIn("status", 1);
            paramList.add(callback.getUuid());

        }

        if (callback.isFirst()) {

            paramList.add(PlanConstant.CALLBACK_STATUS_WAIT);
        } else {

            paramList.add(PlanConstant.CALLBACK_STATUS_FAIL);
        }

        SqlMeta sqlMeta = new SqlMeta();
        sqlMeta.setSql(updateBuilder.sql());
        sqlMeta.setParamList(SyncConvert.toArrayList(paramList));
        sqlMeta.setSqlType(SyncConstant.OPERATE_TYPE_UPDATE);

        executor.sql(sqlMeta);

        List<SqlMeta> metaList = executor.execute();

        if (metaList.size() == 1 && metaList.get(0).getAffectNum() == 1) {

            success = true;
        }

        return success;

    }

    /**
     * 发送回调请求
     *
     * @param callback 回调任务
     * @return
     */
    private boolean sendPost(CallbackTask callback) {

        boolean result = false;

        CloseableHttpClient client;
        CloseableHttpResponse response = null;
        String callbackUrl = null;

        try {

            CallBackNode callBackNode = this.callBackNodeMap.get(callback.getProject());

            if (callBackNode == null) {

                throw new RuntimeException(callback.getProject() + " 没有配置回调主机地址");
            }

            callbackUrl = callBackNode.getCallbackUrl();

            if (StringUtils.isBlank(callbackUrl)) {

                throw new RuntimeException(callback.getProject() + " 没有可用的回调主机地址");
            }

            client = this.getClient(callbackUrl);

            // 回调接口调用
            HttpPost post = new HttpPost(callbackUrl);

            post.setConfig(this.requestConfig);
            post.setEntity(new UrlEncodedFormEntity(Collections.singletonList(new BasicNameValuePair("msg", callback.getMsg())), "UTF-8"));

            response = client.execute(post);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

                String resultStr = EntityUtils.toString(response.getEntity());

                if (StringUtils.isNotBlank(resultStr)) {

                    JSONObject jsonObject = JSONObject.parseObject(resultStr);

                    if (jsonObject != null && jsonObject.containsKey("code") && HttpStatus.SC_OK == jsonObject.getInteger("code")) {

                        result = true;
                    }
                }

            }

        } catch (Exception e) {

            if (e instanceof HttpHostConnectException) {

                log.error("回调失败,无法连接主机");
            }

            if(e instanceof ConnectTimeoutException){

                log.error("回调失败,网络超时");
            }

            log.error("回调失败异常,e:{}",e.getMessage(),e);

        } finally {


            if (null != response) {

                try {

                    response.close();

                } catch (IOException e) {

                    log.error("关闭响应流失败:{}", e.getMessage(), e);
                }
            }

        }

        return result;
    }
}
