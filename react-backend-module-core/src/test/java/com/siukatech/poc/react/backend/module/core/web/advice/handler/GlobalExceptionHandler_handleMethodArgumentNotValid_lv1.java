package com.siukatech.poc.react.backend.module.core.web.advice.handler;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GlobalExceptionHandler_handleMethodArgumentNotValid_lv1 {
    @Size(min = 5, max = 10)
    private String id;
    @NotNull
    private GlobalExceptionHandler_handleMethodArgumentNotValid_lv2 lv2;
    @Valid
    private List<GlobalExceptionHandler_handleMethodArgumentNotValid_lv2> lv2List;
}
