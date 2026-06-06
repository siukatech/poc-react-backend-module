package com.siukatech.poc.react.backend.module.quartz.service;

import com.siukatech.poc.react.backend.module.quartz.mapper.SchedulerMetaDataMapper;
import com.siukatech.poc.react.backend.module.quartz.model.SchedulerActionEnum;
import com.siukatech.poc.react.backend.module.quartz.model.SchedulerActionRes;
import com.siukatech.poc.react.backend.module.quartz.model.SchedulerInfoRec;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Slf4j
@Component
public class SchedulerService {

    private final Scheduler scheduler;

    public SchedulerService(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public SchedulerInfoRec getSchedulerInfo() throws SchedulerException {
        SchedulerMetaData schedulerMetaData = this.scheduler.getMetaData();
        SchedulerInfoRec schedulerInfoRec = SchedulerMetaDataMapper.INSTANCE.convertMetaDataToInfoRec(schedulerMetaData);
        return schedulerInfoRec;
    }

    public SchedulerActionRes configureScheduler(String action) throws SchedulerException {
        SchedulerActionEnum schedulerActionEnum = SchedulerActionEnum.fromAction(action);
        switch (schedulerActionEnum) {
            case START -> this.scheduler.start();
            case SHUTDOWN -> this.scheduler.shutdown();
            case PAUSE_ALL -> this.scheduler.pauseAll();
            case RESUME_ALL -> this.scheduler.resumeAll();
            case STANDBY -> this.scheduler.standby();
        }
        SchedulerActionRes schedulerActionRes = new SchedulerActionRes(schedulerActionEnum.getAction());
        return schedulerActionRes;
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

    public List<JobKey> getJobList() throws SchedulerException {
        List<JobKey> jobKeyList = new ArrayList<>();
        // 1. Get all job group names from all groups
        List<String> jobGroupNameList = this.scheduler.getJobGroupNames();
        for (String groupName : jobGroupNameList) {
            Set<JobKey> jobKeySet = this.scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName));
            for (JobKey jobKey : jobKeySet) {
                JobDetail jobDetail = this.scheduler.getJobDetail(jobKey);
                // Process details here
            }
            jobKeyList.addAll(jobKeySet);
        }
        return jobKeyList;
    }

}
