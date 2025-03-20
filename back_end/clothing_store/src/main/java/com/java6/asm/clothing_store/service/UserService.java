package com.java6.asm.clothing_store.service;

import com.java6.asm.clothing_store.dto.request.UserRegisterRequest;
import com.java6.asm.clothing_store.dto.response.SystemUserRegisterResponse;

import java.util.List;

public interface UserService {

    SystemUserRegisterResponse createUser(UserRegisterRequest userRegisterRequest);

    SystemUserRegisterResponse updateUser(UserRegisterRequest userRegisterRequest, Integer id);

    void deleteUser(Integer userId);

    List<SystemUserRegisterResponse> retrieveAllUsers();

    SystemUserRegisterResponse retrieveUserById(Integer userId);

}
