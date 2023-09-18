package com.emintufan.orderservice.service.abstracts;

import com.emintufan.orderservice.dto.request.CreateCategoryRequest;
import com.emintufan.orderservice.dto.response.CreateCategoryResponse;

import java.util.List;

public interface CategoryService {
    String createCategory(CreateCategoryRequest categoryRequest);

    String updateCategory(CreateCategoryRequest request,Long categoryId);

    String deleteCategory(Long categoryId);
    List<CreateCategoryResponse> getCategories();
}
