package com.sk.skala.shopapi.dto.customer;

import jakarta.validation.constraints.NotBlank;

public record UpdateCustomerRequest(
        @NotBlank(message = "비밀번호는 필수입니다.")
        String customerPassword
) {
}
