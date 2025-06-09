package com.openapi.cloud.core.model.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.openapi.cloud.core.model.ValidationGroups;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest implements Serializable {

    private static final long serialVersionUID = 1200160788658364366L;

    @JsonIgnore
    private Long id;
    // Long provides a much larger range of values:
    // Integer max value: 2,147,483,647 (2^31 - 1)
    // Long max value: 9,223,372,036,854,775,807 (2^63 - 1)

    @NotBlank(groups = ValidationGroups.Create.class, message = "{product.name.required}")
    @Size(min = 3, max = 100, message = "{product.name.size}", groups = ValidationGroups.Update.class)
    private String name;

    @NotNull(message = "Description cannot be null")
    @Size(min = 3, max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "Model cannot be null")
    @Size(max = 50, message = "Model cannot exceed 50 characters")
    private String model;

}
