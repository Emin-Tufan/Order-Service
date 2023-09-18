package com.emintufan.orderservice.dto.request;

import lombok.Data;

@Data
public class CreateOrderItemRequest {
    private Long userId;
    private int quantity;
    private Long productId;
}