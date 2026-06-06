package com.siukatech.poc.react.backend.module.quartz;

import org.junit.jupiter.api.Test;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

//@DataJpaTest
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

//    @Test
    void givenEmailJob_whenSchedulerRestart_thenJobAndTriggerReloadedFromDatabase() throws Exception {
        // Verify the job and trigger exist in the running scheduler
        JobKey jobKey = new JobKey("emailJob", "emailGroup");
        TriggerKey triggerKey = new TriggerKey("emailTrigger", "emailGroup");

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