package com.cakesandsnacks.service;

import com.cakesandsnacks.dto.PaymentDTO;
import com.cakesandsnacks.dto.PaymentProcessDTO;
import com.cakesandsnacks.entity.*;
import com.cakesandsnacks.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentMethodRepository paymentMethodRepository;

    public PaymentDTO processPayment(PaymentProcessDTO dto) {
        Order order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        PaymentMethod paymentMethod = paymentMethodRepository.findById(dto.getPaymentMethodId())
                .orElseThrow(() -> new RuntimeException("Payment method not found"));

        // Simulate payment processing
        String transactionId = "TXN-" + UUID.randomUUID().toString();

        Payment payment = Payment.builder()
                .order(order)
                .transactionId(transactionId)
                .amount(dto.getAmount())
                .status(PaymentStatus.PROCESSING)
                .paymentMethod(paymentMethod.getPaymentType())
                .responseData("{\"status\": \"processing\", \"timestamp\": \"" + LocalDateTime.now() + "\"}")
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        // Update order status
        order.setStatus(OrderStatus.CONFIRMED);
        orderRepository.save(order);

        // Simulate successful payment
        savedPayment.setStatus(PaymentStatus.COMPLETED);
        paymentRepository.save(savedPayment);

        return convertToDTO(savedPayment);
    }

    public PaymentDTO getPaymentByOrderId(Long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found for order"));
        return convertToDTO(payment);
    }

    public PaymentDTO getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        return convertToDTO(payment);
    }

    public void refundPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (payment.getStatus() != PaymentStatus.COMPLETED) {
            throw new RuntimeException("Can only refund completed payments");
        }

        payment.setStatus(PaymentStatus.REFUNDED);
        paymentRepository.save(payment);

        Order order = payment.getOrder();
        order.setStatus(OrderStatus.REFUNDED);
        orderRepository.save(order);
    }

    private PaymentDTO convertToDTO(Payment payment) {
        return new PaymentDTO(
                payment.getId(),
                payment.getOrder().getId(),
                payment.getTransactionId(),
                payment.getAmount(),
                payment.getStatus().toString(),
                payment.getPaymentMethod().toString(),
                payment.getCreatedAt()
        );
    }
}
