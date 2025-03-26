package com.java6.asm.clothing_store.service.implement;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.java6.asm.clothing_store.configuration.CloudinaryConfig;
import com.java6.asm.clothing_store.constance.RoleEnum;
import com.java6.asm.clothing_store.constance.StatusEnum;
import com.java6.asm.clothing_store.constance.TypeAccountEnum;
import com.java6.asm.clothing_store.dto.mapper.UserResponseMapper;
import com.java6.asm.clothing_store.dto.mapper.UserUpdateRequestMapper;
import com.java6.asm.clothing_store.dto.request.UserRegisterRequest;
import com.java6.asm.clothing_store.dto.request.UserRequest;
import com.java6.asm.clothing_store.dto.request.UserUpdateRequest;
import com.java6.asm.clothing_store.dto.response.UserResponse;
import com.java6.asm.clothing_store.entity.User;
import com.java6.asm.clothing_store.exception.AppException;
import com.java6.asm.clothing_store.exception.ErrorCode;
import com.java6.asm.clothing_store.repository.UserRepository;
import com.java6.asm.clothing_store.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserResponseMapper userResponseMapper;

    private final UserUpdateRequestMapper userUpdateRequestMapper;

    private final Cloudinary cloudinary;

    @Transactional
    @Override
    public UserResponse createUser(UserRegisterRequest request) {

        UserResponse checkedUser = retrieveUserByEmail(request.getEmail());

        if (checkedUser != null) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        User newUser = User.builder()
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .fullname(request.getFullname())
                .role(RoleEnum.CUSTOMER)
                .status(StatusEnum.ACTIVE)
                .type(TypeAccountEnum.SYSTEM)
                .createdAt(LocalDate.now())
                .build();
        return userResponseMapper.toResponse(userRepository.save(newUser));
    }

    @Override
    public UserResponse updateUser(UserUpdateRequest request) {
        // Tìm user hiện tại
        User user = userRepository.findByEmailAndStatus(request.getEmail(), StatusEnum.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Cập nhật thông tin từ request, chỉ thay đổi nếu có giá trị mới
        if (request.getFullname() != null && !request.getFullname().isEmpty()) {
            user.setFullname(request.getFullname().trim());
        }
        if (request.getPhone() != null && !request.getPhone().isEmpty()) {
            user.setPhone(request.getPhone().trim());
        }

        // Nếu có file ảnh trong request, upload lên Cloudinary
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            try {
                Map uploadResult = cloudinary.uploader().upload(request.getImage().getBytes(), ObjectUtils.emptyMap());
                String imageUrl = (String) uploadResult.get("secure_url");
                user.setImage(imageUrl); // Lưu URL ảnh vào entity User
            } catch (IOException e) {
                throw new RuntimeException("Không thể tải ảnh lên Cloudinary", e);
            }
        }

        // Lưu thay đổi vào database
        User updatedUser = userRepository.save(user);
        return userResponseMapper.toResponse(updatedUser);
    }

    @Override
    public void deleteUser(Integer userId) {

    }

    @Override
    public List<UserResponse> retrieveAllUsers() {
        return List.of();
    }

    @Override
    public UserResponse retrieveUserByEmail(String email) {
        User user = userRepository.findByEmailAndStatus(email, StatusEnum.ACTIVE).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userResponseMapper.toResponse(user);
    }
}
