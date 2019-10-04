package com.ljl.note.collection.common.mapper;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * Created by liaura_ljl on 2019/7/27.
 */
public interface CommonMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
