package com.emintufan.orderservice.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {
    private List<CreateOrderItemRequest> createOrderItemRequests;
}
