package com.java6.asm.clothing_store.controller.authentication;

import com.java6.asm.clothing_store.dto.ApiResponse;
import com.java6.asm.clothing_store.dto.response.AuthResponse;
import com.java6.asm.clothing_store.service.authentication.token.JwtAccessTokenService;
import com.java6.asm.clothing_store.service.authentication.token.JwtRefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/google")
@AllArgsConstructor
public class GoogleAuthController {

    private final JwtRefreshTokenService jwtRefreshTokenService;

    private final JwtAccessTokenService jwtAccessTokenService;

    @GetMapping("/callback")
    public ResponseEntity<ApiResponse<AuthResponse>> googleLogin(@AuthenticationPrincipal OAuth2User oauth2User) {
        String email = oauth2User.getAttribute("email");

        // Kiểm tra user đã tồn tại chưa
//        Optional<User> userOptional = authService.findByUsername(email);
//        User user = userOptional.orElseGet(() -> {
//            User newUser = new User();
//            newUser.setUsername(email);
//            newUser.setEmail(email);
//            newUser.setProvider("GOOGLE");
//            authService.save(newUser);
//            return newUser;
//        });

        // 🔑 Tạo Access Token & Refresh Token
        String accessToken = jwtAccessTokenService.generateToken(email);
        String refreshToken = jwtRefreshTokenService.generateToken(email);

        return ResponseEntity.ok(ApiResponse.success(new AuthResponse(accessToken, refreshToken)));
    }
}
