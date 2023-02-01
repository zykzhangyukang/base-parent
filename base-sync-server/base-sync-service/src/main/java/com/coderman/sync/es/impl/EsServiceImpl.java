package com.coderman.sync.es.impl;

import com.coderman.sync.es.EsService;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;

@Service
public class EsServiceImpl implements EsService {

    @Resource
    private RestHighLevelClient restHighLevelClient;

    @PostConstruct
    public void init() throws IOException {



    }
}
