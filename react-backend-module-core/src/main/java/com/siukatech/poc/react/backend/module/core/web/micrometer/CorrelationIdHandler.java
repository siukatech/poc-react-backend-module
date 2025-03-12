package com.siukatech.poc.react.backend.module.core.web.micrometer;

import io.micrometer.tracing.CurrentTraceContext;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class CorrelationIdHandler {

    public static final String HEADER_X_CORRELATION_ID = "X-Correlation-Id";

    private final Tracer tracer;

    public CorrelationIdHandler(Tracer tracer) {
        this.tracer = tracer;
    }

    public String getCorrelationId() {
        TraceContext traceContext = Optional.of(tracer).map(Tracer::currentTraceContext).map(CurrentTraceContext::context).orElse(null);
        String traceId = Optional.of(traceContext).map(TraceContext::traceId).orElse("NULL");
        String spanId = Optional.of(traceContext).map(TraceContext::spanId).orElse("NULL");
        String parentId = Optional.of(traceContext).map(TraceContext::parentId).orElse("NULL");
        Span span = tracer.currentSpan();
        log.debug("getCorrelationId - traceId: [{}], spanId: [{}], parentId: [{}]"
                , traceId, spanId, parentId);
        log.debug("getCorrelationId - traceContext: [{}]", traceContext.toString());
        log.debug("getCorrelationId - span: [{}]", span.toString());
        return Optional.of(tracer)
                .map(t -> t.currentTraceContext())
                .map(tc -> tc.context())
                .map(cxt -> {
                    String correlationId = String.format("%s-%s"
                            , cxt.traceId()
                            , cxt.spanId()
                    );
                    return correlationId;
                })
//                .map(cxt -> cxt.toString())
                .orElse("")
                ;
    }

}
