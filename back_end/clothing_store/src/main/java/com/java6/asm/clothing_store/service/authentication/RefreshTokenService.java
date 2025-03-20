package com.java6.asm.clothing_store.service.authentication;

public interface RefreshTokenService {

    String generateToken(String email, String deviceId);

    String validateToken(String token);

    boolean deleteToken(String token);
}