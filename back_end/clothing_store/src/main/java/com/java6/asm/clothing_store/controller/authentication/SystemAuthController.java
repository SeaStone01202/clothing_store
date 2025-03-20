package com.java6.asm.clothing_store.controller.authentication;

import com.java6.asm.clothing_store.dto.ApiResponse;
import com.java6.asm.clothing_store.dto.request.UserRequest;
import com.java6.asm.clothing_store.dto.response.AuthResponse;
import com.java6.asm.clothing_store.dto.request.UserRegisterRequest;
import com.java6.asm.clothing_store.dto.response.SystemUserRegisterResponse;
import com.java6.asm.clothing_store.dto.response.UserResponse;
import com.java6.asm.clothing_store.exception.AppException;
import com.java6.asm.clothing_store.exception.ErrorCode;
import com.java6.asm.clothing_store.service.UserService;
import com.java6.asm.clothing_store.service.authentication.OAuthSystemUserService;
import com.java6.asm.clothing_store.service.authentication.impl.JwtAccessRefreshTokenService;
import com.java6.asm.clothing_store.service.authentication.impl.JwtRefreshRefreshTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/system")
@AllArgsConstructor
public class SystemAuthController {

    private final OAuthSystemUserService oAuthSystemUserService;

    /**
     * ✅ API đăng nhập (`/auth/system/login`)
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody UserRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(ApiResponse.success(oAuthSystemUserService.authenticateAndGenerateTokens(request, response)));
    }

    /**
     * ✅ API làm mới Access Token (`/auth/system/refresh`)
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(
            @CookieValue(value = "refreshToken", required = false) String refreshToken) {
       return ResponseEntity.ok(ApiResponse.success(oAuthSystemUserService.refreshAccessToken(refreshToken)));
    }

    /**
     * ✅ API đăng xuất (`/auth/system/logout`)
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(
            @CookieValue(value = "refreshToken", required = false) String refreshToken, HttpServletResponse response) {
        return ResponseEntity.ok(ApiResponse.success(oAuthSystemUserService.logout(refreshToken, response)));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getSystemUser(
            @RequestHeader( value = "Authorization", required = false) String authorizationHeader) {

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.ok(ApiResponse.error(new AppException(ErrorCode.ACCESS_TOKEN_INVALID)));
        }

        String accessToken = authorizationHeader.replace("Bearer ", "");

        return ResponseEntity.ok(ApiResponse.success(oAuthSystemUserService.getUser(accessToken)));
    }

}
