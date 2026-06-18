package com.cakesandsnacks.repository;

import com.cakesandsnacks.entity.Delivery;
import com.cakesandsnacks.entity.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Delivery Repository
@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    Optional<Delivery> findByOrderId(Long orderId);
    Optional<Delivery> findByTrackingNumber(String trackingNumber);
    List<Delivery> findByStatus(DeliveryStatus status);
}
