package com.emintufan.orderservice.controller;

import com.emintufan.orderservice.dto.request.CreateProductRequest;
import com.emintufan.orderservice.service.abstracts.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/product")
@RestController
public class ProductController {
    private ProductService service;

    @Autowired
    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@RequestBody CreateProductRequest createProductRequest) {
        try {
            return (ResponseEntity.status(HttpStatus.CREATED).body(service.createProduct(createProductRequest)));

        } catch (IllegalArgumentException e) {
            return (ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Category Was not found!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error :" + e.getMessage());
        }
    }

    @PutMapping("/put/{productId}")
    public ResponseEntity<?> updateProduct(@RequestBody CreateProductRequest request, @PathVariable Long productId) {
        try {
            return (ResponseEntity.status(HttpStatus.ACCEPTED).body(service.updateCategory(request, productId)));
        } catch (IllegalArgumentException e) {
            return (ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Category or Product was not found!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error :" + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
        try {
            return (ResponseEntity.status(HttpStatus.OK).body(service.deleteProduct(productId)));

        } catch (IllegalArgumentException e) {
            return (ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product was not found!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error :" + e.getMessage());
        }
    }

    @GetMapping("/products")
    public ResponseEntity<?> getProducts() {
        return (ResponseEntity.status(HttpStatus.OK).body(service.getProducts()));
    }
}
