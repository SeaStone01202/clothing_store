package com.java6.asm.clothing_store.controller.authentication;

import com.java6.asm.clothing_store.service.authentication.impl.JwtAccessRefreshTokenService;
import com.java6.asm.clothing_store.service.authentication.impl.JwtRefreshRefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/google")
@RequiredArgsConstructor
public class GoogleAuthController {

    private final JwtAccessRefreshTokenService jwtAccessTokenService;
    private final JwtRefreshRefreshTokenService jwtRefreshTokenService;

//    @GetMapping("/success")
//    public ResponseEntity<ApiResponse<AuthResponse>> googleLoginSuccess(@AuthenticationPrincipal OAuth2User oauth2User) {
//
//        GoogleUserRegisterResponse user = authService.authenticateGoogleUser(oauth2User);
//
//        String accessToken = jwtAccessTokenService.generateToken(user.getEmail());
//
//        String refreshToken = jwtRefreshTokenService.generateToken(user.getEmail());
//
//        return ResponseEntity.ok(ApiResponse.success(new AuthResponse(accessToken, refreshToken)));
//    }
}
