package com.siukatech.poc.react.backend.module.quartz.job;

import com.siukatech.poc.react.backend.module.core.util.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Slf4j
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public abstract class AbstractJob implements Job {

    public static final String KEY_BIZ_DATE_TIME_STR = "bizDateTimeStr";

    private String bizDateTimeStr; // Auto-inject from JobDataMap with key 'bizDateTime'

    public void setBizDateTimeStr(String bizDateTimeStr) {
        this.bizDateTimeStr = bizDateTimeStr;
    }

    @Override
    public void execute(JobExecutionContext context) {
        Date fireTime = context.getFireTime();
        LocalDateTime bizDateTime;
        // Check bizDateTimeStr null, that means JobDataMap having this key or not
        if (this.bizDateTimeStr != null && !this.bizDateTimeStr.trim().isEmpty()) {
            bizDateTime = LocalDateTime.parse(this.bizDateTimeStr, DateTimeFormatter.ofPattern(DateTimeUtils.DATETIME_PATTERN));
        } else {
            bizDateTime = DateTimeUtils.toLocalDateTime(fireTime);
        }
        log.debug("execute - Executing AbstractJob at [{}], bizDateTimeStr: [{}], bizDateTime: [{}]", fireTime, this.bizDateTimeStr, bizDateTime);
        this.process(context, bizDateTime);
    }

    public abstract void process(JobExecutionContext context, LocalDateTime bizDateTime);

}
