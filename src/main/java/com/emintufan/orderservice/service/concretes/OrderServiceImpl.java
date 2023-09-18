package com.emintufan.orderservice.service.concretes;

import com.emintufan.orderservice.dao.business.OrderItemRepository;
import com.emintufan.orderservice.dao.business.OrderRepository;
import com.emintufan.orderservice.dao.business.ProductRepository;
import com.emintufan.orderservice.dao.user.UserRepository;
import com.emintufan.orderservice.dto.request.CreateOrderItemRequest;
import com.emintufan.orderservice.dto.request.CreateOrderRequest;
import com.emintufan.orderservice.dto.response.CreateOrderResponse;
import com.emintufan.orderservice.dto.response.CreateUserResponse;
import com.emintufan.orderservice.entities.business.Order;
import com.emintufan.orderservice.entities.business.OrderItem;
import com.emintufan.orderservice.entities.business.Product;
import com.emintufan.orderservice.entities.user.User;
import com.emintufan.orderservice.mapper.ModelMapperManager;
import com.emintufan.orderservice.service.abstracts.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    private OrderRepository orderRepository;
    private OrderItemRepository orderItemRepository;
    private ProductRepository productRepository;
    private UserRepository userRepository;
    private ModelMapperManager manager;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository, ProductRepository productRepository, UserRepository userRepository, ModelMapperManager manager) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.manager = manager;
    }

    @Override
    public CreateOrderResponse createOrder(CreateOrderRequest request) {
        List<CreateOrderItemRequest> itemRequests = request.getCreateOrderItemRequests();
        if (itemRequests.isEmpty())
            throw new IllegalArgumentException("OrderItems Not Found!");

        List<OrderItem> orderItemList = new ArrayList<>();

        User user = userRepository.findById(itemRequests.get(0).getUserId()).orElseThrow(
                () -> new IllegalArgumentException("User Not Found!"));

        BigDecimal totalPrice = BigDecimal.ZERO;
        LocalDateTime currentDateTime = LocalDateTime.now();

        Order order = new Order();
        order.setOwner(user);
        order.setDate(currentDateTime);

        for (CreateOrderItemRequest tempItem : itemRequests) {
            Optional<Product> product = productRepository.findById(tempItem.getProductId());

            if (!product.isPresent())
                throw new IllegalArgumentException("User or Product Not Found!");
            if(tempItem.getQuantity() > product.get().getUnitInStock())
                throw new RuntimeException("No have Product Stock!");
            OrderItem tempOrderItem =
                    OrderItem.builder()
                            .order(order)
                            .price(product.get().getUnitPrice())
                            .product(product.get())
                            .quantity(tempItem.getQuantity())
                            .build();
            orderItemList.add(tempOrderItem);
            totalPrice = totalPrice.add(product.get().getUnitPrice().
                    multiply(new BigDecimal(tempItem.getQuantity())));
        }

        order.setTotalPrice(totalPrice);
        for (OrderItem orderItem : orderItemList) {
            orderItem.setOrder(order);
        }
        orderRepository.save(order);
        orderItemRepository.saveAll(orderItemList);

        CreateOrderResponse response = CreateOrderResponse.builder()
                .id(order.getId())
                .date(order.getDate())
                .totalPrice(order.getTotalPrice())
                .owner(CreateUserResponse.builder()
                        .id(order.getOwner().getId())
                        .name(order.getOwner().getName())
                        .email(order.getOwner().getEmail())
                        .role(user.getRole())
                        .build())
                .build();
        return response;
    }
}
