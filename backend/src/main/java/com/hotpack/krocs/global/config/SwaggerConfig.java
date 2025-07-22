package com.hotpack.krocs.global.config;

import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;

@Configuration
@ConditionalOnClass(OpenAPIDefinition.class) // springdoc 없으면 자동 무시
@OpenAPIDefinition(
        info = @Info(title = "Krocs API", version = "v1", description = "Krocs API 명세서")
)
public class SwaggerConfig {
}
