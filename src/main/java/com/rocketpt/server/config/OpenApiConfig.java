package com.rocketpt.server.config;

import com.rocketpt.server.common.Constants;

import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.media.StringSchema;

/**
 * @author plexpt
 */
//@Configuration
//@ConditionalOnProperty(havingValue = "")
@OpenAPIDefinition(info = @Info(title = "Rocket PT",
        description = "A Flexible and Efficient private tracker",
        version = "v1"))
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer"
)
public class OpenApiConfig {

    @Bean
    public OpenAPI getOpenAPI() {
        return new OpenAPI().components(new Components()
                .addHeaders(Constants.TOKEN_HEADER_NAME,
                        new Header().description("Auth header").schema(new StringSchema())));
    }

}
