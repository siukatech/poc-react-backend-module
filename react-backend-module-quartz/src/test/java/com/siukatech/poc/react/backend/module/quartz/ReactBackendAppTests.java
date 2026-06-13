package com.siukatech.poc.react.backend.module.quartz;

import com.siukatech.poc.react.backend.module.quartz.job.AbstractJob;
import com.siukatech.poc.react.backend.module.quartz.job.TestJob;
import com.siukatech.poc.react.backend.module.quartz.listener.TestJobListener;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.quartz.*;
import org.quartz.impl.matchers.KeyMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@Slf4j
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
    void givenTestJob_whenSchedulerRestart_thenJobAndTriggerReloadedFromDatabase() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        JobListener testJobListener = new TestJobListener(latch);

        JobKey jobKey = new JobKey("testJob", "DEFAULT");
        TriggerKey triggerKey = new TriggerKey("testTrigger", "DEFAULT");
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(AbstractJob.KEY_BIZ_DATE_TIME_STR, "2026-06-14 01:27:00");

        JobDetail testJobDetail = JobBuilder.newJob(TestJob.class)
//                .withIdentity("testJob")
                .withIdentity(jobKey)
                .setJobData(jobDataMap)
                .storeDurably()
                .build();

        CronScheduleBuilder schedule = CronScheduleBuilder.cronSchedule("0/20 * * * * ?"); // every 20 second
        Trigger testJobTrigger = TriggerBuilder.newTrigger()
                .forJob(testJobDetail)
//                .withIdentity("testTrigger")
                .withIdentity(triggerKey)
                .withSchedule(schedule)
                .build();

        this.scheduler.getListenerManager().addJobListener(testJobListener, KeyMatcher.keyEquals(jobKey));
        this.scheduler.scheduleJob(testJobDetail, testJobTrigger);

        JobDetail jobDetail = this.scheduler.getJobDetail(jobKey);
        assertNotNull(jobDetail, "EmailJob should exist in the running scheduler");
        log.info("givenEmailJob - jobDetail: [{}]", jobDetail);

        Trigger trigger = this.scheduler.getTrigger(triggerKey);
        assertNotNull(trigger, "EmailTrigger should exist in the running scheduler");
        log.info("givenEmailJob - trigger: [{}]", trigger);

//        Awaitility.await()
//                .atMost(70, SECONDS)
//                .pollInterval(2, SECONDS)
//                .until(() -> this.tracker.isJobExecuted())
//        ;
        boolean finished = latch.await(70, TimeUnit.SECONDS);
        assertTrue(finished, "Job was not completed at expected datetime");

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