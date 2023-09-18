package com.emintufan.orderservice.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateRegisterRequest {
    private String email;
    private String name;
    private String password;
}
