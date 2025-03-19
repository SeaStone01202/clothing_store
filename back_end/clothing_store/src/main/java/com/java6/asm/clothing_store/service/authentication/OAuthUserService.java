package com.java6.asm.clothing_store.service.authentication;

import com.java6.asm.clothing_store.constance.TypeAccountEnum;
import com.java6.asm.clothing_store.dto.response.UserRegisterResponse;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface OAuthUserService {

    Optional<UserRegisterResponse> findOrCreateUser(String externalId, String name, TypeAccountEnum type);

    Optional<UserRegisterResponse> getUserByUsername(String username);
}