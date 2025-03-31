package com.java6.asm.clothing_store.controller;

import com.java6.asm.clothing_store.dto.ApiResponse;
import com.java6.asm.clothing_store.dto.request.OrderRequest;
import com.java6.asm.clothing_store.dto.response.OrderResponse;
import com.java6.asm.clothing_store.entity.Order;
import com.java6.asm.clothing_store.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(
            @RequestHeader( value = "Authorization", required = false) String authorizationHeader,
            @RequestBody OrderRequest request) {
        String accessToken = authorizationHeader.replace("Bearer ", "");
        return ResponseEntity.ok(ApiResponse.success(orderService.createOrder(accessToken, request)));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrdersByEmail (
            @RequestHeader( value = "Authorization", required = false) String authorizationHeader) {
        String accessToken = authorizationHeader.replace("Bearer ", "");
        return ResponseEntity.ok(ApiResponse.success(orderService.getOrdersByEmail(accessToken)));
    }
}