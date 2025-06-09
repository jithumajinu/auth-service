package com.openapi.cloud.core.exception;

import com.acid.core.web.constants.ApiErrorCode;
import com.google.common.collect.Maps;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.acid.core.web.model.ApiResponse;
import com.acid.core.web.model.ApiResponse.ApiError.ErrorDetail;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handleAccessDeniedException(
            AccessDeniedException ex,
            HttpServletRequest request) {

        log.error("AccessDeniedException occurred at [{}] - Message: {}",
                request.getRequestURI(),
                ex.getMessage());
        return new ResponseEntity<>(createAcApiErrorCodeResponse(ex), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request) {

        log.error("Exception occurred at [{}] - Message: {}, StackTrace:",
                request.getRequestURI(),
                ex.getMessage(),
                ex);
        return new ResponseEntity<>(createAcApiErrorCodeResponse(ex), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(BindException.class)
    public <T> ResponseEntity<ApiResponse<T>> handleError(BindException ex) {
        return new ResponseEntity<>(createAcApiResponse(ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse> handleNotFound(
            NotFoundException ex,
            HttpServletRequest request) {

        log.error("NotFoundException occurred at [{}] - Message: {}, StackTrace:",
                request.getRequestURI(),
                ex.getMessage(),
                ex);
        return new ResponseEntity<>(createAcApiErrorCodeResponse(ex), HttpStatus.NOT_FOUND);
    }


    public static <T> ApiResponse<T> createAcApiResponse(BindException ex) {
        return ApiResponse.<T>builder()
                .error(ApiResponse.ApiError.builder()
                        .errorCode(ApiErrorCode.INPUT_ERROR)
                        .message(ex.getMessage())
                        .errors(createAcInputErrors(ex.getBindingResult()))
                        .build()
                ).build();
    }


    public static Map<String, ErrorDetail> createAcInputErrors(BindingResult bindingResult) {
        Map<String, ErrorDetail> errors = Maps.newHashMap();
        for (FieldError err : bindingResult.getFieldErrors()) {
            errors.put(err.getField(),
                    ErrorDetail.builder()
                            .code(err.getCodes()[err.getCodes().length - 1])
                            .message(err.getDefaultMessage())
                            .build());
        }
        return errors;
    }

    public static <T> ApiResponse<T> createAcApiErrorCodeResponse(Exception ex) {
        ApiErrorCode apiErrorCode = null;

        apiErrorCode = ApiErrorCode.GENERAL_ERROR;

        return ApiResponse.<T>builder()
                .error(ApiResponse.ApiError.builder()
                        .errorCode(apiErrorCode)
                        .message(ex.getMessage())
                        .build()
                ).build();
    }

}
