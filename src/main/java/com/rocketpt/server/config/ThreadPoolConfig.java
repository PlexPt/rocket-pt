package com.rocketpt.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

import lombok.extern.slf4j.Slf4j;

/**
 * @author pt
 * @email plexpt@gmail.com
 * @date 2019-09-16 10:55
 */

@Slf4j
@EnableAsync
@Configuration
public class ThreadPoolConfig {

    /**
     * 默认线程池 springboot
     *
     * @return Executor
     */
    @Bean
    public ThreadPoolTaskExecutor defaultThreadPool() {
        return getThreadPool("default-async-service-", 16, 256,
                16, 60);
    }

    /**
     * @return 系统日志记录
     */
    @Bean
    public ThreadPoolTaskExecutor logThreadPool() {
        return getThreadPool("log-async-service-", 16, 256,
                16, 10);
    }

    /**
     * @return KEY同步池
     */
    @Bean
    public ThreadPoolTaskExecutor keyThreadPool() {
        return getThreadPool("keyThreadPool", 40, 40,
                9000, 60);
    }


    /**
     * @param threadNamePrefix 线程名称前缀 default-async-service-
     * @param corePoolSize     核心线程数目
     * @param maxPoolSize      最大线程数
     * @param queueCapacity    队列中最大的数目
     * @param keepAliveSeconds 线程空闲后的最大存活时间
     * @return
     */
    public ThreadPoolTaskExecutor getThreadPool(String threadNamePrefix, int corePoolSize,
                                                int maxPoolSize,
                                                int queueCapacity, int keepAliveSeconds) {
        log.info("start asyncServiceExecutor");

//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix(threadNamePrefix);

        //rejection-policy：当pool已经达到max size的时候，如何处理新任务
        //CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行
        //对拒绝task的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setKeepAliveSeconds(keepAliveSeconds);
        //加载
        executor.initialize();
        return executor;
    }

    //通过注解引入配置示例
//    @Resource(name = "defaultThreadPool")
//    private ThreadPoolTaskExecutor executor;

}
