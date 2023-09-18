package com.emintufan.orderservice.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderResponse {
    private Long id;
    private LocalDateTime date;
    private BigDecimal totalPrice;
    private CreateUserResponse owner;

}
