package com.cakesandsnacks.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Analytics DTOs
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {
    private String label;
    private String value;
    private String change;
    private String trend;
}
