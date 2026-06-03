package com.siukatech.poc.react.backend.module.quartz.controller;


import com.siukatech.poc.react.backend.module.core.web.annotation.v1.PublicApiV1Controller;
import com.siukatech.poc.react.backend.module.quartz.model.SchedulerActionEnum;
import com.siukatech.poc.react.backend.module.quartz.model.SchedulerActionRes;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerMetaData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@PublicApiV1Controller
public class SchedulerController {

    private final Scheduler scheduler;

    public SchedulerController(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @GetMapping(value = "/quartz/scheduler")
    public ResponseEntity getScheduler() throws SchedulerException {
        SchedulerMetaData schedulerMetaData = this.scheduler.getMetaData();
        return ResponseEntity.ok(schedulerMetaData);
    }

    @PatchMapping(value = "/quartz/scheduler/{action}")
    public ResponseEntity configureScheduler(@PathVariable String action) throws SchedulerException {
        SchedulerActionEnum schedulerActionEnum = SchedulerActionEnum.fromAction(action);
        switch (schedulerActionEnum) {
            case START -> this.scheduler.start();
            case SHUTDOWN -> this.scheduler.shutdown();
            case PAUSE_ALL -> this.scheduler.pauseAll();
            case RESUME_ALL -> this.scheduler.resumeAll();
            case STANDBY -> this.scheduler.standby();
        }
        SchedulerActionRes schedulerActionRes = new SchedulerActionRes(action);
        log.debug("configureScheduler - schedulerActionRes: [" + action
                + "]");
        return ResponseEntity.ok(schedulerActionRes);
    }

}
