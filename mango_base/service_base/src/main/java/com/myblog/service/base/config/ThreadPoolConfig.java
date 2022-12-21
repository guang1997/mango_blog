package com.myblog.service.base.config;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThreadPoolConfig {

    @Value("${thread.webVisit.core-pool-size:2}")
    private int webVisitPoolCoreSize;

    @Value("${thread.webVisit.max-pool-size:5}")
    private int webVisitPoolMaxSize;

    @Value("${thread.webVisit.queue-capacity:100}")
    private int webVisitQueueSize;

    @Value("${thread.webVisit.keep-alive-seconds:1}")
    private Long webVisitTaskKeepAlive;

    @Value("${thread.webVisit.core-pool-size:1}")
    private int exceptionLogPoolCoreSize;

    @Value("${thread.webVisit.max-pool-size:5}")
    private int exceptionLogPoolMaxSize;

    @Value("${thread.webVisit.queue-capacity:100}")
    private int exceptionLogQueueSize;

    @Value("${thread.webVisit.keep-alive-seconds:1}")
    private Long exceptionLogTaskKeepAlive;

    @Bean
    public ThreadPoolExecutor webVisitQueueSizeTaskPool() {
        return new ThreadPoolExecutor(webVisitPoolCoreSize, webVisitPoolMaxSize,
            webVisitTaskKeepAlive,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(webVisitQueueSize), new ThreadPoolExecutor.CallerRunsPolicy());
    }

    @Bean
    public ThreadPoolExecutor exceptionLogQueueSizeTaskPool() {
        return new ThreadPoolExecutor(exceptionLogPoolCoreSize, exceptionLogPoolMaxSize,
            exceptionLogTaskKeepAlive,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(exceptionLogQueueSize), new ThreadPoolExecutor.CallerRunsPolicy());
    }
}
