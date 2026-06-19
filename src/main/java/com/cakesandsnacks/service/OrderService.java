package com.cakesandsnacks.service;

import com.cakesandsnacks.dto.*;
import com.cakesandsnacks.entity.*;
import com.cakesandsnacks.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final AddressRepository addressRepository;

    public OrderDTO createOrder(Long userId, OrderCreateDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Calculate totals
        BigDecimal subtotal = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemCreateDTO itemDto : dto.getOrderItems()) {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (product.getStock() < itemDto.getQuantity()) {
                throw new RuntimeException("Insufficient stock for " + product.getName());
            }

            BigDecimal lineTotal = product.getPrice().multiply(
                    BigDecimal.valueOf(itemDto.getQuantity())
            );
            subtotal = subtotal.add(lineTotal);

            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(itemDto.getQuantity())
                    .unitPrice(product.getPrice())
                    .lineTotal(lineTotal)
                    .specialInstructions(itemDto.getSpecialInstructions())
                    .build();

            orderItems.add(orderItem);
            product.setStock(product.getStock() - itemDto.getQuantity());
            productRepository.save(product);
        }

        // Calculate shipping and tax
        BigDecimal shippingCost = calculateShippingCost(dto.getDeliveryOption());
        BigDecimal taxAmount = subtotal.multiply(BigDecimal.valueOf(0.08));
        BigDecimal totalAmount = subtotal.add(shippingCost).add(taxAmount);

        // Create order
        String orderNumber = "ORD-" + System.currentTimeMillis();
        Order order = Order.builder()
                .orderNumber(orderNumber)
                .user(user)
                .status(OrderStatus.PENDING)
                .subtotal(subtotal)
                .shippingCost(shippingCost)
                .taxAmount(taxAmount)
                .totalAmount(totalAmount)
                .deliveryOption(DeliveryOption.valueOf(dto.getDeliveryOption()))
                .notes(dto.getNotes())
                .build();

        // Set shipping address
        if (dto.getShippingAddress() != null) {
            Address address = Address.builder()
                    .firstName(dto.getShippingAddress().getFirstName())
                    .lastName(dto.getShippingAddress().getLastName())
                    .street(dto.getShippingAddress().getStreet())
                    .city(dto.getShippingAddress().getCity())
                    .state(dto.getShippingAddress().getState())
                    .zipCode(dto.getShippingAddress().getZipCode())
                    .country(dto.getShippingAddress().getCountry())
                    .phone(dto.getShippingAddress().getPhone())
                    .addressType(AddressType.SHIPPING)
                    .isDefault(dto.getShippingAddress().getIsDefault() != null
                            ? dto.getShippingAddress().getIsDefault() : false)
                    .build();
            order.setShippingAddress(address);
        }

        // Set payment method
        if (dto.getPaymentMethodId() != null) {
            PaymentMethod paymentMethod = paymentMethodRepository.findById(dto.getPaymentMethodId())
                    .orElseThrow(() -> new RuntimeException("Payment method not found"));
            order.setPaymentMethod(paymentMethod);
        }

        // ✅ CRITICAL: Initialize the orderItems set and add all items
        order.setOrderItems(new HashSet<>());
        for (OrderItem item : orderItems) {
            item.setOrder(order);
            order.getOrderItems().add(item);
        }

        // Save the order (cascade will persist orderItems)
        Order savedOrder = orderRepository.save(order);

        return convertToDTO(savedOrder);
    }

    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return convertToDTO(order);
    }

    public List<OrderDTO> getUserOrders(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public OrderDTO updateOrderStatus(Long id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(OrderStatus.valueOf(status));
        Order updated = orderRepository.save(order);
        return convertToDTO(updated);
    }

    public void cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() != OrderStatus.PENDING &&
                order.getStatus() != OrderStatus.CONFIRMED) {
            throw new RuntimeException("Cannot cancel order with status: " + order.getStatus());
        }

        order.setStatus(OrderStatus.CANCELLED);

        // Restore product stock
        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
            productRepository.save(product);
        }

        orderRepository.save(order);
    }

    private BigDecimal calculateShippingCost(String deliveryOption) {
        return switch (deliveryOption) {
            case "STANDARD" -> BigDecimal.ZERO;
            case "EXPRESS" -> BigDecimal.valueOf(25);
            case "WHITE_GLOVE" -> BigDecimal.valueOf(85);
            default -> BigDecimal.ZERO;
        };
    }

    private OrderDTO convertToDTO(Order order) {
        // ✅ order.getOrderItems() is now guaranteed to be non‑null
        List<OrderItemDTO> items = order.getOrderItems().stream()
                .map(item -> new OrderItemDTO(
                        item.getId(),
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        item.getLineTotal(),
                        item.getSpecialInstructions()
                ))
                .collect(Collectors.toList());

        return new OrderDTO(
                order.getId(),
                order.getOrderNumber(),
                order.getStatus().toString(),
                order.getSubtotal(),
                order.getShippingCost(),
                order.getTaxAmount(),
                order.getTotalAmount(),
                order.getDeliveryOption().toString(),
                order.getCreatedAt(),
                order.getUpdatedAt(),
                items
        );
    }
}