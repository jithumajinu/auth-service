package com.openapi.cloud.core.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum  OpenApiStatusEnum {
    PREMIUM("PREMIUM"),
    NORMAL("NORMAL");
    private final String value;
}
