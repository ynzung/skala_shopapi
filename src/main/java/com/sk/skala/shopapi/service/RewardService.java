package com.sk.skala.shopapi.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sk.skala.shopapi.dto.reward.CheckInResponse;
import com.sk.skala.shopapi.entity.Customer;
import com.sk.skala.shopapi.entity.DailyCheckIn;
import com.sk.skala.shopapi.exception.DataNotFoundException;
import com.sk.skala.shopapi.exception.RewardAlreadyReceivedException;
import com.sk.skala.shopapi.repository.CustomerRepository;
import com.sk.skala.shopapi.repository.DailyCheckInRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RewardService {
    private static final double DAILY_CHECK_IN_POINT = 1000.0;

    private final CustomerRepository customerRepository;
    private final DailyCheckInRepository dailyCheckInRepository;

    @Transactional
    public CheckInResponse checkIn(String customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new DataNotFoundException(
                        "고객을 찾을 수 없습니다: " + customerId
                ));
        LocalDate today = LocalDate.now();

        if (dailyCheckInRepository
                .existsByCustomerCustomerIdAndCheckInDate(customerId, today)) {
            throw new RewardAlreadyReceivedException(
                    "오늘은 이미 출석 포인트를 받았습니다."
            );
        }

        dailyCheckInRepository.save(DailyCheckIn.builder()
                .customer(customer)
                .checkInDate(today)
                .build());
        customer.setCustomerPoint(
                customer.getCustomerPoint() + DAILY_CHECK_IN_POINT
        );
        customerRepository.save(customer);

        return new CheckInResponse(
                "출석 체크가 완료되었습니다.",
                DAILY_CHECK_IN_POINT,
                customer.getCustomerPoint()
        );
    }
}
