package com.cakesandsnacks.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateOfBirth;
    private String preferredLanguage;
    private String timezone;
    private Integer loyaltyPoints;
    private String membershipTier;
}
