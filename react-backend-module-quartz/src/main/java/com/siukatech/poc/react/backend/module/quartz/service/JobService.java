package com.siukatech.poc.react.backend.module.quartz.service;


import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class JobService {

    private final Scheduler scheduler;

    public JobService(Scheduler scheduler) {
        this.scheduler = scheduler;
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
