package com.ljl.note.collection.liveRecord.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ThreadPoolConfig {

    @Bean("liveRecordMergeTaskExecutor")
    public ThreadPoolTaskExecutor liveRecordMergeTaskExecutor(){
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(20);
        threadPoolTaskExecutor.setQueueCapacity(400);
        threadPoolTaskExecutor.setMaxPoolSize(150);
        threadPoolTaskExecutor.setKeepAliveSeconds(180);
        threadPoolTaskExecutor.setThreadNamePrefix("liveRecordMergeTaskExecutor");
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return threadPoolTaskExecutor;
    }

    @Bean("queryLiveRecordMergeResultExecutor")
    public ThreadPoolTaskExecutor queryLiveRecordMergeResultExecutor(){
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(20);
        threadPoolTaskExecutor.setQueueCapacity(400);
        threadPoolTaskExecutor.setMaxPoolSize(150);
        threadPoolTaskExecutor.setKeepAliveSeconds(180);
        threadPoolTaskExecutor.setThreadNamePrefix("queryLiveRecordMergeResultExecutor");
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return threadPoolTaskExecutor;
    }

    @Bean("liveRecordPartDeleteExecutor")
    public ThreadPoolTaskExecutor liveRecordPartDeleteExecutor(){
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(20);
        threadPoolTaskExecutor.setQueueCapacity(400);
        threadPoolTaskExecutor.setMaxPoolSize(150);
        threadPoolTaskExecutor.setKeepAliveSeconds(180);
        threadPoolTaskExecutor.setThreadNamePrefix("liveRecordPartDeleteExecutor");
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return threadPoolTaskExecutor;
    }

    @Bean("fluxCountTotalHandleExecutor")
    public ThreadPoolTaskExecutor fluxCountTotalHandleExecutor(){
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(20);
        threadPoolTaskExecutor.setQueueCapacity(400);
        threadPoolTaskExecutor.setMaxPoolSize(150);
        threadPoolTaskExecutor.setKeepAliveSeconds(180);
        threadPoolTaskExecutor.setThreadNamePrefix("fluxCountTotalHandleExecutor");
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return threadPoolTaskExecutor;
    }
}
