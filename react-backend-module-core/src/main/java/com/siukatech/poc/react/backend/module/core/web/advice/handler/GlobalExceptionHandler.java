package com.siukatech.poc.react.backend.module.core.web.advice.handler;

import com.siukatech.poc.react.backend.module.core.security.exception.PermissionControlNotFoundException;
import com.siukatech.poc.react.backend.module.core.web.advice.mapper.ProblemDetailExtMapper;
import com.siukatech.poc.react.backend.module.core.web.advice.model.ErrorDetail;
import com.siukatech.poc.react.backend.module.core.web.advice.model.ProblemDetailExt;
import com.siukatech.poc.react.backend.module.core.web.micrometer.CorrelationIdHandler;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.oauth2.server.resource.introspection.BadOpaqueTokenException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
//@Component
//@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    //    private final Tracer tracer;
    private final CorrelationIdHandler correlationIdHandler;
    private final ProblemDetailExtMapper problemDetailExtMapper;

    public GlobalExceptionHandler(
//            Tracer tracer
//            ,
            CorrelationIdHandler correlationIdHandler
//            , ProblemDetailExtMapper problemDetailExtMapper
    ) {
//        this.tracer = tracer;
        this.correlationIdHandler = correlationIdHandler;
//        this.problemDetailExtMapper = problemDetailExtMapper;
        this.problemDetailExtMapper = Mappers.getMapper(ProblemDetailExtMapper.class);
    }

    protected ProblemDetailExt createProblemDetailExt(ProblemDetail body) {
        ProblemDetailExt bodyExt = problemDetailExtMapper.convertProblemDetailToExtWithoutCorrelationId(body);
        bodyExt.setCorrelationId(this.correlationIdHandler.getCorrelationId());
        return bodyExt;
    }

    protected ResponseEntity handleRuntimeInternal(RuntimeException ex, WebRequest request
            , HttpStatus status, Object[] args) {

        log.error("handleRuntimeInternal - status: [" +  status.toString()
                + "], ex: [" + ex
                + "], ex.getClass.getName: [" + ex.getClass().getName()
                + "], ex.getMessage: [" + ex.getMessage()
                + "], ex.fillInStackTrace: ", ex.fillInStackTrace());

        String defaultDetail = ex.getMessage();
        ProblemDetail body = createProblemDetail(ex, status, defaultDetail, null, args, request);
        return handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(value = {RuntimeException.class})
    protected ResponseEntity handleRuntimeException(RuntimeException ex, WebRequest request) {
        Object[] args = {ex.getClass().getName()};
        return this.handleRuntimeInternal(ex, request, HttpStatus.INTERNAL_SERVER_ERROR, args);
    }

    /**
     * This is exception handler of IllegalArgumentException
     *
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(value = {IllegalArgumentException.class})
    protected ResponseEntity handleIllegalArgument(IllegalArgumentException ex, WebRequest request) {
        Object[] args = {};
        return this.handleRuntimeInternal(ex, request, HttpStatus.CONFLICT, args);
    }

    /**
     * This is the exception handler of Spring Data ObjectOptimisticLockingFailureException
     *
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(value = {ObjectOptimisticLockingFailureException.class})
    protected ResponseEntity handleObjectOptimisticLockingFailure(
            ObjectOptimisticLockingFailureException ex, WebRequest request) {
        log.error("handleObjectOptimisticLockingFailure - ex: [" + ex
                + "], ex.getIdentifier.getClass.getName: [" + ex.getIdentifier().getClass().getName()
                + "], ex.getIdentifier: [" + ex.getIdentifier()
                + "], ex.getPersistentClassName: [" + ex.getPersistentClassName()
                + "]");
        Object[] args = {ex.getIdentifier(), ex.getPersistentClassName()};
        return this.handleRuntimeInternal(ex, request, HttpStatus.CONFLICT, args);
    }

    @Override
    protected ResponseEntity handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers
            , HttpStatusCode status, WebRequest request) {
//        return handleExceptionInternal(ex, defaultDetailBuf.toString(), headers, status, request);
//
        String contextPath = request.getContextPath();
        List<ErrorDetail> errorDetailList = new ArrayList<>();
//        StringBuffer defaultDetailBuf = new StringBuffer();
//        ex.getBindingResult().getAllErrors().forEach(objectError -> {
//            defaultDetailBuf
//                    .append("objectName: ").append(objectError.getObjectName())
//                    .append(", ").append("message: ").append(objectError.getDefaultMessage())
//                    .append(System.lineSeparator())
//            ;
//        });
        ex.getFieldErrors().forEach(fieldError -> {
            ErrorDetail errorDetail = new ErrorDetail(
                    fieldError.getCode()
                    , fieldError.getCodes()
                    , ErrorDetail.refineCodes(fieldError.getCodes(), true)
                    , ErrorDetail.refineCodes(fieldError.getCodes(), false)
                    , fieldError.getDefaultMessage()
                    , fieldError.getObjectName()
                    , fieldError.getField()
                    , fieldError.getRejectedValue()
                    , Arrays.stream(fieldError.getArguments() == null
                            ? new Object[0] : fieldError.getArguments()
                    ).toList()
            );
//            String errorDetail = "";
//            errorDetail += fieldError.getDefaultMessage();
//            errorDetail += " : ";
//            errorDetail += fieldError.getObjectName();
//            errorDetail += ".";
//            errorDetail += fieldError.getField();
//            errorDetail += "=";
//            errorDetail += fieldError.getRejectedValue();
//            if (fieldError.getArguments() != null) {
////                errorDetail += " (";
////                errorDetail += String.join(",", Arrays.stream(fieldError.getArguments()).toArray(String[]::new));
////                errorDetail += ")";
//                Arrays.stream(fieldError.getArguments()).forEach(arg -> {
//                    log.debug("handleMethodArgumentNotValid - arg: [" + arg + "]");
//                });
//            }

//            errorDetail += System.lineSeparator();
            errorDetailList.add(errorDetail);
//            defaultDetailBuf
//                    .append("objectName: ").append(fieldError.getObjectName())
//                    .append(", ")
//                    .append("fieldError: ").append(fieldError.getField())
//                    .append(", ")
//                    .append("rejectedValue: ").append(fieldError.getRejectedValue())
//                    .append(", ")
//                    .append("message: ").append(fieldError.getDefaultMessage())
//                    .append(fieldError.getDefaultMessage())
//                    .append(" (")
//                    .append(fieldError.getObjectName())
//                    .append(".")
//                    .append(fieldError.getField())
//                    .append("=")
//                    .append(fieldError.getRejectedValue())
//                    .append(")")
//                    .append(System.lineSeparator())
//            ;
        });
//        String defaultDetail = defaultDetailBuf.toString();
////        String defaultDetail = String.join("\n", errorDetailList.toArray(String[]::new));
//        String defaultDetail = StringUtils.join(errorDetailList, "\n");
        String defaultDetail = ex.getMessage();
        ProblemDetail body = createProblemDetail(ex, status, defaultDetail
                , null, null, request);
        body.setProperties(Map.of("errorDetails", errorDetailList));
        return handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    protected ResponseEntity handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        log.error("handleHttpMessageNotReadable - ex: [" + ex
                + "], ex.getClass.getName: [" + ex.getClass().getName()
                + "], ex.getMessage: [" + ex.getMessage()
                + "], ex.fillInStackTrace: ", ex.fillInStackTrace());

        String defaultDetail = ex.getMessage();
        ProblemDetail body = createProblemDetail(ex, status
//                , "Failed to read request"
                , defaultDetail
                , null, null, request);
        return handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    protected ResponseEntity handleHttpMessageNotWritable(
            HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        log.error("handleHttpMessageNotWritable - ex: [" + ex
                + "], ex.getClass.getName: [" + ex.getClass().getName()
                + "], ex.getMessage: [" + ex.getMessage()
                + "], ex.fillInStackTrace: ", ex.fillInStackTrace());

        String defaultDetail = ex.getMessage();
        ProblemDetail body = createProblemDetail(ex, status
//                , "Failed to write request"
                , defaultDetail
                , null, null, request);
        return handleExceptionInternal(ex, body, headers, status, request);
    }

    /**
     * This is the exception handler of Jpa EntityNotFoundException
     *
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(value = {EntityNotFoundException.class})
    protected ResponseEntity handleEntityNotFound(EntityNotFoundException ex, WebRequest request) {
        log.error("handleEntityNotFound - ex: [" + ex
                + "], ex.getClass.getName: [" + ex.getClass().getName()
                + "], ex.getMessage: [" + ex.getMessage()
                + "], ex.fillInStackTrace: ", ex.fillInStackTrace());
        return handleRuntimeInternal(ex, request, HttpStatus.NOT_FOUND, null);
    }

    @ExceptionHandler(value = PermissionControlNotFoundException.class)
    protected ResponseEntity handlePermissionControlNotFound(
            PermissionControlNotFoundException ex, WebRequest request) {

        log.error("handlePermissionControlNotFound - ex: [" + ex
                + "], ex.getClass.getName: [" + ex.getClass().getName()
                + "], ex.getMessage: [" + ex.getMessage()
                + "], request.getHeader: [" + request.getHeader("")
                + "], ex.fillInStackTrace: ", ex.fillInStackTrace()
        );

        HttpStatus status = HttpStatus.FORBIDDEN;
        Object[] args = {ex.getClass().getName()};
        String defaultDetail = ""
//                + ex.getMessage()  // should not expose the exception detail
                ;
        ProblemDetail body = createProblemDetail(ex, status, defaultDetail, null, args, request);
        ProblemDetailExt bodyExt = createProblemDetailExt(body);
        return handleExceptionInternal(ex, bodyExt, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(value = {InvalidBearerTokenException.class, BadOpaqueTokenException.class})
    protected ResponseEntity handleInvalidBearerToken(InvalidBearerTokenException ex, WebRequest request) {
        Object[] args = {ex.getClass().getName(), ex.getClass().getNestHost()};
        return this.handleRuntimeInternal(ex, request, HttpStatus.NON_AUTHORITATIVE_INFORMATION, args);

    }

}
