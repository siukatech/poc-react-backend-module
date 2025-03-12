package com.siukatech.poc.react.backend.module.core.web.advice.model;

import lombok.Data;
import org.springframework.http.ProblemDetail;

@Data
public class ProblemDetailExt extends ProblemDetail {
    protected String correlationId;
}
