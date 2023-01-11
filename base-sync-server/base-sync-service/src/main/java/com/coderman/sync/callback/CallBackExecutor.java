package com.coderman.sync.callback;

import com.coderman.sync.callback.meta.CallbackTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Component
@Lazy(false)
@Slf4j
public class CallBackExecutor {

    private final PoolingHttpClientConnectionManager connectionManager = null;

    private final Map<String, CloseableHttpClient> clientMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init(){


    }


    private final ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);


    public void addTask(final CallbackTask callbackTask) {

        threadPoolExecutor.execute(() -> {

            try {



                System.out.println("执行同步回调");

            }catch (Throwable throwable){


                log.error("回调失败",throwable);
            }
        });
    }
}
