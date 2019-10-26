package com.ljl.note.collection.imClient.framework.disruptor;

import com.ljl.note.collection.imClient.framework.disruptor.consumer.ClientMessageConsumer;
import com.ljl.note.collection.imCommon.disruptor.MessageConsumer;
import com.ljl.note.collection.imCommon.disruptor.RingBufferWorkerPoolFactory;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DisruptorStart {

    @PostConstruct
    public void disruptorStart(){
        MessageConsumer[] conusmers = new MessageConsumer[4];
        for(int i =0; i < conusmers.length; i++) {
            MessageConsumer messageConsumer = new ClientMessageConsumer("code:clientId:" + i);
            conusmers[i] = messageConsumer;
        }
        RingBufferWorkerPoolFactory.getInstance().initAndStart(ProducerType.MULTI,
                1024*1024,
                //new YieldingWaitStrategy(),
                new BlockingWaitStrategy(),
                conusmers);
    }
}
