package com.java6.asm.clothing_store.dto.mapper;

import com.java6.asm.clothing_store.dto.request.UserRegisterRequest;
import com.java6.asm.clothing_store.dto.response.SystemUserRegisterResponse;
import com.java6.asm.clothing_store.dto.response.UserResponse;
import com.java6.asm.clothing_store.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserResponseMapper {

    UserResponse toResponse(User user);

    User toEntity(UserResponse response);
}
