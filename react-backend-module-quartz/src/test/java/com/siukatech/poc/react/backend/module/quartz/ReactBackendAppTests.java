package com.siukatech.poc.react.backend.module.quartz;

import com.siukatech.poc.react.backend.module.quartz.job.EmailJob;
import org.junit.jupiter.api.Test;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@Import(value = {
        ReactBackendAppTests.TestConfig.class
})
@SpringBootTest(properties = {
        "spring.quartz.job-store-type=memory"
//        "spring.quartz.job-store-type=jdbc"
//        , "spring.quartz.jdbc.initialize-schema=always"
})
public class ReactBackendAppTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private Scheduler scheduler;

    @TestConfiguration
    static class TestConfig {

    }

    @Test
    void givenEmailJob_whenSchedulerRestart_thenJobAndTriggerReloadedFromDatabase() throws Exception {

        JobDetail emailJobDetail = JobBuilder.newJob(EmailJob.class)
                    .withIdentity("emailJob")
                    .storeDurably()
                    .build();
        CronScheduleBuilder schedule = CronScheduleBuilder.cronSchedule("0 0/1 * * * ?"); // every 1 min
        Trigger emailJobTrigger = TriggerBuilder.newTrigger()
                    .forJob(emailJobDetail)
                    .withIdentity("emailTrigger")
                    .withSchedule(schedule)
                    .build();
        scheduler.scheduleJob(emailJobDetail, emailJobTrigger);

        // Verify the job and trigger exist in the running scheduler
        JobKey jobKey = new JobKey("emailJob", "DEFAULT");
        TriggerKey triggerKey = new TriggerKey("emailTrigger", "DEFAULT");

        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        assertNotNull(jobDetail, "EmailJob should exist in the running scheduler");

        Trigger trigger = scheduler.getTrigger(triggerKey);
        assertNotNull(trigger, "EmailTrigger should exist in the running scheduler");

        // Stop and restart the scheduler
        scheduler.standby();
        Scheduler restartedScheduler = applicationContext.getBean(Scheduler.class);
        restartedScheduler.start();

        // The job and trigger should be reloaded from the database
        assertTrue(restartedScheduler.isStarted(), "Scheduler should be running after restart");

        JobDetail reloadedJob = restartedScheduler.getJobDetail(jobKey);
        assertNotNull(reloadedJob, "EmailJob should be reloaded from the database after restart");

        Trigger reloadedTrigger = restartedScheduler.getTrigger(triggerKey);
        assertNotNull(reloadedTrigger, "EmailTrigger should be reloaded from the database after restart");
    }
}