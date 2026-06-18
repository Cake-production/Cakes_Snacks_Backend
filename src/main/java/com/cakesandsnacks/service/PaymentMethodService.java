package com.cakesandsnacks.service;

import com.cakesandsnacks.dto.PaymentMethodDTO;
import com.cakesandsnacks.dto.PaymentMethodCreateDTO;
import com.cakesandsnacks.entity.PaymentMethod;
import com.cakesandsnacks.entity.PaymentType;
import com.cakesandsnacks.entity.User;
import com.cakesandsnacks.repository.PaymentMethodRepository;
import com.cakesandsnacks.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentMethodService {
    private final PaymentMethodRepository paymentMethodRepository;
    private final UserRepository userRepository;

    public PaymentMethodDTO addPaymentMethod(Long userId, PaymentMethodCreateDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // If this is the first payment method, set as default
        long count = paymentMethodRepository.findByUserIdAndIsActiveTrue(userId).size();
        boolean isDefault = (count == 0) || dto.getIsDefault();

        PaymentMethod paymentMethod = PaymentMethod.builder()
                .user(user)
                .paymentType(PaymentType.valueOf(dto.getPaymentType()))
                .cardBrand(dto.getCardBrand())
                .lastFourDigits(dto.getLastFourDigits())
                .expiryMonth(dto.getExpiryMonth())
                .expiryYear(dto.getExpiryYear())
                .cardholderName(dto.getCardholderName())
                .isDefault(isDefault)
                .isActive(true)
                .build();

        // If this is default, unset other defaults for this user
        if (isDefault) {
            paymentMethodRepository.findByUserIdAndIsDefaultTrue(userId)
                    .ifPresent(existing -> {
                        existing.setIsDefault(false);
                        paymentMethodRepository.save(existing);
                    });
        }

        PaymentMethod saved = paymentMethodRepository.save(paymentMethod);
        return convertToDTO(saved);
    }

    public PaymentMethodDTO getPaymentMethod(Long id) {
        PaymentMethod method = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment method not found"));
        return convertToDTO(method);
    }

    public List<PaymentMethodDTO> getUserPaymentMethods(Long userId) {
        return paymentMethodRepository.findByUserIdAndIsActiveTrue(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public PaymentMethodDTO updatePaymentMethod(Long id, PaymentMethodCreateDTO dto) {
        PaymentMethod method = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment method not found"));

        method.setCardholderName(dto.getCardholderName());
        method.setExpiryMonth(dto.getExpiryMonth());
        method.setExpiryYear(dto.getExpiryYear());

        PaymentMethod updated = paymentMethodRepository.save(method);
        return convertToDTO(updated);
    }

    public void deletePaymentMethod(Long id) {
        PaymentMethod method = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment method not found"));
        method.setIsActive(false);
        paymentMethodRepository.save(method);
    }

    public PaymentMethodDTO setDefaultPaymentMethod(Long id) {
        PaymentMethod method = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment method not found"));

        // Unset other defaults for this user
        paymentMethodRepository.findByUserIdAndIsDefaultTrue(method.getUser().getId())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(id)) {
                        existing.setIsDefault(false);
                        paymentMethodRepository.save(existing);
                    }
                });

        // Set this as default
        method.setIsDefault(true);
        PaymentMethod updated = paymentMethodRepository.save(method);
        return convertToDTO(updated);
    }

    private PaymentMethodDTO convertToDTO(PaymentMethod method) {
        return new PaymentMethodDTO(
                method.getId(),
                method.getPaymentType().toString(),
                method.getCardBrand(),
                method.getLastFourDigits(),
                method.getExpiryMonth(),
                method.getExpiryYear(),
                method.getCardholderName(),
                method.getIsDefault(),
                method.getIsActive()
        );
    }
}