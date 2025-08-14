package com.hotpack.krocs.domain.auth.controller;

import com.hotpack.krocs.domain.auth.dto.UserSession;
import com.hotpack.krocs.global.common.response.ApiResponse;
import com.hotpack.krocs.global.security.annotation.Login;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthTestController {

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserSession>> me(@Login UserSession userSession) {
        if (userSession == null) {
            return ResponseEntity.status(401).body(ApiResponse.onFailure("GLOBAL401", "인증 실패"));
        }
        return ResponseEntity.ok(ApiResponse.success(userSession));
    }
}

