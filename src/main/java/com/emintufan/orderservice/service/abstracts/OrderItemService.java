package com.emintufan.orderservice.service.abstracts;

import com.emintufan.orderservice.dto.request.CreateOrderItemRequest;
import com.emintufan.orderservice.dto.response.CreateOrderItemResponse;

import java.util.List;

public interface OrderItemService {
    String updateOrderItem(CreateOrderItemRequest orderItem,Long id);

    List<CreateOrderItemResponse> getOrders(Long userId);

    String deleteOrderItem(Long orderItemId,Long userId);
}
