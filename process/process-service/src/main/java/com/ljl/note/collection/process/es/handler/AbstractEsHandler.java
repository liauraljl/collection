package com.ljl.note.collection.process.es.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.ljl.note.collection.process.common.MsCloudPlugBaseCode;
import com.ljl.note.collection.process.es.bean.EsConfig;
import com.ljl.note.collection.process.es.bean.EsModel;
import com.ljl.note.collection.process.execption.MsCloudPlugException;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.*;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: AbstractEsHandler
 * @date: 2019/10/16
 */
@Slf4j
@SuppressWarnings("unchecked")
public abstract class AbstractEsHandler<T extends EsModel> implements EsHandler<T> {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private EsConfig esConfig;

    private RestHighLevelClient getClient() {
        return esConfig.esClient().getClient();
    }

    /**
     * 获取泛型的真实class
     */
    private Class<T> getClassType() {
        Type superClass = getClass().getGenericSuperclass();
        return (Class<T>) ((ParameterizedType) superClass).getActualTypeArguments()[0];
    }

    /**
     * 索引名称
     *
     * @return
     */
    public abstract String indexName();

    /**
     * 校验索引
     *
     * @param index
     */
    private void checkArg(String index) {
        Preconditions.checkNotNull(index, "Args index [{}] cannot be null", index);
    }

    /**
     * 创建索引
     */
    @Override
    public void createIndex(String indexMapping) {
        checkArg(indexName());
        CreateIndexRequest request = new CreateIndexRequest(indexName());
        request.settings(Settings.builder().put("index.number_of_shards", 3).put("index.number_of_replicas", 2));
        //创建索引时创建文档类型映射
        request.mapping(indexMapping,XContentType.JSON);
        try {
            CreateIndexResponse res = getClient().indices().create(request, RequestOptions.DEFAULT);
            if (!res.isAcknowledged()) {
                log.error("createIndex [{}] fail data :{}", indexName(), indexMapping);
                throw new MsCloudPlugException(MsCloudPlugBaseCode.ES_CREATE_INDEX_ERROR);
            }
        } catch (Exception e) {
            log.error("createIndex [{}] fail data :{},error:{}", indexName(), indexMapping, e.getMessage(), e);
            throw new MsCloudPlugException(MsCloudPlugBaseCode.ES_CREATE_INDEX_ERROR);
        }
    }

    /**
     * 增加修改数据
     *
     * @param esModel
     */
    @Override
    public boolean update(T esModel) {
        checkArg(indexName());
        IndexRequest request = new IndexRequest(indexName());
        request.id(esModel.getId());
        request.source(JSON.toJSONString(esModel), XContentType.JSON);
        try {
            getClient().index(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("update [{}] esModel fail data :{},error:{}", indexName(), JSON.toJSONString(esModel), e.getMessage(), e);
            throw new MsCloudPlugException(MsCloudPlugBaseCode.ES_UPSET_MODEL_ERROR);
        }
        return true;
    }

    /**
     * 根据ID删除数据
     *
     * @param id
     */
    @Override
    public boolean deleteOne(String id) {
        checkArg(indexName());
        try {
            getClient().delete(new DeleteRequest(indexName(),id),RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("deleteOne [{}] esModel fail id :{},error:{}", indexName(), id, e.getMessage(), e);
            throw new MsCloudPlugException(MsCloudPlugBaseCode.ES_DELETE_MODEL_ERROR);
        }
        return true;
    }

    /**
     * 根据IDS批量删除数据
     *
     * @param ids
     * @return
     */
    @Override
    public boolean deleteByIds(List<String> ids) {
        checkArg(indexName());
        BulkRequest request = new BulkRequest();
        ids.forEach(id -> request.add(new DeleteRequest(indexName(), id)));
        try {
            getClient().bulk(request, RequestOptions.DEFAULT);
            return true;
        } catch (Exception e) {
            log.error("deleteByIds [{}] esModels fail ids :{},error:{}", indexName(), JSON.toJSONString(ids), e.getMessage(), e);
            throw new MsCloudPlugException(MsCloudPlugBaseCode.ES_DELETE_MODEL_ERROR);
        }
    }

    /**
     * 批量增加修改数据
     *
     * @param models      数据
     * @param handlerFlag 是否需要处理数据
     */
    @Override
    public boolean updateList(List<T> models, boolean handlerFlag) {
        checkArg(indexName());
        Preconditions.checkNotNull(models, "Args list [{}] cannot be null", models);
        BulkRequest request = new BulkRequest();
        if (handlerFlag) {
            models = handlerList(models);
        }
        models.forEach(item -> request.add(new IndexRequest(indexName()).id(item.getId())
                .source(JSON.toJSONString(item), XContentType.JSON)));
        try {
            getClient().bulk(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("updateList [{}] esModels fail data :{},error:{}", indexName(), JSON.toJSONString(models), e.getMessage(), e);
            throw new MsCloudPlugException(MsCloudPlugBaseCode.ES_UPSET_MODEL_ERROR);
        }
        return true;
    }

    /**
     * 数据转换处理
     *
     * @param models
     * @return
     */
    protected List<T> handlerList(List<T> models) {
        return models;
    }


    /**
     * 根据ID获取数据
     *
     * @param id
     * @return
     */
    @Override
    public T findOne(String id) {
        checkArg(indexName());
        try {
            GetResponse getResponse = getClient().get(new GetRequest(indexName(),id),RequestOptions.DEFAULT);
            if (getResponse == null || getResponse.isSourceEmpty()) {
                return null;
            }
            T t = JSON.parseObject(getResponse.getSourceAsString(), getClassType());
            return t;
        } catch (Exception e) {
            log.error("findOne [{}] esModel fail id :{},error:{}", indexName(), id, e.getMessage(), e);
            throw new MsCloudPlugException(MsCloudPlugBaseCode.ES_DELETE_MODEL_ERROR);
        }
    }

    /**
     * 根据IDS批量获取数据
     *
     * @param ids
     * @return
     */
    @Override
    public List<T> findListByIds(List<String> ids) {
        checkArg(indexName());
        try {
            MultiGetRequest multiGetRequest = new MultiGetRequest();
            ids.forEach(id -> multiGetRequest.add(indexName(),id));
            MultiGetResponse multiGetResponse = getClient().mget(multiGetRequest,RequestOptions.DEFAULT);
            if (multiGetResponse == null || null == multiGetResponse.getResponses()
                    || multiGetResponse.getResponses().length == 0) {
                return Lists.newArrayList();
            }
            List<T> resultList = new ArrayList<T>(multiGetResponse.getResponses().length);
            for (MultiGetItemResponse multiGetItemResponse : multiGetResponse.getResponses()) {
                String json = multiGetItemResponse.getResponse().getSourceAsString();
                try {
                    resultList.add(JSONObject.parseObject(json, getClassType()));
                } catch (Exception e) {
                    log.error("Converting json:[{}] to clazz:[{}] err:[{}] ", json, getClassType(), e.getMessage(), e);
                }
            }
            return resultList;
        } catch (Exception e) {
            log.error("findListByIds [{}] esModels fail ids :{},error:{}", indexName(), JSON.toJSONString(ids), e.getMessage(), e);
            throw new MsCloudPlugException(MsCloudPlugBaseCode.ES_DELETE_MODEL_ERROR);
        }
    }

    /**
     * 根据参数查询数据
     *
     * @param t
     * @return
     */
    @Override
    public List<T> searchByParam(T t) {
        checkArg(indexName());
        SearchRequest request = new SearchRequest(indexName());
        request.source(createSearchSourceBuilder(t));
        try {
            SearchResponse response = getClient().search(request, RequestOptions.DEFAULT);
            if (response == null || null == response.getHits()
                    || null == response.getHits().getHits() || response.getHits().getHits().length == 0) {
                return Lists.newArrayList();
            }
            SearchHit[] hits = response.getHits().getHits();
            List<T> resultList = new ArrayList<>(hits.length);
            for (SearchHit hit : hits) {
                String json = hit.getSourceAsString();
                try {
                    resultList.add(JSONObject.parseObject(json, getClassType()));
                } catch (Exception e) {
                    log.error("Converting json:[{}] to clazz:[{}] err:[{}] ", json, getClassType(), e.getMessage(), e);
                }
            }
            return resultList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 创建搜索的条件 SearchSourceBuilder
     * @param t
     * @return
     */
    protected abstract SearchSourceBuilder createSearchSourceBuilder(T t);
}
