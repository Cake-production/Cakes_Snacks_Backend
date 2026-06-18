package com.cakesandsnacks.service;

import com.cakesandsnacks.dto.AnalyticsDashboardDTO;
import com.cakesandsnacks.dto.DashboardStatsDTO;
import com.cakesandsnacks.dto.TopSellingProductDTO;
import com.cakesandsnacks.entity.OrderStatus;
import com.cakesandsnacks.entity.Product;
import com.cakesandsnacks.repository.OrderRepository;
import com.cakesandsnacks.repository.ProductRepository;
import com.cakesandsnacks.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public AnalyticsDashboardDTO getDashboardStats() {
        BigDecimal totalRevenue = Optional.ofNullable(orderRepository.getTotalRevenue())
                .orElse(BigDecimal.ZERO);
        Long totalOrders = (long) orderRepository.findAll().size();
        Long deliveredOrders = orderRepository.countDeliveredOrders();
        Long totalCustomers = (long) userRepository.findAll().size();

        BigDecimal averageOrderValue = totalOrders > 0 
            ? totalRevenue.divide(BigDecimal.valueOf(totalOrders), 2, java.math.RoundingMode.HALF_UP)
            : BigDecimal.ZERO;

        List<DashboardStatsDTO> stats = new ArrayList<>();
        stats.add(new DashboardStatsDTO("Total Revenue", "$" + totalRevenue, "+12%", "up"));
        stats.add(new DashboardStatsDTO("Orders Fulfilled", totalOrders.toString(), "+8%", "up"));
        stats.add(new DashboardStatsDTO("Avg Order Value", "$" + averageOrderValue, "-5%", "down"));
        stats.add(new DashboardStatsDTO("Growth Rate", "68%", "YoY", "up"));

        List<TopSellingProductDTO> topProducts = getTopSellingProducts();

        return new AnalyticsDashboardDTO(
                totalRevenue,
                totalOrders,
                deliveredOrders,
                averageOrderValue,
                totalCustomers,
                stats,
                topProducts
        );
    }

    public List<TopSellingProductDTO> getTopSellingProducts() {
        return productRepository.findTopSellingProducts()
                .stream()
                .map(product -> new TopSellingProductDTO(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getPrice().multiply(BigDecimal.valueOf(product.getSales())),
                        product.getSales()
                ))
                .collect(Collectors.toList());
    }

    public List<DashboardStatsDTO> getMonthlyStats() {
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfMonth = LocalDateTime.now();

        var ordersThisMonth = orderRepository.findOrdersByDateRange(startOfMonth, endOfMonth);
        BigDecimal monthlyRevenue = ordersThisMonth.stream()
                .map(o -> o.getTotalAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<DashboardStatsDTO> stats = new ArrayList<>();
        stats.add(new DashboardStatsDTO("Monthly Revenue", "$" + monthlyRevenue, "+15%", "up"));
        stats.add(new DashboardStatsDTO("Orders This Month", String.valueOf(ordersThisMonth.size()), "+10%", "up"));

        return stats;
    }

    public BigDecimal calculateTotalRevenue() {
        BigDecimal total = orderRepository.getTotalRevenue();
        return total != null ? total : BigDecimal.ZERO;
    }

    public Long getTotalOrdersCount() {
        return (long) orderRepository.findByStatus(OrderStatus.DELIVERED).size();
    }

    public Long getTotalCustomersCount() {
        return (long) userRepository.findAll().size();
    }
}
