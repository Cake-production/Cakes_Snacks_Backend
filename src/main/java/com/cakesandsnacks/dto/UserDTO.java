package com.cakesandsnacks.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

// User DTOs
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String role;
    private String profileImage;
    private Boolean isActive;
    private LocalDateTime createdAt;
}

