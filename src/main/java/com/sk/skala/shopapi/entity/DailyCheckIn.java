package com.sk.skala.shopapi.entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "daily_check_ins",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"customer_id", "check_in_date"}
        )
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyCheckIn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "check_in_date", nullable = false)
    private LocalDate checkInDate;
}
