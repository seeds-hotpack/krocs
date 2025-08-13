package com.hotpack.krocs.global.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotpack.krocs.domain.auth.dto.UserSession;
import com.hotpack.krocs.domain.user.domain.enums.UserRole;
import jakarta.annotation.PostConstruct;
import java.time.Duration;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Profile({"dev", "local"})
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "auth.dev", name = "bootstrap-token", matchIfMissing = false)
public class DevTokenBootstrap {

    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    @Value("${auth.dev.bootstrap-token:}")
    private String bootstrapToken;

    @Value("${auth.dev.user.id:1}")
    private String bootstrapUserId;

    @Value("${auth.dev.user.name:Developer}")
    private String bootstrapUserName;

    @Value("${auth.dev.user.role:USER}")
    private String bootstrapUserRole;

    @PostConstruct
    public void init() {
        if (bootstrapToken == null || bootstrapToken.isBlank()) return;
        UserRole role;
        try {
            role = UserRole.valueOf(bootstrapUserRole.toUpperCase());
        } catch (Exception e) {
            role = UserRole.USER;
        }

        UserSession session = UserSession.of(bootstrapUserId, bootstrapUserName, Set.of(role));
        try {
            String json = objectMapper.writeValueAsString(session);
            stringRedisTemplate.opsForValue().set("auth:token:" + bootstrapToken, json, Duration.ofDays(7));
        } catch (JsonProcessingException e) {
            // ignore
        }
    }
}

