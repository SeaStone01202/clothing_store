package com.java6.asm.clothing_store.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class OrderResponse {
    private Integer id;
    private Double totalPrice;
    private String status;        // Giá trị enum gốc (reception, prepare, ...)
    private String statusDisplay; // Giá trị hiển thị tiếng Việt
    private Integer userId;
    private Integer addressId;
    private LocalDate createdAt;
}