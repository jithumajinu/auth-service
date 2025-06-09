package com.openapi.cloud.core.constants;

import org.apache.commons.lang3.StringUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AuthorizationTypePatterns {

    CUSTOMER_FUNCTION("CUSTOMER", "/api/customer/"),
    CUSTOMER_EDIT("customer_edit", "/api/customer/edit"),
    CUSTOMER_DELETE("customer_delete", "/api/customer/delete"),
    CUSTOMER_ADD("customer_add", "/api/customer/edit");

    @Getter
    private final String code;

    @Getter
    private final String apiCode;

    public String getPathPattern() {
        if (StringUtils.isNotBlank(this.code))
            return this.code + "**";
        return null;
    }

    public String getApiPathPattern() {
        if (StringUtils.isNotBlank(this.apiCode))
            return this.apiCode + "**";
        return null;
    }
}
