package com.siukatech.poc.react.backend.module.quartz.listener;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.listeners.JobListenerSupport;

import java.util.concurrent.CountDownLatch;

@Slf4j
public class TestJobListener extends JobListenerSupport {

    private final CountDownLatch countDownLatch;

    public TestJobListener(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public String getName() {
        return "TestJobListener";
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        log.debug("jobWasExecuted - countDownLatch: [{}]", countDownLatch);
        // Quartz will trigger this when job was executed
        countDownLatch.countDown(); // Release the lock
    }

}
