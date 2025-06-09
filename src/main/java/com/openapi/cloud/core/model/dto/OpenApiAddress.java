package com.openapi.cloud.core.model.dto;
import lombok.*;
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpenApiAddress {

    private String postalCode;
    private String addressLine;
}
