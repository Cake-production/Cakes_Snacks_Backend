package com.cakesandsnacks.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateDTO {
    private List<OrderItemCreateDTO> orderItems;
    private AddressDTO shippingAddress;
    private Long paymentMethodId;
    private String deliveryOption;
    private String notes;
}
