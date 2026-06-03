package com.siukatech.poc.react.backend.module.quartz.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SchedulerActionRes (
        @JsonProperty String action
) {
}
