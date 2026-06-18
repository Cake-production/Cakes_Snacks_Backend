package com.cakesandsnacks.controller;

import com.cakesandsnacks.dto.AnalyticsDashboardDTO;
import com.cakesandsnacks.dto.DashboardStatsDTO;
import com.cakesandsnacks.dto.TopSellingProductDTO;
import com.cakesandsnacks.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    @GetMapping("/dashboard")
    public ResponseEntity<AnalyticsDashboardDTO> getDashboardStats() {
        AnalyticsDashboardDTO stats = analyticsService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/top-products")
    public ResponseEntity<List<TopSellingProductDTO>> getTopSellingProducts() {
        List<TopSellingProductDTO> topProducts = analyticsService.getTopSellingProducts();
        return ResponseEntity.ok(topProducts);
    }

    @GetMapping("/monthly-stats")
    public ResponseEntity<List<DashboardStatsDTO>> getMonthlyStats() {
        List<DashboardStatsDTO> stats = analyticsService.getMonthlyStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/revenue")
    public ResponseEntity<String> getTotalRevenue() {
        String revenue = "$" + analyticsService.calculateTotalRevenue();
        return ResponseEntity.ok(revenue);
    }

    @GetMapping("/orders-count")
    public ResponseEntity<Long> getTotalOrdersCount() {
        Long count = analyticsService.getTotalOrdersCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/customers-count")
    public ResponseEntity<Long> getTotalCustomersCount() {
        Long count = analyticsService.getTotalCustomersCount();
        return ResponseEntity.ok(count);
    }
}
