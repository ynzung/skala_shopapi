package com.sk.skala.shopapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDto {

    private String customerId;
    private String customerPassword;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Double customerPoint;
}
