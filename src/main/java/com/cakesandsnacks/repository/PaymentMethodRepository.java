package com.cakesandsnacks.repository;

import com.cakesandsnacks.entity.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// PaymentMethod Repository
@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
    List<PaymentMethod> findByUserId(Long userId);
    List<PaymentMethod> findByUserIdAndIsActiveTrue(Long userId);
    Optional<PaymentMethod> findByUserIdAndIsDefaultTrue(Long userId);
    Optional<PaymentMethod> findByStripePaymentMethodId(String stripeId);
}
