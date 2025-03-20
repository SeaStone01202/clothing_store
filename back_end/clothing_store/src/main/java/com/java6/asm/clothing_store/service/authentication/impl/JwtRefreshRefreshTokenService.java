package com.java6.asm.clothing_store.service.authentication.impl;

import com.java6.asm.clothing_store.exception.AppException;
import com.java6.asm.clothing_store.exception.ErrorCode;
import com.java6.asm.clothing_store.service.authentication.RefreshTokenService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class JwtRefreshRefreshTokenService implements RefreshTokenService {

    private final RedisTemplate<String, String> redisTemplate;
    private static final long REFRESH_TOKEN_TTL = 7 * 24 * 60 * 60; // ⏳ TTL: 7 ngày

    public JwtRefreshRefreshTokenService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String generateToken(String email, String deviceId) {
        // 📌 Tạo key Redis theo format "refreshToken:<email>:<deviceId>"
        String redisKey = "refreshToken:" + email + ":" + deviceId;

        // 📌 Nếu deviceId đã tồn tại, trả về refresh token cũ
        String existingToken = redisTemplate.opsForValue().get(redisKey);
        if (existingToken != null) {
            return existingToken; // ✅ Giữ nguyên refresh token cũ
        }

        // 📌 Kiểm tra số lượng thiết bị đăng nhập
        Set<String> existingDevices = redisTemplate.keys("refreshToken:" + email + ":*");
        if (existingDevices != null && existingDevices.size() >= 3) {
            throw new AppException(ErrorCode.TOO_MANY_DEVICES);
        }

        // 📌 Nếu deviceId chưa tồn tại, tạo refresh token mới
        String refreshToken = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(redisKey, refreshToken, REFRESH_TOKEN_TTL, TimeUnit.SECONDS);

        return refreshToken;
    }


    public String validateToken(String refreshToken) {
        // 📌 Lấy danh sách tất cả các refreshToken đang lưu
        Set<String> keys = redisTemplate.keys("refreshToken:*");

        // 📌 Duyệt qua danh sách để kiểm tra token nào trùng với refreshToken cần tìm
        for (String key : keys) {
            String storedToken = redisTemplate.opsForValue().get(key);
            if (refreshToken.equals(storedToken)) {
                return key.split(":")[1]; // ✅ Trả về email nếu token hợp lệ
            }
        }

        throw new AppException(ErrorCode.REFRESH_TOKEN_INVALID);
    }

    public boolean deleteToken(String refreshToken) {
        Set<String> keys = redisTemplate.keys("refreshToken:*");

        if (refreshToken == null || refreshToken.isEmpty()) {
            return false;
        }

        for (String key : keys) {
            String storedToken = redisTemplate.opsForValue().get(key);
            if (refreshToken.equals(storedToken)) {
                redisTemplate.delete(key); // ✅ Xóa đúng key
                return true;
            }
        }

        return false;
    }



}
