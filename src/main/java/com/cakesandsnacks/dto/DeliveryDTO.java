package com.cakesandsnacks.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Delivery DTOs
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDTO {
    private Long id;
    private Long orderId;
    private String status;
    private String trackingNumber;
    private String carrier;
    private String estimatedDeliveryDate;
    private String actualDeliveryDate;
    private String driverName;
    private String driverPhone;
}
