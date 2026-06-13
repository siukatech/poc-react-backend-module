package com.siukatech.poc.react.backend.module.core.util;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

@Slf4j
@NoArgsConstructor
public class DateTimeUtils {

    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String TIME_PATTERN = "HH:mm:ss";
    public static final String DATETIME_PATTERN = DATE_PATTERN + " " + TIME_PATTERN;

    public static LocalDateTime toLocalDateTime(Date legacyDate) {
        return toLocalDateTime(legacyDate, ZoneId.systemDefault());
    }
    public static LocalDateTime toLocalDateTime(Date legacyDate, ZoneId zoneId) {
        LocalDateTime localDateTime = Objects.isNull(legacyDate)?null:legacyDate.toInstant()
                .atZone(zoneId)
                .toLocalDateTime();
        return localDateTime;
    }
}
