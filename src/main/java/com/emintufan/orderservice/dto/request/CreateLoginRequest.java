package com.emintufan.orderservice.dto.request;

import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class CreateLoginRequest {
    private String email;
    private String password;
}
