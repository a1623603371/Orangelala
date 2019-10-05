package com.mayikt.pay.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;

/**
 * @ProjectName:整合线程池
 * @Package: com.mayikt.pay.config
 * @ClassName: AsycTaskConfig
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/24 17:20
 * @Version: 1.0
 */
@Configuration
@EnableAsync
@Slf4j
public class AsycTaskConfig implements AsyncConfigurer {
    /**
     * 最小线程数(核心线程数)
     */
    @Value("${threadPool.corePoolSize}")
    private int corePoolSize;
    /**
     * 最大线程数
     */
    @Value("${threadPool.maxPoolSize}")
    private int maxPoolSize;
    /**
     * 等待队列(队列最大长度)
     */
    @Value("${threadPool.queueCapacity}")
    private int queueCapacity;

    @Nullable
    @Override
    public Executor getAsyncExecutor() {
        return null;
    }

    @Nullable
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null;
    }
}
