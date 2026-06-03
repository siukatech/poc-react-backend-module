package com.siukatech.poc.react.backend.module.quartz.service;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class TriggerService {

    private final Scheduler scheduler;

    public TriggerService(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public List<Trigger> getTriggerList() throws SchedulerException {
        List<Trigger> triggerList = new ArrayList<>();
        // 1. Get all trigger keys from all groups
        Set<TriggerKey> triggerKeySet = this.scheduler.getTriggerKeys(GroupMatcher.anyTriggerGroup());
        // 2. Loop through keys and retrieve individual triggers
        for (TriggerKey triggerKey : triggerKeySet) {
            Trigger trigger = this.scheduler.getTrigger(triggerKey);
            if (trigger != null) {
                triggerList.add(trigger);
            }
        }
        return triggerList;
    }

}
