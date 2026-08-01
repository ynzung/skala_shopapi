package com.sk.skala.shopapi.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sk.skala.shopapi.entity.DailyCheckIn;

public interface DailyCheckInRepository extends JpaRepository<DailyCheckIn, Long> {
    boolean existsByCustomerCustomerIdAndCheckInDate(
            String customerId,
            LocalDate checkInDate
    );

    void deleteByCustomerCustomerId(String customerId);
}
