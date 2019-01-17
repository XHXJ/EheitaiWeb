package com.xhxj.Component;


import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/*
*
* 配置线程池
* */

@Configuration
@ComponentScan("com.xhxj")
@EnableAsync//利用@EnableAsync注解开启异步任务支持
public class CustomMultiThreadingConfig implements AsyncConfigurer{
    @Override
    public Executor getAsyncExecutor() {// 实现AsyncConfigurer接口并重写getAsyncExecutor方法，并返回一个ThreadPoolTaskExecutor，这样我们就获得了一个基于线程池TaskExecutor
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        //线程池维护线程的最少数量
        taskExecutor.setCorePoolSize(40);
        //线程最大的数量
        taskExecutor.setMaxPoolSize(80);
        //线程池所使用的缓冲队列
        taskExecutor.setQueueCapacity(1000);
        taskExecutor.initialize();
        return taskExecutor;
    }
}
