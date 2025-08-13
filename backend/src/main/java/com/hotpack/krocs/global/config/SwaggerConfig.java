package com.hotpack.krocs.global.config;

import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import com.hotpack.krocs.global.security.annotation.Login;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@ConditionalOnClass(OpenAPIDefinition.class) // springdoc 없으면 자동 무시
@OpenAPIDefinition(
        info = @Info(title = "Krocs API", version = "v1", description = "Krocs API 명세서"),
        security = {@SecurityRequirement(name = "bearerAuth")}
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SwaggerConfig {

    @Bean
    public OperationCustomizer hideLoginArguments() {
        return (operation, handlerMethod) -> {
            Set<String> loginParamNames = Arrays.stream(handlerMethod.getMethodParameters())
                    .filter(p -> p.hasParameterAnnotation(Login.class))
                    .map(p -> p.getParameterName())
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            if (operation.getParameters() != null && !loginParamNames.isEmpty()) {
                operation.setParameters(
                        operation.getParameters().stream()
                                .filter(p -> !loginParamNames.contains(p.getName()))
                                .collect(Collectors.toList())
                );
            }
            return operation;
        };
    }
}
