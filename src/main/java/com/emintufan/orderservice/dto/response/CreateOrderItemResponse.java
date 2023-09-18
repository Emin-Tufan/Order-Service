package com.emintufan.orderservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderItemResponse {
    private Long id;
    private int quantity;
    private BigDecimal price;
    private CreateOrderResponse order;
    private CreateProductResponse product;
}
