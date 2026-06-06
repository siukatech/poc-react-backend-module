package com.siukatech.poc.react.backend.module.quartz.controller;


import com.siukatech.poc.react.backend.module.core.web.annotation.v1.PublicApiV1Controller;
import com.siukatech.poc.react.backend.module.quartz.model.SchedulerActionRes;
import com.siukatech.poc.react.backend.module.quartz.model.SchedulerInfoRec;
import com.siukatech.poc.react.backend.module.quartz.service.SchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@PublicApiV1Controller
public class SchedulerController {

    private final SchedulerService schedulerService;

    public SchedulerController(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    @GetMapping(value = "/quartz/scheduler")
    public ResponseEntity getScheduler() throws SchedulerException {
        SchedulerInfoRec schedulerInfoRec = this.schedulerService.getSchedulerInfo();
        return ResponseEntity.ok(schedulerInfoRec);
    }

    @PatchMapping(value = "/quartz/scheduler/{action}")
    public ResponseEntity configureScheduler(@PathVariable String action) throws SchedulerException {
        SchedulerActionRes schedulerActionRes = this.schedulerService.configureScheduler(action);
        log.debug("configureScheduler - schedulerActionRes: [" + action
                + "]");
        return ResponseEntity.ok(schedulerActionRes);
    }

}
