package com.hotpack.krocs.domain.auth.dto;

import com.hotpack.krocs.domain.user.domain.enums.UserRole;
import java.io.Serializable;
import java.util.Collections;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserSession implements Serializable {
    private final String userId;
    private final String displayName;
    private final Set<UserRole> roles;

    public static UserSession of(String userId, String displayName, Set<UserRole> roles) {
        return UserSession.builder()
                .userId(userId)
                .displayName(displayName)
                .roles(roles == null ? Collections.emptySet() : roles)
                .build();
    }
}

