package com.siukatech.poc.react.backend.module.quartz.model;

import org.quartz.*;

import java.time.LocalDateTime;
import java.util.Date;

public record TriggerRec(
        TriggerKeyRec triggerKeyRec
        , JobKeyRec jobKeyRec
        , String description
        , String calendarName
        , JobDataMap jobDataMap
        , int priority
//        , boolean mayFirstAgain
        , LocalDateTime startTime
        , LocalDateTime endTime
        , LocalDateTime nextFireTime
        , LocalDateTime previousFireTime
//        , LocalDateTime fireTimeAfter
        , LocalDateTime finalFireTime
        , int misfireInstruction
) {
}
