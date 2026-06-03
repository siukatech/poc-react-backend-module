package com.siukatech.poc.react.backend.module.quartz.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum SchedulerActionEnum {

    START("start")
    , PAUSE_ALL("pauseAll")
    , RESUME_ALL("resumeAll")
    , SHUTDOWN("shutdown")
    , STANDBY("standby")
    ;

    @Getter
    @JsonValue
    private final String action;
    private static final Map<String, SchedulerActionEnum> ACTION_MAP;

    // Constructor
    SchedulerActionEnum(String action) {
        this.action = action;
    }
    // Static block initializes the lookup map once for high performance
    static {
        Map<String, SchedulerActionEnum> map = new HashMap<>();
        for (SchedulerActionEnum schedulerActionEnum : SchedulerActionEnum.values()) {
            // Store using lowercase to ensure case-insensitive matching
            map.put(schedulerActionEnum.action.toLowerCase(), schedulerActionEnum);
        }
        ACTION_MAP = Collections.unmodifiableMap(map);
    }

    // Custom retrieval method
    // Tells Jackson how to reconstruct the Enum from the JSON value
    @JsonCreator
    public static SchedulerActionEnum fromAction(String action) {
        if (action == null) {
//            return null;
            throw new IllegalArgumentException("Unknown scheduler action: [%s]".formatted(action));
        }
        // Match against the map keys safely
        return ACTION_MAP.get(action.trim().toLowerCase());
    }

}
