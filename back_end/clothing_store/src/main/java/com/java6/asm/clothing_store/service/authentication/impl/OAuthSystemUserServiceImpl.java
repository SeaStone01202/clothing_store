package com.java6.asm.clothing_store.service.authentication.impl;

import com.java6.asm.clothing_store.dto.mapper.UserResponseMapper;
import com.java6.asm.clothing_store.dto.request.UserRequest;
import com.java6.asm.clothing_store.dto.response.AuthResponse;
import com.java6.asm.clothing_store.dto.response.UserResponse;
import com.java6.asm.clothing_store.exception.AppException;
import com.java6.asm.clothing_store.exception.ErrorCode;
import com.java6.asm.clothing_store.repository.UserRepository;
import com.java6.asm.clothing_store.service.authentication.AccessTokenService;
import com.java6.asm.clothing_store.service.authentication.DeviceManagementService;
import com.java6.asm.clothing_store.service.authentication.OAuthSystemUserService;
import com.java6.asm.clothing_store.service.authentication.RefreshTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@AllArgsConstructor
public class OAuthSystemUserServiceImpl implements OAuthSystemUserService {

    private final UserRepository userRepository;

    private final UserResponseMapper userResponseMapper;

    private final PasswordEncoder passwordEncoder;

    private final AccessTokenService jwtAccessTokenService;

    private final RefreshTokenService jwtRefreshTokenService;

    private final DeviceManagementService deviceManagementService;
    /**
     * ✅ Đăng nhập & tạo Access Token + Refresh Token
     */
    @Override
    public AuthResponse authenticateAndGenerateTokens(UserRequest request, HttpServletResponse response) {

        if (request.getEmail().isEmpty()) {
            throw new AppException(ErrorCode.EMAIL_INVALID);
        }

        if (request.getPassword().isEmpty()) {
            throw new AppException(ErrorCode.PASSWORD_INVALID);
        }

        UserResponse userChecked = userRepository
                .findByEmail(request.getEmail())
                .filter(user -> passwordEncoder.matches(request.getPassword(), user.getPassword()))
                .map(userResponseMapper::toResponse)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        String email = userChecked.getEmail();

        String deviceId = request.getDeviceId();

        String refreshToken = deviceManagementService.getOrGenerateRefreshToken(email, deviceId);

        String accessToken = jwtAccessTokenService.generateToken(email);

        Cookie refreshTokenCookie = createRefreshTokenCookie(refreshToken);

        response.addCookie(refreshTokenCookie);

        return AuthResponse.builder().accessToken(accessToken).build();
    }

    /**
     * ✅ Làm mới Access Token từ Refresh Token
     */
    @Override
    public AuthResponse refreshAccessToken(String refreshToken) {

        String email = Optional
                .ofNullable(jwtRefreshTokenService
                        .validateToken(refreshToken))
                .orElseThrow(() -> new AppException(ErrorCode.REFRESH_TOKEN_INVALID));


        String newAccessToken = userRepository.findByEmail(email)
                .map(user -> jwtAccessTokenService.generateToken(user.getEmail()))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return AuthResponse.builder().accessToken(newAccessToken).build();
    }

    /**
     * ✅ Đăng xuất
     */
    @Override
    public String logout(String refreshToken, HttpServletResponse response) {
        boolean deleted = deviceManagementService.removeRefreshToken(refreshToken);

        if (deleted) {
            Cookie expiredCookie = createExpiredRefreshTokenCookie();
            response.addCookie(expiredCookie);
            return "Đăng xuất thành công";
        }

        throw new AppException(ErrorCode.REFRESH_TOKEN_INVALID);
    }

    @Override
    public UserResponse getUser(String accessToken) {

        if (accessToken == null || accessToken.isBlank()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED_EXCEPTION);
        }

        String email = jwtAccessTokenService.validateToken(accessToken);

        if (email == null) {
            throw new AppException(ErrorCode.UNAUTHENTICATED_EXCEPTION);
        }

        return userRepository.findByEmail(email)
                .map(userResponseMapper::toResponse)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }


    // ===================== HÀM HỖ TRỢ =====================

    /**
     * ✅ Tạo Cookie chứa Refresh Token
     */
    private Cookie createRefreshTokenCookie(String refreshToken) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        return cookie;
    }

    /**
     * ✅ Tạo Cookie rỗng để xóa Refresh Token
     */
    private Cookie createExpiredRefreshTokenCookie() {
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        return cookie;
    }
}