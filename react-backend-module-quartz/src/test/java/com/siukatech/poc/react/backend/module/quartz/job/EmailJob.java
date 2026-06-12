package com.siukatech.poc.react.backend.module.quartz.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;

@Slf4j
public class EmailJob extends AbstractJob {

    @Override
    public void run(JobExecutionContext context) {
        log.info("run - Executing EmailJob at [{}]", context.getFireTime());
    }

}