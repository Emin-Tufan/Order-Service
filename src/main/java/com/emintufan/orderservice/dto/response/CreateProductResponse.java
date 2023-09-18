package com.emintufan.orderservice.dto.response;

import com.emintufan.orderservice.entities.business.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductResponse {
    private Long id;
    private String name;
    private int unitInStock;
    private CreateCategoryResponse category;
    private BigDecimal unitPrice;

}
