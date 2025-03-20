package com.java6.asm.clothing_store.service.authentication.impl;

import com.java6.asm.clothing_store.constance.RoleEnum;
import com.java6.asm.clothing_store.constance.StatusEnum;
import com.java6.asm.clothing_store.constance.TypeAccountEnum;
import com.java6.asm.clothing_store.dto.mapper.GoogleUserMapper;
import com.java6.asm.clothing_store.dto.response.GoogleUserRegisterResponse;
import com.java6.asm.clothing_store.entity.User;
import com.java6.asm.clothing_store.repository.UserRepository;
import com.java6.asm.clothing_store.service.authentication.OAuthGoogleUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OAuthGoogleUserServiceImpl implements OAuthGoogleUserService {

    private final UserRepository userRepository;
    private final GoogleUserMapper googleUserMapper;

    /**
     * ✅ Tìm user theo username (tài khoản hệ thống)
     */
    @Override
    public GoogleUserRegisterResponse getUserByEmail(String email) {
        return userRepository.findByEmail(email).map(googleUserMapper::toResponse).orElseThrow();
    }

    /**
     * ✅ Tìm hoặc tạo user mới khi đăng nhập bằng OAuth (Google, Zalo...)
     */
    @Override
    public GoogleUserRegisterResponse findOrCreateUser(String email, String name, String image, TypeAccountEnum type) {
        return userRepository.findByEmail(email)
                .map(googleUserMapper::toResponse) // Nếu user đã có, trả về DTO
                .orElseGet(() -> { // 🔥 Nếu chưa có, tạo user mới
                    GoogleUserRegisterResponse newUser = createUser(email, name, image, type);
                    return newUser;
                });
    }


    /**
     * ✅ Tạo user mới
     */
    private GoogleUserRegisterResponse createUser(String email, String name, String image, TypeAccountEnum type) {
        User newUser = User.builder()
                .fullname(name)
                .email(email)
                .image(image)
                .type(type)
                .role(RoleEnum.CUSTOMER)
                .status(StatusEnum.ACTIVE)
                .build();

        userRepository.save(newUser);
        return googleUserMapper.toResponse(newUser);
    }

}