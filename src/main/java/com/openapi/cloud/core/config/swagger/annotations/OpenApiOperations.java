package com.openapi.cloud.core.config.swagger.annotations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface OpenApiOperations {

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            responses = {
                    @ApiResponse(responseCode = "200", content = {
                            @Content(mediaType = "application/json", array = @ArraySchema(
                                    schema = @Schema(implementation = com.acid.core.web.model.ApiResponse.class)))
                    }),
                    @ApiResponse(responseCode = "401", ref = "unauthorizedStatus"),
                    @ApiResponse(responseCode = "500", ref = "internalServerErrorStatus"),
            }
    )
    public @interface OpenApiListOperation {
        String tags() default "openapi";

        String summary() default "summary";

        String description() default "description";

        String operationId() default "operationId";

        boolean isPaginated() default false;

        boolean sortable() default false;

        String[] additionalTags() default {};
    }


    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            responses = {
                    @ApiResponse(responseCode = "200", content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = com.acid.core.web.model.ApiResponse.class))
                    }),
                    @ApiResponse(responseCode = "401", ref = "unauthorizedStatus"),
                    @ApiResponse(responseCode = "500", ref = "internalServerErrorStatus"),
            }
    )
    public @interface OpenApiObjectOperation {
        String tags() default "openapi";

        String summary() default "summary";

        String description() default "description";

        String operationId() default "operationId";

        boolean isPaginated() default false;

        boolean sortable() default false;

        String[] additionalTags() default {};
    }

}
