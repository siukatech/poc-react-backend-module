package com.siukatech.poc.react.backend.module.core.business.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.TimeZone;


@Data
//@EqualsAndHashCode(callSuper = true)
public class UserViewDto extends UserDto {
    private LocalDateTime appDatetime;
    private LocalDateTime dbDatetime;
    private TimeZone timeZone;
}

