package com.emintufan.orderservice.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateProductRequest {
    private String name;
    private Long category;
    private int unitInStock;
    private BigDecimal unitPrice;
}
