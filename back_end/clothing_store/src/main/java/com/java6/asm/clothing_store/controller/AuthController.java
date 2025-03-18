package com.java6.asm.clothing_store.controller;

import com.java6.asm.clothing_store.dto.AuthRequest;
import com.java6.asm.clothing_store.dto.AuthResponse;
import com.java6.asm.clothing_store.dto.RefreshTokenRequest;
import com.java6.asm.clothing_store.service.AuthService;
import com.java6.asm.clothing_store.service.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * ✅ Controller xử lý xác thực và quản lý JWT
 * - Cung cấp API đăng nhập (`/auth/login`)
 * - Cung cấp API làm mới Access Token (`/auth/refresh`)
 * - Cung cấp API đăng xuất (`/auth/logout`)
 */
@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final JwtEncoder jwtEncoder;

    private final RefreshTokenService refreshTokenService;

    private AuthService authService;


    /**
     * ✅ API đăng nhập (`/auth/login`)
     * - Kiểm tra username & password (tạm thời hardcode)
     * - Nếu đúng, tạo Access Token (5 phút) và Refresh Token (7 ngày)
     * - Trả về Access Token + Refresh Token cho client
     *
     * @param request Dữ liệu đăng nhập (username, password)
     * @return ResponseEntity chứa Access Token & Refresh Token nếu hợp lệ, hoặc 401 nếu sai
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        // Kiểm tra username & password (hardcode)
        if (authService.findByUsername(request.getUsername()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // 🔑 Tạo Access Token
        String accessToken = generateAccessToken(request.getUsername());

        // 🔄 Tạo Refresh Token (lưu vào Redis)
        String refreshToken = refreshTokenService.createRefreshToken(request.getUsername());

        // ✅ Trả về Access Token & Refresh Token
        return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken));
    }

    /**
     * ✅ API làm mới Access Token (`/auth/refresh`)
     * - Kiểm tra Refresh Token có hợp lệ không
     * - Nếu hợp lệ, cấp Access Token mới
     * - Nếu không, trả về lỗi 401
     *
     * @param refreshTokenRequest Dữ liệu chứa Refresh Token
     * @return ResponseEntity chứa Access Token mới hoặc 401 nếu Refresh Token không hợp lệ
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        // 🔄 Kiểm tra xem Refresh Token có hợp lệ không
        String username = refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());

        // ❌ Nếu Refresh Token không hợp lệ → Trả về lỗi 401
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // 🔑 Tạo Access Token mới
        String newAccessToken = generateAccessToken(username);

        // ✅ Trả về Access Token mới & giữ nguyên Refresh Token cũ
        return ResponseEntity.ok(new AuthResponse(newAccessToken, refreshTokenRequest.getRefreshToken()));
    }

    /**
     * ✅ API đăng xuất (`/auth/logout`)
     * - Xóa Refresh Token khỏi Redis để vô hiệu hóa nó
     *
     * @param refreshTokenRequest Dữ liệu chứa Refresh Token cần xóa
     * @return Thông báo đăng xuất thành công
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        // 🔄 Xóa Refresh Token khỏi Redis
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());

        // ✅ Trả về thông báo đăng xuất thành công
        return ResponseEntity.ok("Logged out successfully");
    }

    /**
     * 🔑 Tạo Access Token bằng RSA Private Key
     * - Thời gian sống: 5 phút
     * - Chứa thông tin user & role
     *
     * @param username Tên user để gán vào JWT
     * @return Chuỗi Access Token đã ký
     */
    private String generateAccessToken(String username) {
        Instant now = Instant.now();
        return jwtEncoder.encode(JwtEncoderParameters.from(
                JwsHeader.with(SignatureAlgorithm.RS256).build(),
                JwtClaimsSet.builder()
                        .subject(username) // Ai đang đăng nhập?
                        .issuedAt(now) // Thời điểm tạo token
                        .expiresAt(now.plus(1, ChronoUnit.MINUTES)) // Hết hạn sau 5 phút
                        .claim("role", "ADMIN") // Gán quyền cho user
                        .build()
        )).getTokenValue();
    }
}
