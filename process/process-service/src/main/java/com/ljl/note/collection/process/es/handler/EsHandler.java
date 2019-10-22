package com.ljl.note.collection.process.es.handler;


import com.ljl.note.collection.process.es.bean.EsModel;

import java.util.List;

/**
 * @description: EsHandler
 */
public interface EsHandler<T extends EsModel> {

    /**
     * 创建索引
     * @param indexMapping
     */
    void createIndex(String indexMapping);

    /**
     * 增加修改数据
     * @param t
     * @return
     */
    boolean update(T t);

    /**
     * 批量增加修改数据
     * @param tList         数据
     * @param handlerFlag  是否需要处理数据
     * @return
     */
    boolean updateList(List<T> tList, boolean handlerFlag);

    /**
     * 根据ID删除数据
     * @param id
     * @return
     */
    boolean deleteOne(String id);

    /**
     * 根据IDS批量删除数据
     * @param ids
     * @return
     */
    boolean deleteByIds(List<String> ids);

    /**
     * 根据ID获取数据
     * @param id
     * @return
     */
    T findOne(String id);

    /**
     * 根据IDS批量获取数据
     * @param ids
     * @return
     */
    List<T> findListByIds(List<String> ids);

    /**
     * 根据参数查询数据
     * @param t
     * @return
     */
    List<T> searchByParam(T t);
}
