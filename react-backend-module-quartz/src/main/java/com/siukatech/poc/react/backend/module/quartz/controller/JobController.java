package com.siukatech.poc.react.backend.module.quartz.controller;

import com.siukatech.poc.react.backend.module.core.web.annotation.v1.PublicApiV1Controller;
import com.siukatech.poc.react.backend.module.quartz.service.SchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j
@PublicApiV1Controller
public class JobController {

    private final Scheduler scheduler;
    private final SchedulerService schedulerService;

    public JobController(Scheduler scheduler, SchedulerService schedulerService) {
        this.scheduler = scheduler;
        this.schedulerService = schedulerService;
    }

    @GetMapping(value = "/quartz/job/keys")
    public ResponseEntity getJobKeyList() throws SchedulerException {
        List<JobKey> jobKeyList = this.schedulerService.getJobList();
        return ResponseEntity.ok(jobKeyList);
    }

}
