package com.test.task.videostream.config;

import org.apache.tomcat.util.threads.TaskQueue;
import org.apache.tomcat.util.threads.TaskThreadFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableAsync
public class AsyncConfig {
    public static final String ASYNC_TASK_EXECUTOR = "taskExecutor";

    @Value("${async.task.executor.poolSize.core}")
    private int corePoolSize;
    @Value("${async.task.executor.poolSize.max}")
    private int maxPoolSize;
    @Value("${async.task.executor.keepAliveTime}")
    private int keepAliveTime;
    @Value("${async.task.executor.queueCapacity}")
    private int queueCapacity;
    @Value("${async.task.executor.threadNamePrefix}")
    private String threadNamePrefix;

    @Bean(ASYNC_TASK_EXECUTOR)
    public Executor taskExecutor() {
        TaskQueue workQueue = new TaskQueue(queueCapacity);
        TaskThreadFactory taskThreadFactory = new TaskThreadFactory(threadNamePrefix, false, Thread.NORM_PRIORITY);
        return new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.MINUTES, workQueue, taskThreadFactory);
    }
}
