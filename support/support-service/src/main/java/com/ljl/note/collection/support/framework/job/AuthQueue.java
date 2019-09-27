package com.ljl.note.collection.support.framework.job;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.DelayQueue;

@Data
@Component
@Slf4j
public class AuthQueue {

    public static DelayQueue<AuthTask> delayQueue = new DelayQueue<AuthTask>();

    public static Map<String,AuthTask> authMap = new HashMap<>();

    public static void removeId(String channelId){
        delayQueue.remove(authMap.get(channelId));
        log.info("delayQueue content:{}", Arrays.toString(delayQueue.toArray()));
        authMap.remove(channelId);
    }

    @PostConstruct
    public void authCheck(){
        new Thread(() -> {
            while(true){
                AuthTask task = null;
                try {
                    task = delayQueue.take();
                    log.info("删除消息{}",task);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(task.getContext() != null){
                    log.info("通道id{}",task.getContext());
                    authMap.remove(task.getContext().channel().id().asLongText());
                    task.getContext().channel().close();
                }
            }
        }).start();
    }

}
