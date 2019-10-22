package com.ljl.note.collection.process.es.bean;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.Serializable;

/**
 * @description: EsClient
 */
@Slf4j
@Data
public class EsClient implements Serializable,AutoCloseable{
    private static final long serialVersionUID = -5625129072392862470L;

    private RestHighLevelClient client;

    @Override
    public void close() throws Exception {
        if (null != client){
            this.client.close();
        }
    }
}
