package com.java6.asm.clothing_store.service;

import com.java6.asm.clothing_store.dto.request.ProductRequest;
import com.java6.asm.clothing_store.dto.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    ProductResponse addProduct(ProductRequest productRequest);

    ProductResponse updateProduct(ProductRequest productRequest);

    void deleteProduct(Integer productId);

    ProductResponse getProduct(Integer productId);

    List<ProductResponse> retrieveAllProducts();

    Page<ProductResponse> findAll(Pageable pageable);

    Page<ProductResponse> findByCategory(String category, Pageable pageable);

    Page<ProductResponse> findByName(String name, Pageable pageable);
}
