package com.siukatech.poc.react.backend.module.core.web.advice.handler;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GlobalExceptionHandler_handleMethodArgumentNotValid_lv2 {
    @Size(min = 5, max = 10)
    private String id;
}
