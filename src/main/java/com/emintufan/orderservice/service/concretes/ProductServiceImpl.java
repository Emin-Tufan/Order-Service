package com.emintufan.orderservice.service.concretes;

import com.emintufan.orderservice.dao.business.CategoryRepository;
import com.emintufan.orderservice.dao.business.ProductRepository;
import com.emintufan.orderservice.dto.request.CreateProductRequest;
import com.emintufan.orderservice.dto.response.CreateProductResponse;
import com.emintufan.orderservice.entities.business.Category;
import com.emintufan.orderservice.entities.business.Product;
import com.emintufan.orderservice.mapper.ModelMapperManager;
import com.emintufan.orderservice.service.abstracts.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository repository;
    private ModelMapperManager manager;
    private CategoryRepository categoryRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository repository, ModelMapperManager manager, CategoryRepository categoryRepository) {
        this.repository = repository;
        this.manager = manager;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public String createProduct(CreateProductRequest createProductRequest) {
        Optional<Category> category = categoryRepository.findById(createProductRequest.getCategory());
        if (!category.isPresent())
            throw new IllegalArgumentException("Category was not found!");

        Product product = manager.forRequest().map(createProductRequest, Product.class);
        product.setCategory(category.get());
        repository.save(product);
        return ("Product was created!");
    }

    @Override
    public String updateCategory(CreateProductRequest request, Long productId) {
        Optional<Product> product = repository.findById(productId);
        Optional<Category> category = categoryRepository.findById(request.getCategory());
        if (!category.isPresent() || !product.isPresent())
            throw new IllegalArgumentException("Category or Product was not found!");

        Product newProduct = manager.forRequest().map(request, Product.class);
        newProduct.setCategory(category.get());
        newProduct.setId(productId);

        repository.save(newProduct);
        return ("Product was Updated!");
    }

    @Override
    public String deleteProduct(Long productId) {

        Optional<Product> product = repository.findById(productId);
        if (!product.isPresent())
            throw new IllegalArgumentException("Product was not found!");
        repository.deleteById(productId);

        return ("Product was deleted!");
    }

    @Override
    public List<CreateProductResponse> getProducts() {
        List<Product> productResponses = repository.findAll();
        List<CreateProductResponse> responses = productResponses.stream().map(product -> manager
                .forResponse().map(product, CreateProductResponse.class)).collect(Collectors.toList());
        if (productResponses.isEmpty())
            return (null);
        return (responses);
    }
}
