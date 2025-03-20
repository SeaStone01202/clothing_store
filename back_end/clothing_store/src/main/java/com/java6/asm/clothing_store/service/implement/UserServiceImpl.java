package com.java6.asm.clothing_store.service.implement;

import com.java6.asm.clothing_store.constance.RoleEnum;
import com.java6.asm.clothing_store.constance.StatusEnum;
import com.java6.asm.clothing_store.constance.TypeAccountEnum;
import com.java6.asm.clothing_store.dto.mapper.SystemUserMapper;
import com.java6.asm.clothing_store.dto.request.UserRegisterRequest;
import com.java6.asm.clothing_store.dto.response.SystemUserRegisterResponse;
import com.java6.asm.clothing_store.entity.User;
import com.java6.asm.clothing_store.repository.UserRepository;
import com.java6.asm.clothing_store.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final SystemUserMapper systemUserMapper;

    @Transactional
    @Override
    public SystemUserRegisterResponse createUser(UserRegisterRequest request) {

        User newUser = User.builder()
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .fullname(request.getFullname())
                .role(RoleEnum.CUSTOMER)
                .status(StatusEnum.ACTIVE)
                .type(TypeAccountEnum.SYSTEM)
                .createdAt(LocalDate.now())
                .build();
        return systemUserMapper.toResponse(userRepository.save(newUser));
    }

    @Override
    public SystemUserRegisterResponse updateUser(UserRegisterRequest userRegisterRequest, Integer id) {
        return null;
    }

    @Override
    public void deleteUser(Integer userId) {

    }

    @Override
    public List<SystemUserRegisterResponse> retrieveAllUsers() {
        return List.of();
    }

    @Override
    public SystemUserRegisterResponse retrieveUserById(Integer userId) {
        return null;
    }
}
