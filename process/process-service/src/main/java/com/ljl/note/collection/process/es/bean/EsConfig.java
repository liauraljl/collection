package com.ljl.note.collection.process.es.bean;

import com.ljl.note.collection.process.common.MsCloudPlugBaseCode;
import com.ljl.note.collection.process.execption.MsCloudPlugException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @description: EsConfig
 */
@Data
@Slf4j
@Component
public class EsConfig {

    @Value("${es.useEs:false}")
    private boolean useEs;

    @Value("${es.socketAddressStrings:127.0.0.1:9200}")
    private String socketAddressStrings;

    @Value("${es.scheme:http}")
    private String scheme;

    @Value("${es.connectTimeOut:1000}")
    private Integer connectTimeOut;

    @Value("${es.socketTimeOut:30000}")
    private Integer socketTimeOut;

    @Value("${es.connectionRequestTimeOut:500}")
    private Integer connectionRequestTimeOut;

    @Value("${es.maxConnTotal:50}")
    private Integer maxConnTotal;

    @Value("${es.maxConnPerRoute:10}")
    private Integer maxConnPerRoute;

    private RestClientBuilder mutiConnectConfig;

    public EsClient esClient() {
        EsClient esClient = new EsClient();
        if (useEs) {
            initClient(esClient);
        }
        return esClient;
    }

    /**
     * 初始化ES 连接 client
     *
     * @param esClient
     */
    private void initClient(EsClient esClient) {
        /*if (null == config) {
            log.error("no es config : es.properties");
            throw new MsCloudPlugException(MsCloudPlugBaseCode.ES_CONFIG_NULL);
        }*/
        String[] socketAddress = socketAddressStrings.split(",");
        if (null == socketAddress || socketAddress.length == 0) {
            log.error("es client init fail : socketAddress is null");
            throw new MsCloudPlugException(MsCloudPlugBaseCode.ES_ADDRESS_ERROR);
        }
        HttpHost[] httpHosts = new HttpHost[socketAddress.length];
        for (int i = 0; i < socketAddress.length; i++) {
            try {
                String[] socketAddressSplit = StringUtils.split(socketAddress[i], ":");
                httpHosts[i] = new HttpHost(socketAddressSplit[0], Integer.parseInt(socketAddressSplit[1]), scheme);
            } catch (Exception e) {
                log.error("es client init fail ,addresses is error:{}", e.getMessage());
                throw new MsCloudPlugException(MsCloudPlugBaseCode.ES_ADDRESS_ERROR);
            }
        }
        RestClientBuilder builder = RestClient.builder(httpHosts);
        // 异步httpclient连接延时配置
        setConnectTimeOutConfig(builder);
        //使用异步httpclient时设置并发连接数
        setMutiConnectConfig(builder);
        esClient.setClient(new RestHighLevelClient(builder));
        if (null == esClient.getClient()) {
            log.error("es client init fail : client is null");
            throw new MsCloudPlugException(MsCloudPlugBaseCode.ES_CLIENT_NULL);
        }
        log.info("es client init success ,client is :{}", esClient.getClient());
    }

    /**
     * 设置异步httpclient连接延时配置
     * @param builder
     */
    public void setConnectTimeOutConfig(RestClientBuilder builder) {
        builder.setRequestConfigCallback(requestConfigBuilder -> {
            requestConfigBuilder.setConnectTimeout(connectTimeOut);
            requestConfigBuilder.setSocketTimeout(socketTimeOut);
            requestConfigBuilder.setConnectionRequestTimeout(connectionRequestTimeOut);
            return requestConfigBuilder;
        });
    }

    /**
     * 使用异步httpclient时设置并发连接数
     * @param builder
     */
    public void setMutiConnectConfig(RestClientBuilder builder) {
        builder.setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.setMaxConnTotal(maxConnTotal);
            httpClientBuilder.setMaxConnPerRoute(maxConnPerRoute);
            return httpClientBuilder;
        });
    }
}
