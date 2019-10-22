package com.ljl.note.collection.process.service.es;

import com.ljl.note.collection.process.es.bean.EsIndexEnum;
import com.ljl.note.collection.process.es.handler.AbstractEsHandler;
import com.ljl.note.collection.process.util.DateUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @description: UserNodeEsService
 */
@Service
public class UserNodeEsService extends AbstractEsHandler<EsNodeUser> {

    /**
     * 索引名称
     *
     * @return
     */
    @Override
    public String indexName() {
        return EsIndexEnum.NODE_USER.getIndexName();
    }

    /**
     * 创建搜索的条件 SearchSourceBuilder
     *
     * @param esNodeUser
     * @return
     */
    @Override
    protected SearchSourceBuilder createSearchSourceBuilder(EsNodeUser esNodeUser) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        if (null == esNodeUser){
            return searchSourceBuilder;
        }
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //查询当前节点数据
        if (!StringUtils.isEmpty(esNodeUser.getNodeId())){
            boolQueryBuilder.must(QueryBuilders.termQuery("nodeId",esNodeUser.getNodeId()));
        }
        //查询传递时间的数据
        if (!StringUtils.isEmpty(esNodeUser.getCreateTime())){
            boolQueryBuilder.must(QueryBuilders.rangeQuery("createTime")
                    .lt(DateUtils.getTodayLastPointTimestamps(esNodeUser.getCreateTime()))
                    .gte(DateUtils.getTodayZeroPointTimestamps(esNodeUser.getCreateTime())));
        }
        searchSourceBuilder.query(boolQueryBuilder);
        return searchSourceBuilder;
    }
}
