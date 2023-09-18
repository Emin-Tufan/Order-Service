package com.emintufan.orderservice.service.concretes;

import com.emintufan.orderservice.dao.business.CategoryRepository;
import com.emintufan.orderservice.dto.request.CreateCategoryRequest;
import com.emintufan.orderservice.dto.response.CreateCategoryResponse;
import com.emintufan.orderservice.entities.business.Category;
import com.emintufan.orderservice.mapper.ModelMapperManager;
import com.emintufan.orderservice.service.abstracts.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository repository;
    private ModelMapperManager modelMapperManager;

    @Autowired
    public CategoryServiceImpl(CategoryRepository repository, ModelMapperManager modelMapperManager) {
        this.repository = repository;
        this.modelMapperManager = modelMapperManager;
    }

    @Override
    public String createCategory(CreateCategoryRequest categoryRequest) {
        try {
            Category category = Category.builder()
                    .name(categoryRequest.getName()).build();
            repository.save(category);
            return ("Category Created!");
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while creating category");
        }
    }
    @Override
    public String updateCategory(CreateCategoryRequest request, Long categoryId) {
        Optional<Category> categoryOptional = repository.findById(categoryId);

        if (!categoryOptional.isPresent()) {
            throw new IllegalArgumentException("Category was not found!");
        }
        try {
            Category category = categoryOptional.get();
            category.setName(request.getName());
            repository.save(category);

            CreateCategoryResponse response = new CreateCategoryResponse();
            return "Category Updated!";
        } catch (Exception e) {
            throw new RuntimeException("Error while updating category");
        }
    }

    @Override
    public String deleteCategory(Long categoryId) {
        Optional<Category> categoryOptional = repository.findById(categoryId);
        if (!categoryOptional.isPresent()) {
            throw new IllegalArgumentException("Category was not found!");
        }
        try {
            repository.delete(categoryOptional.get());
            return ("Category Deleted!");
        } catch (Exception e) {
            throw new RuntimeException("Error while deleting category");
        }
    }

    @Override
    public List<CreateCategoryResponse> getCategories() {
        List<Category> categories = repository.findAll();
        return categories.stream()
                .map(category -> modelMapperManager.forResponse().map(category, CreateCategoryResponse.class))
                .collect(Collectors.toList());
    }
}
