package com.java6.asm.clothing_store.controller;

import com.java6.asm.clothing_store.dto.request.AddressRequest;
import com.java6.asm.clothing_store.dto.response.AddressResponse;
import com.java6.asm.clothing_store.service.AddressService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/address")
@AllArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping("/create")
    public ResponseEntity<AddressResponse> createAddress(@RequestBody AddressRequest request) {
        AddressResponse response = addressService.createAddress(request);
        return ResponseEntity.ok(response);
    }
}