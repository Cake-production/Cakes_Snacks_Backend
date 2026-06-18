package com.cakesandsnacks.controller;

import com.cakesandsnacks.dto.PaymentDTO;
import com.cakesandsnacks.dto.PaymentMethodDTO;
import com.cakesandsnacks.dto.PaymentMethodCreateDTO;
import com.cakesandsnacks.dto.PaymentProcessDTO;
import com.cakesandsnacks.service.PaymentService;
import com.cakesandsnacks.service.PaymentMethodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class PaymentController {
    private final PaymentService paymentService;
    private final PaymentMethodService paymentMethodService;

    // ==================== PAYMENT METHODS ====================

    @PostMapping("/methods")
    public ResponseEntity<PaymentMethodDTO> addPaymentMethod(
            @RequestParam Long userId,
            @RequestBody PaymentMethodCreateDTO dto) {
        PaymentMethodDTO method = paymentMethodService.addPaymentMethod(userId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(method);
    }

    @GetMapping("/methods/user/{userId}")
    public ResponseEntity<List<PaymentMethodDTO>> getPaymentMethods(@PathVariable Long userId) {
        List<PaymentMethodDTO> methods = paymentMethodService.getUserPaymentMethods(userId);
        return ResponseEntity.ok(methods);
    }

    @GetMapping("/methods/detail/{methodId}")
    public ResponseEntity<PaymentMethodDTO> getPaymentMethod(@PathVariable Long methodId) {
        PaymentMethodDTO method = paymentMethodService.getPaymentMethod(methodId);
        return ResponseEntity.ok(method);
    }

    @PutMapping("/methods/{methodId}")
    public ResponseEntity<PaymentMethodDTO> updatePaymentMethod(
            @PathVariable Long methodId,
            @RequestBody PaymentMethodCreateDTO dto) {
        PaymentMethodDTO method = paymentMethodService.updatePaymentMethod(methodId, dto);
        return ResponseEntity.ok(method);
    }

    @DeleteMapping("/methods/{methodId}")
    public ResponseEntity<Void> deletePaymentMethod(@PathVariable Long methodId) {
        paymentMethodService.deletePaymentMethod(methodId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/methods/{methodId}/set-default")
    public ResponseEntity<PaymentMethodDTO> setDefaultPaymentMethod(@PathVariable Long methodId) {
        PaymentMethodDTO method = paymentMethodService.setDefaultPaymentMethod(methodId);
        return ResponseEntity.ok(method);
    }

    // ==================== PAYMENTS ====================

    @PostMapping("/process")
    public ResponseEntity<PaymentDTO> processPayment(@RequestBody PaymentProcessDTO dto) {
        PaymentDTO payment = paymentService.processPayment(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(payment);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentDTO> getPaymentById(@PathVariable Long paymentId) {
        PaymentDTO payment = paymentService.getPaymentById(paymentId);
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentDTO> getPaymentByOrderId(@PathVariable Long orderId) {
        PaymentDTO payment = paymentService.getPaymentByOrderId(orderId);
        return ResponseEntity.ok(payment);
    }

    @PostMapping("/{paymentId}/refund")
    public ResponseEntity<Void> refundPayment(@PathVariable Long paymentId) {
        paymentService.refundPayment(paymentId);
        return ResponseEntity.noContent().build();
    }
}