package com.siukatech.poc.react.backend.module.quartz.controller;


import com.siukatech.poc.react.backend.module.core.web.annotation.v1.PublicApiV1Controller;
import com.siukatech.poc.react.backend.module.quartz.service.TriggerService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j
@PublicApiV1Controller
public class TriggerController {

    private final Scheduler scheduler;
    private final TriggerService triggerService;

    public TriggerController(Scheduler scheduler, TriggerService triggerService) {
        this.scheduler = scheduler;
        this.triggerService = triggerService;
    }

    @GetMapping(value = "/quartz/triggers/")
    public ResponseEntity getTriggerList() throws SchedulerException {
        List<Trigger> triggerList = this.triggerService.getTriggerList();
        return ResponseEntity.ok(triggerList);
    }

}
