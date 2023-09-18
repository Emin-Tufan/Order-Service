package com.emintufan.orderservice.service.abstracts;

import com.emintufan.orderservice.dto.request.CreateOrderRequest;
import com.emintufan.orderservice.dto.response.CreateOrderResponse;


public interface OrderService {
    CreateOrderResponse createOrder(CreateOrderRequest request);
}
