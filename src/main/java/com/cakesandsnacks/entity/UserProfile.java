package com.cakesandsnacks.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "user_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column
    private LocalDate dateOfBirth;

    @Column
    private String companyName;

    @Column
    private String businessType;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column
    private String preferredLanguage = "en";

    @Column
    private String timezone = "UTC";

    @Column
    private Boolean twoFactorEnabled = false;

    @Column
    private Integer loyaltyPoints = 0;

    @Column
    private String membershipTier = "STANDARD";
}
