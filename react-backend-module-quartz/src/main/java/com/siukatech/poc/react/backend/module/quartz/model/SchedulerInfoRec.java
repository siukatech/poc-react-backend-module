package com.siukatech.poc.react.backend.module.quartz.model;

import java.time.LocalDateTime;

public record SchedulerInfoRec(
        String schedulerName
        , String schedulerInstanceId
        , Class<?> schedulerClass
        , LocalDateTime runningSince
        , int numberOfJobsExecuted
        , boolean schedulerRemote
        , boolean started
        , boolean inStandbyMode
        , boolean shutdown
        , Class<?> jobStoreClass
        , boolean jobStoreSupportsPersistence
        , boolean jobStoreClustered
        , Class<?> threadPoolClass
        , int threadPoolSize
        , String version
) {
}
