package com.emintufan.orderservice.service.concretes;

import com.emintufan.orderservice.dao.business.OrderItemRepository;
import com.emintufan.orderservice.dao.business.OrderRepository;
import com.emintufan.orderservice.dao.business.ProductRepository;
import com.emintufan.orderservice.dao.user.UserRepository;
import com.emintufan.orderservice.dto.request.CreateOrderItemRequest;
import com.emintufan.orderservice.dto.response.CreateOrderItemResponse;
import com.emintufan.orderservice.entities.business.Order;
import com.emintufan.orderservice.entities.business.OrderItem;
import com.emintufan.orderservice.entities.business.Product;
import com.emintufan.orderservice.entities.user.User;
import com.emintufan.orderservice.mapper.ModelMapperManager;
import com.emintufan.orderservice.service.abstracts.OrderItemService;
import jakarta.persistence.EntityManager;
import org.hibernate.event.spi.SaveOrUpdateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    private OrderItemRepository orderItemRepository;
    private OrderRepository orderRepository;
    private ProductRepository productRepository;
    private EntityManager entityManager;
    private ModelMapperManager modelMapperManager;
    private UserRepository userRepository;

    @Autowired
    public OrderItemServiceImpl(OrderItemRepository orderItemRepository, OrderRepository orderRepository, ProductRepository productRepository, EntityManager entityManager, ModelMapperManager modelMapperManager, UserRepository userRepository) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.entityManager = entityManager;
        this.modelMapperManager = modelMapperManager;
        this.userRepository = userRepository;
    }


    @Override
    public String updateOrderItem(CreateOrderItemRequest orderItem, Long id) {

        OrderItem item = orderItemRepository.getReferenceById(id);
        Optional<Product> newProduct = productRepository.findById(orderItem.getProductId());

        if (!newProduct.isPresent()) throw new IllegalArgumentException("Product not found!");

        int newQuantity = orderItem.getQuantity();

        if (!(item != null) || newQuantity <= 0) throw new IllegalArgumentException();

        Order order = item.getOrder();

        BigDecimal price = newProduct.get().getUnitPrice();
        BigDecimal newPrice = price.multiply(new BigDecimal(newQuantity));

        BigDecimal totalPrice = order.getTotalPrice();
        BigDecimal oldPrice = item.getPrice().multiply(new BigDecimal(item.getQuantity()));

        totalPrice = totalPrice.subtract(oldPrice).add(newPrice);

        item.setProduct(newProduct.get());
        item.setQuantity(newQuantity);

        order = item.getOrder();
        order.setTotalPrice(totalPrice);
        item.setOrder(order);

        orderRepository.save(order);
        orderItemRepository.save(item);

        return "Order Update Successfully";
    }

    @Override
    public List<CreateOrderItemResponse> getOrders(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        final String query;

        if (!user.isPresent()) throw new IllegalArgumentException("User not found");

        if (user.get().getAuthorities().stream().anyMatch(temp -> temp.getAuthority().equals("ROLE_CUSTOMER")))
            query = "SELECT oi FROM OrderItem oi WHERE oi.order.owner.id = :userId AND oi.isDeleted = false";

        else query = "SELECT oi FROM OrderItem oi WHERE oi.order.owner.id = :userId";

        List<OrderItem> orderItems = entityManager.createQuery(query, OrderItem.class).setParameter("userId", userId).getResultList();

        List<CreateOrderItemResponse> itemResponses = orderItems.stream().map(orderItem -> modelMapperManager.forResponse().map(orderItem, CreateOrderItemResponse.class)).collect(Collectors.toList());
        return (itemResponses);

    }

    @Override
    public String deleteOrderItem(Long orderItemId,Long userId) {
        Optional<OrderItem> orderItem = orderItemRepository.findById(orderItemId);
        Optional<User> user = userRepository.findById(userId);

        if(!user.isPresent()||!orderItem.isPresent())
            throw new IllegalArgumentException("User or OrderItem was not found!");

        BigDecimal orderItemPrice = orderItem.get().getPrice();

        Order order = orderItem.get().getOrder();

        if (user.get().getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            orderItemRepository.deleteById(orderItemId);
            BigDecimal newTotalPrice = order.getTotalPrice().subtract(orderItemPrice);

            order.setTotalPrice(newTotalPrice);
            orderRepository.save(order);
        } else
            orderItem.get().setDeleted(true);

        orderItemRepository.save(orderItem.get());
        return ("OrderItem was deleted!");
    }
}
