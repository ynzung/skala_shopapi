package com.sk.skala.shopapi;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.sk.skala.shopapi.dto.CheckInResponse;
import com.sk.skala.shopapi.exception.DuplicateFavoriteException;
import com.sk.skala.shopapi.exception.RewardAlreadyReceivedException;
import com.sk.skala.shopapi.service.CustomerService;

@SpringBootTest
@Transactional
class CustomerFeatureTests {

    @Autowired
    private CustomerService customerService;

    @Test
    void 출석체크는_하루에_한번만_포인트를_지급한다() {
        double beforePoint = customerService
                .getCustomerById("customer1")
                .getCustomerPoint();

        CheckInResponse response = customerService.checkIn("customer1");

        assertEquals(1000.0, response.rewardPoint());
        assertEquals(beforePoint + 1000.0, response.customerPoint());
        assertThrows(
                RewardAlreadyReceivedException.class,
                () -> customerService.checkIn("customer1")
        );
    }

    @Test
    void 찜한_상품을_추가하고_삭제할_수_있다() {
        customerService.addFavorite("customer1", 1L);

        assertEquals(1, customerService.getFavorites("customer1").size());
        assertThrows(
                DuplicateFavoriteException.class,
                () -> customerService.addFavorite("customer1", 1L)
        );

        customerService.deleteFavorite("customer1", 1L);
        assertTrue(customerService.getFavorites("customer1").isEmpty());
    }
}
