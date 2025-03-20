package com.java6.asm.clothing_store.service.authentication;

import com.java6.asm.clothing_store.constance.TypeAccountEnum;
import com.java6.asm.clothing_store.dto.response.GoogleUserRegisterResponse;

public interface OAuthGoogleUserService {

    GoogleUserRegisterResponse findOrCreateUser(String email, String name, String image, TypeAccountEnum type);

    GoogleUserRegisterResponse getUserByEmail(String email);

}
