package com.siukatech.poc.react.backend.module.quartz.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;

import java.time.LocalDateTime;

@Slf4j
public class TestJob extends AbstractJob {

    @Override
    public void process(JobExecutionContext context, LocalDateTime bizDateTime) {
        log.info("process - Executing TestJob at [{}], bizDateTime: [{}]", context.getFireTime(), bizDateTime);
    }

}