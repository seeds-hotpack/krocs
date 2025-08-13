package com.hotpack.krocs.global.security.auth;

import com.hotpack.krocs.domain.auth.dto.UserSession;
import com.hotpack.krocs.domain.auth.service.RedisTokenService;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenAuthenticationProvider implements AuthenticationProvider {

    private final RedisTokenService redisTokenService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!(authentication instanceof TokenAuthentication)) {
            return null;
        }
        String rawToken = Objects.toString(authentication.getPrincipal(), null);
        UserSession session = redisTokenService.findUserByToken(rawToken)
                .orElseThrow(() -> new BadCredentialsException("Invalid token"));

        var authorities = session.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toSet());

        return TokenAuthentication.authenticated(session, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return TokenAuthentication.class.isAssignableFrom(authentication);
    }
}

