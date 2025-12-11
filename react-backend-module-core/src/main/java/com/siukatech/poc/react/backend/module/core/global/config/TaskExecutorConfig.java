package com.siukatech.poc.react.backend.module.core.global.config;

import io.micrometer.context.ContextSnapshotFactory;
import org.springframework.boot.task.ThreadPoolTaskExecutorBuilder;
import org.springframework.boot.task.ThreadPoolTaskExecutorCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadFactory;

@Configuration
public class TaskExecutorConfig {

    @Bean
    public ThreadPoolTaskExecutorCustomizer daemonThreadCustomizer() {
        return (executor) -> {
            // Get the current thread name prefix (optional, for clarity)
            String prefix = executor.getThreadNamePrefix() != null ? executor.getThreadNamePrefix() : "task-";

            // Create a custom ThreadFactory
            ThreadFactory customThreadFactory = runnable -> {
                Thread thread = new Thread(runnable, prefix + executor.getPoolSize());
                thread.setDaemon(true); // Set the thread as a daemon
                return thread;
            };

            // Set the custom ThreadFactory on the ThreadPoolTaskExecutor
            executor.setThreadFactory(customThreadFactory);
        };
    }

    @Bean
    public ThreadPoolTaskExecutorBuilder threadPoolTaskExecutorBuilder() {
        return new ThreadPoolTaskExecutorBuilder()
                .customizers(daemonThreadCustomizer()); // Add the customizer
    }

    @Bean
    public TaskDecorator taskDecorator() {
        return runnable -> ContextSnapshotFactory.builder().build().captureAll().wrap(runnable);
    }

    // You can then build your executor using the configured builder:
    @Bean
    public ThreadPoolTaskExecutor defaultThreadPoolTaskExecutor(
            ThreadPoolTaskExecutorBuilder threadPoolTaskExecutorBuilder
            , TaskDecorator taskDecorator) {
        return threadPoolTaskExecutorBuilder
                .corePoolSize(2)
                .maxPoolSize(4)
                .queueCapacity(10)
                .threadNamePrefix("default-daemon-task-")
                .taskDecorator(taskDecorator)
                .build();
    }

}
