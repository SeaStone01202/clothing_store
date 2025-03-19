package com.java6.asm.clothing_store.dto.response;

import com.java6.asm.clothing_store.constance.RoleEnum;
import com.java6.asm.clothing_store.constance.StatusEnum;
import com.java6.asm.clothing_store.constance.TypeAccountEnum;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterResponse {

    private String username;

    private String fullname;

    private String email;

    private String password;

    private RoleEnum role;

    private StatusEnum status;

    private TypeAccountEnum type;

    private LocalDate createdAt;
}
