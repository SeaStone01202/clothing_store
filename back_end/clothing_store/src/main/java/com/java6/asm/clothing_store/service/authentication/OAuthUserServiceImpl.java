package com.java6.asm.clothing_store.service.authentication;

import com.java6.asm.clothing_store.constance.RoleEnum;
import com.java6.asm.clothing_store.constance.StatusEnum;
import com.java6.asm.clothing_store.constance.TypeAccountEnum;
import com.java6.asm.clothing_store.dto.mapper.UserMapper;
import com.java6.asm.clothing_store.dto.response.UserRegisterResponse;
import com.java6.asm.clothing_store.entity.User;
import com.java6.asm.clothing_store.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Primary
public class OAuthUserServiceImpl implements OAuthUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * ✅ Tìm user theo username (tài khoản hệ thống)
     */
    @Override
    public Optional<UserRegisterResponse> getUserByUsername(String username) {
        return userRepository.findByUsername(username).map(userMapper::toResponse);
    }

    /**
     * ✅ Tìm hoặc tạo user mới khi đăng nhập bằng OAuth (Google, Zalo...)
     */
    @Override
    public Optional<UserRegisterResponse> findOrCreateUser(String externalId, String name, TypeAccountEnum type) {
        return userRepository.findByUsername(externalId).map(userMapper::toResponse).or(() -> Optional.ofNullable(createUser(externalId, name, type)));
    }

    /**
     * ✅ Tạo user mới
     */
    private UserRegisterResponse createUser(String externalId, String name, TypeAccountEnum type) {
        User newUser = User.builder()
                .username(externalId)
                .fullname(name)
                .type(type)
                .role(RoleEnum.CUSTOMER)
                .status(StatusEnum.ACTIVE)
                .build();

        userRepository.save(newUser);
        return userMapper.toResponse(newUser);
    }

}