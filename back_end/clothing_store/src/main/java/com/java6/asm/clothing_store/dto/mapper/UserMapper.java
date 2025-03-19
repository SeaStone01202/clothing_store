package com.java6.asm.clothing_store.dto.mapper;

import com.java6.asm.clothing_store.dto.request.UserRegisterRequest;
import com.java6.asm.clothing_store.dto.response.UserRegisterResponse;
import com.java6.asm.clothing_store.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserRegisterResponse toResponse(User user);

    User toEntity(UserRegisterRequest registerRequest);
}
