package com.openapi.cloud.core.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.openapi.cloud.core.model.enums.OpenApiStatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpenApiRequest implements Serializable {

    private static final long serialVersionUID = 178378389798378973L;

    @JsonIgnore
    private Long customerId;

    @JsonProperty("name")
    @NotBlank
    @Size(min = 1, max = 5)
    private String name;

    @JsonProperty("dateOfBirth")
    @NotBlank
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$")
    private LocalDate dateOfBirth;

    @JsonProperty("statusType")
    private OpenApiStatusEnum statusType;
}
