package com.java6.asm.clothing_store.controller;

import com.java6.asm.clothing_store.dto.ApiResponse;
import com.java6.asm.clothing_store.dto.request.UserRegisterRequest;
import com.java6.asm.clothing_store.dto.request.UserUpdateRequest;
import com.java6.asm.clothing_store.dto.response.UserResponse;
import com.java6.asm.clothing_store.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> registerUser(@Valid @RequestBody UserRegisterRequest userRequest) {
        return ResponseEntity.ok(ApiResponse.success(userService.createUser(userRequest)));
    }

    @PutMapping("/update")
    public ResponseEntity<UserResponse> updateUser(@ModelAttribute UserUpdateRequest request) {
        UserResponse response = userService.updateUser(request);
        return ResponseEntity.ok(response);
    }

}
