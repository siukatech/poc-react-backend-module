package com.siukatech.poc.react.backend.module.quartz.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;

@Slf4j
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public abstract class AbstractJob implements Job {

//    private static final Logger logger = LoggerFactory.getLogger(Job.class);

    @Override
    public void execute(JobExecutionContext context) {
        log.debug("execute - Executing AbstractJob at [{}]", context.getFireTime());
        this.run(context);
    }

    public abstract void run(JobExecutionContext context);

}
