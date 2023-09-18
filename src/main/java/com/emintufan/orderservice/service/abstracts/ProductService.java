package com.emintufan.orderservice.service.abstracts;

import com.emintufan.orderservice.dto.request.CreateProductRequest;
import com.emintufan.orderservice.dto.response.CreateProductResponse;

import java.util.List;

public interface ProductService {
    String createProduct(CreateProductRequest createProductRequest);

    String updateCategory(CreateProductRequest request, Long productId);

    String deleteProduct(Long productId);

    List<CreateProductResponse> getProducts();
}
