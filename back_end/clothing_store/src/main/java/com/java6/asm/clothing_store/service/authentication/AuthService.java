package com.java6.asm.clothing_store.service.authentication;

import com.java6.asm.clothing_store.constance.TypeAccountEnum;
import com.java6.asm.clothing_store.dto.response.UserRegisterResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {

    private final OAuthUserService oAuthUserService;

    /**
     * ✅ Đăng nhập bằng tài khoản hệ thống (username, password)
     */
    public Optional<UserRegisterResponse> authenticateSystemUser(String username) {
        return oAuthUserService.getUserByUsername(username);
    }

    /**
     * ✅ Đăng nhập bằng Google
     */
    public Optional<UserRegisterResponse> authenticateGoogleUser(OAuth2User oauth2User) {
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");
        return oAuthUserService.findOrCreateUser(email, name, TypeAccountEnum.GOOGLE);
    }

    /**
     * ✅ Đăng nhập bằng Zalo
     */
    public Optional<UserRegisterResponse> authenticateZaloUser(String externalId, String name) {
        return oAuthUserService.findOrCreateUser(externalId, name, TypeAccountEnum.ZALO);
    }
}