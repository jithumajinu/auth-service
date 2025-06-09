package com.openapi.cloud.core.controller;

import com.acid.core.web.model.ApiResponse;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public abstract class AbstractCoreUtilController {


    @Autowired
    private MessageSource messageSource;

    /**
     * Formats input errors from BindingResult into a map of error details.
     *
     * @param result the BindingResult containing validation errors
     * @return a map where keys are field names and values are ApiError.ErrorDetail objects
     */
    protected Map<String, ApiResponse.ApiError.ErrorDetail> formatInputErrors(BindingResult result) {
        Map<String, ApiResponse.ApiError.ErrorDetail> errors = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(result.getAllErrors())) {
            errors = result.getAllErrors().stream()
                    .filter(FieldError.class::isInstance)
                    .map(FieldError.class::cast)
                    .collect(Collectors.toMap(
                            FieldError::getField,
                            e -> ApiResponse.ApiError.ErrorDetail.builder()
                                    .code(Objects.requireNonNull(e.getCodes())[e.getCodes().length - 1])
                                    .message(messageSource.getMessage(e, LocaleContextHolder.getLocale()))
                                    .build()));
        }
        return errors;
    }

}
