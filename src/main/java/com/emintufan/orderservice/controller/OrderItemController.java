package com.emintufan.orderservice.controller;

import com.emintufan.orderservice.dto.request.CreateOrderItemRequest;
import com.emintufan.orderservice.dto.request.CreateOrderRequest;
import com.emintufan.orderservice.dto.response.CreateOrderItemResponse;
import com.emintufan.orderservice.dto.response.CreateOrderResponse;
import com.emintufan.orderservice.service.abstracts.OrderItemService;
import com.emintufan.orderservice.service.abstracts.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/order-item")
public class OrderItemController {
    private OrderService orderService;
    private OrderItemService orderItemService;

    @Autowired
    public OrderItemController(OrderService orderService, OrderItemService orderItemService) {
        this.orderService = orderService;
        this.orderItemService = orderItemService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest request) {
        try {
            URI location = URI.create("/create");
            CreateOrderResponse response = orderService.createOrder(request);
            return ResponseEntity.created(location).body(response);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body("Bad Request: " + ex.getMessage());
        } catch (RuntimeException e) {
            return (ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No have stock!"));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Internal Server Error!");
        }
    }

    @PutMapping("/update/{orderId}")
    public ResponseEntity<?> updateOrder(@RequestBody CreateOrderItemRequest request, @PathVariable Long orderId) {
        try {
            String response = orderItemService.updateOrderItem(request, orderId);
            return (ResponseEntity.status(HttpStatus.ACCEPTED).body(response));
        } catch (IllegalArgumentException e) {
            return (ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request" + e.getMessage()));
        } catch (RuntimeException e) {
            return (ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error" + e.getMessage()));
        }
    }

    @GetMapping("/orders/{userId}")
    public ResponseEntity<?> getOrders(@PathVariable Long userId) {
        try {
            List<CreateOrderItemResponse> orderList = orderItemService.getOrders(userId);
            return (ResponseEntity.status(HttpStatus.OK).body(orderList));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error : " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{orderItemId}/{userId}")
    public ResponseEntity<?> deleteOrderItem(@PathVariable Long orderItemId, @PathVariable Long userId) {
        try {
            orderItemService.deleteOrderItem(orderItemId, userId);
            return ResponseEntity.status(HttpStatus.OK).body("Order Item Was Deleted!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("OrderItem or User  was not found");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal Server Error :" + e.getMessage());
        }
    }
}
