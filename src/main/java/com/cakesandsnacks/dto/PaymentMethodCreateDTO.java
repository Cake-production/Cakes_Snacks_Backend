package com.cakesandsnacks.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodCreateDTO {
    private String paymentType;
    private String cardBrand;
    private String lastFourDigits;
    private String expiryMonth;
    private String expiryYear;
    private String cardholderName;
    private Boolean isDefault;
}
