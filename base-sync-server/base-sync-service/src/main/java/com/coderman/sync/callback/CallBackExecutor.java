package com.coderman.sync.callback;

import com.alibaba.fastjson.JSONObject;
import com.coderman.service.config.PropertyConfig;
import com.coderman.sync.callback.meta.CallBackNode;
import com.coderman.sync.callback.meta.CallbackTask;
import com.coderman.sync.context.CallbackContext;
import com.coderman.sync.exception.SyncException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
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
        Map<String, String> callbackMap = PropertyConfig.getDictMap("callback");

        for (String project : callbackMap.keySet()) {

            String[] hosts = callbackMap.get(project).split(",");

            CallBackNode callBackNode = new CallBackNode();


            for (String host : hosts) {

                callBackNode.getAvailableList().add(host);
            }

            this.callBackNodeMap.put(project, callBackNode);
        }

    }


    public CloseableHttpClient getClient(String url) {

        if (!this.httpClientMap.containsKey(url)) {

            this.httpClientMap.put(url, this.httpClientBuilder.build());
        }

        return this.httpClientMap.get(url);
    }

    public void addTask(final CallbackTask callbackTask) {

        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {

                try {

                    boolean result = dealWithTask(callbackTask);

                    if (!result) {

                        CallbackContext.getCallbackContext().addTaskToDelayQueue(callbackTask);
                    }


                } catch (Throwable throwable) {


                    log.error("回调失败", throwable);
                }
            }
        });
    }

    private boolean dealWithTask(CallbackTask callback) {

        // 标记回调进行中

        // 标记回调完成

        return this.sendPost(callback);
    }

    private boolean sendPost(CallbackTask callback) {

        boolean result = false;

        CloseableHttpClient client;
        CloseableHttpResponse response = null;

        try {

            CallBackNode callBackNode = this.callBackNodeMap.get(callback.getProject());

            if (callBackNode == null) {
                throw new RuntimeException(callback.getProject() + " 没有配置回调主机地址");
            }

            String callbackUrl = callBackNode.getCallbackUrl();
            client = this.getClient(callbackUrl + callback.getProject());

            // 回调接口调用
            HttpPost post = new HttpPost(callbackUrl + "/" + callback.getProject() + "/callback/notify");

            post.setConfig(this.requestConfig);
            post.setEntity(new UrlEncodedFormEntity(
                    Collections.singletonList(new BasicNameValuePair("msg", callback.getMsg())), "UTF-8"
            ));

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

            log.error("回调失败:{}", e.getMessage(), e);

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
