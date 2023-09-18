package com.emintufan.orderservice.controller;

import com.emintufan.orderservice.dto.request.CreateCategoryRequest;
import com.emintufan.orderservice.service.abstracts.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCategory(@RequestBody CreateCategoryRequest categoryRequest) {
        try {
            return (ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(categoryRequest)));
        } catch (IllegalArgumentException e) {
            return (ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while creating category"));
        } catch (Exception e) {
            return (ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error :" + e.getMessage()));
        }
    }

    @PutMapping("/put/{categoryId}")
    public ResponseEntity<?> updateCategory(@RequestBody CreateCategoryRequest request, @PathVariable Long categoryId) {
        try {
            return (ResponseEntity.status(HttpStatus.ACCEPTED).body(categoryService.updateCategory(request, categoryId)));
        } catch (IllegalArgumentException e) {
            return (ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Category was not found!"));
        } catch (Exception e) {
            return (ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error :" + e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long categoryId) {
        try {
            return (ResponseEntity.status(HttpStatus.OK).body(categoryService.deleteCategory(categoryId)));
        } catch (IllegalArgumentException e) {
            return (ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Category was not found!"));
        } catch (Exception e) {
            return (ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error :" + e.getMessage()));
        }
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getCategories() {
        try {
            return (ResponseEntity.status(HttpStatus.OK).body(categoryService.getCategories()));
        } catch (RuntimeException e) {
            return (ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error : " + e.getMessage()));
        }
    }
}
