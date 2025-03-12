package com.siukatech.poc.react.backend.module.core.web.advice.handler;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GlobalExceptionHandler_controller {

    @PostMapping(value = "/post_handleMethodArgumentNotValid")
    public ResponseEntity<GlobalExceptionHandler_result> post_handleMethodArgumentNotValid(
            @Valid @RequestBody GlobalExceptionHandler_handleMethodArgumentNotValid_lv1 lv1) {
        GlobalExceptionHandler_result result = new GlobalExceptionHandler_result();
        return ResponseEntity.ok(result);
    }
}
