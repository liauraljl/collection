package com.ljl.note.collection.imCommon.entity;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

/**
 * disruptor 数据载体
 */
@Data
public class TranslatorDataWapper {

    private TranslatorData data;

    private ChannelHandlerContext ctx;
}
