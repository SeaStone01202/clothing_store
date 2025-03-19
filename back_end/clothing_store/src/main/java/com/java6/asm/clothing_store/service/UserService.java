package com.java6.asm.clothing_store.service;

import com.java6.asm.clothing_store.dto.request.UserRegisterRequest;
import com.java6.asm.clothing_store.dto.response.UserRegisterResponse;

import java.util.List;
import java.util.Objects;

public interface UserService {

    UserRegisterResponse createUser(UserRegisterRequest userRegisterRequest);

    UserRegisterResponse updateUser(UserRegisterRequest userRegisterRequest, Integer id);

    void deleteUser(Integer userId);

    List<UserRegisterResponse> retrieveAllUsers();

    UserRegisterResponse retrieveUserById(Integer userId);

}
