package com.cakesandsnacks.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Payment Method DTOs
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodDTO {
    private Long id;
    private String paymentType;
    private String cardBrand;
    private String lastFourDigits;
    private String expiryMonth;
    private String expiryYear;
    private String cardholderName;
    private Boolean isDefault;
    private Boolean isActive;
}
