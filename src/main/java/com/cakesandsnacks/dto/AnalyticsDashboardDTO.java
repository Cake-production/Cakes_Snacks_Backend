package com.cakesandsnacks.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsDashboardDTO {
    private BigDecimal totalRevenue;
    private Long totalOrders;
    private Long deliveredOrders;
    private BigDecimal averageOrderValue;
    private Long totalCustomers;
    private List<DashboardStatsDTO> stats;
    private List<TopSellingProductDTO> topProducts;
}
