package com.java6.asm.clothing_store.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {

    private String email;

    private String password;

    private String deviceId ;
}
