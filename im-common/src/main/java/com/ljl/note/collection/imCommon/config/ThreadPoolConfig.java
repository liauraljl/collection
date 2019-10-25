package com.ljl.note.collection.imCommon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @description: ThreadPoolConfig 配置
 */
@Configuration
public class ThreadPoolConfig {

    @Bean("workPoolStartExecutor")
    public ThreadPoolTaskExecutor workPoolStartExecutor() {
        return setThreadPoolTaskExecutor("[workPoolStartExecutor] -",8,20);
    }

    @Bean(name = "statisticsExecutor")
    public ThreadPoolTaskExecutor statisticsExecutor() {
        return setThreadPoolTaskExecutor("[statisticsExecutor] -");
    }

    private ThreadPoolTaskExecutor setThreadPoolTaskExecutor(String name) {
        return setThreadPoolTaskExecutor(name,4,10);
    }

    private ThreadPoolTaskExecutor setThreadPoolTaskExecutor(String name, Integer coreSize, Integer maxSize) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(coreSize);
        executor.setMaxPoolSize(maxSize);
        executor.setQueueCapacity(10000);
        executor.setKeepAliveSeconds(20);
        executor.setAllowCoreThreadTimeOut(true);
        executor.setThreadNamePrefix(name);
        // CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

}
