package com.openapi.cloud.core.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import java.util.List;

@OpenAPIDefinition
@Configuration
public class OpenApiConfig {

        @Value("${openapi.url}")
        private String openAPIUrl;

        @Value("${openapi.env}")
        private String openAPIEnv;

        @Bean
        public OpenAPI baseOpenAPI() {

                Server openApiServer = new Server();
                openApiServer.setUrl(openAPIUrl);
                openApiServer.setDescription(openAPIEnv);

                Info info = new Info().title("Spring Doc").version("1.0.0");

                /*
                 * Client error
                 * status code 400 | Bad Request
                 */
                ApiResponse badRequestStatus = new ApiResponse().content(
                                new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                                                new io.swagger.v3.oas.models.media.MediaType().addExamples("badRequest",
                                                                new Example().value("400"))))
                                .description("Bad Request!");

                /*
                 * Client error
                 * status code 401 | Unauthorized
                 */
                ApiResponse unauthorizedStatus = new ApiResponse().content(
                                new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                                                new io.swagger.v3.oas.models.media.MediaType().addExamples("Unauthorized",
                                                                new Example().value("401"))))
                                .description("Unauthorized!");

                /*
                 * Server error
                 * status code 500 | Internal Server Error
                 */
                ApiResponse internalServerErrorStatus = new ApiResponse().content(
                                new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                                                new io.swagger.v3.oas.models.media.MediaType().addExamples("Internal Server Error",
                                                                new Example().value("500"))))
                                .description("Internal Server Error!");

                /*
                 * Define Security Scheme
                 * This is used to secure the API with JWT Bearer token
                 */
                Components components = new Components()
                                .addSecuritySchemes("Bearer Authentication",
                                                new SecurityScheme()
                                                                .type(SecurityScheme.Type.HTTP)
                                                                .scheme("bearer")
                                                                .bearerFormat("JWT")
                                                                .description("Enter JWT token"));

                // Add responses to components
                components.addResponses("badRequestStatus", badRequestStatus);
                components.addResponses("unauthorizedStatus", unauthorizedStatus);
                components.addResponses("internalServerErrorStatus", internalServerErrorStatus); // Server error

                return new OpenAPI()
                                .components(components)
                                .info(info)
                                .servers(List.of(openApiServer));
        }
}
