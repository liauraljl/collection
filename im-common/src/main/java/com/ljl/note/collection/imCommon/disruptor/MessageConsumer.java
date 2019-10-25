package com.ljl.note.collection.imCommon.disruptor;

import com.ljl.note.collection.imCommon.entity.TranslatorDataWapper;
import com.lmax.disruptor.WorkHandler;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 消费者
 */
@Data
@AllArgsConstructor
public abstract class MessageConsumer implements WorkHandler<TranslatorDataWapper> {

    protected String consumerId;
}
