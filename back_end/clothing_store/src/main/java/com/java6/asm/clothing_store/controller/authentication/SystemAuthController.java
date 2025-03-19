package com.java6.asm.clothing_store.controller.authentication;

import com.java6.asm.clothing_store.dto.ApiResponse;
import com.java6.asm.clothing_store.dto.request.AuthRequest;
import com.java6.asm.clothing_store.dto.response.AuthResponse;
import com.java6.asm.clothing_store.dto.request.RefreshTokenRequest;
import com.java6.asm.clothing_store.dto.request.UserRegisterRequest;
import com.java6.asm.clothing_store.dto.response.UserRegisterResponse;
import com.java6.asm.clothing_store.service.UserService;
import com.java6.asm.clothing_store.service.authentication.AuthService;
import com.java6.asm.clothing_store.service.authentication.token.JwtAccessTokenService;
import com.java6.asm.clothing_store.service.authentication.token.JwtRefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth/system")
@AllArgsConstructor
public class SystemAuthController {

    private final JwtRefreshTokenService jwtRefreshTokenService;
    private final JwtAccessTokenService jwtAccessTokenService;
    private final AuthService authService;
    private final UserService userService;
    private PasswordEncoder passwordEncoder;

    /**
     * ✅ API đăng nhập (`/auth/system/login`)
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody AuthRequest request) {

        Optional<UserRegisterResponse> user = authService.authenticateSystemUser(request.getUsername());

        if (user.isEmpty() || !passwordEncoder.matches(request.getPassword(), user.get().getPassword())) {
            return ResponseEntity.ok(ApiResponse.error(401, "Sai tài khoản hoặc mật khẩu!"));
        }

        String accessToken = jwtAccessTokenService.generateToken(request.getUsername());

        String refreshToken = jwtRefreshTokenService.generateToken(request.getUsername());

        return ResponseEntity.ok(ApiResponse.success(new AuthResponse(accessToken, refreshToken)));
    }

    /**
     * ✅ API làm mới Access Token (`/auth/system/refresh`)
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        String username = jwtRefreshTokenService.validateToken(refreshTokenRequest.getRefreshToken());

        if (username == null) {
            return ResponseEntity.ok(ApiResponse.error(401, "Refresh Token không hợp lệ!"));
        }

        String newAccessToken = jwtAccessTokenService.generateToken(username);
        return ResponseEntity.ok(ApiResponse.success(new AuthResponse(newAccessToken, refreshTokenRequest.getRefreshToken())));
    }

    /**
     * ✅ API đăng xuất (`/auth/system/logout`)
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        String username = jwtRefreshTokenService.validateToken(refreshTokenRequest.getRefreshToken());

        if (username == null) {
            return ResponseEntity.ok(ApiResponse.error(401, "Refresh Token không hợp lệ!"));
        }

        jwtRefreshTokenService.deleteToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.ok(ApiResponse.success("Đăng xuất thành công"));
    }

    /**
     * ✅ API đăng ký tài khoản hệ thống (`/auth/system/register`)
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserRegisterResponse>> register(@RequestBody UserRegisterRequest request) {
        Optional<UserRegisterResponse> existingUser = authService.authenticateSystemUser(request.getUsername());

        if (existingUser.isPresent()) {
            return ResponseEntity.ok(ApiResponse.error(409, "Tài khoản đã tồn tại!"));
        }

        // 🔄 Tạo tài khoản mới
        UserRegisterResponse newUser = userService.createUser(request);
        return ResponseEntity.ok(ApiResponse.success(newUser));
    }
}
