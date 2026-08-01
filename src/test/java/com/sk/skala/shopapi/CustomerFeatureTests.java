package com.sk.skala.shopapi;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.sk.skala.shopapi.dto.reward.CheckInResponse;
import com.sk.skala.shopapi.exception.RewardAlreadyReceivedException;
import com.sk.skala.shopapi.repository.CustomerRepository;
import com.sk.skala.shopapi.service.FavoriteService;
import com.sk.skala.shopapi.service.RewardService;

@SpringBootTest
@Transactional
class CustomerFeatureTests {

    @Autowired
    private RewardService rewardService;

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void 출석체크는_하루에_한번만_포인트를_지급한다() {
        double beforePoint = customerRepository.findById("customer1")
                .orElseThrow()
                .getCustomerPoint();

        CheckInResponse response = rewardService.checkIn("customer1");

        assertEquals(1000.0, response.rewardPoint());
        assertEquals(beforePoint + 1000.0, response.customerPoint());
        assertThrows(
                RewardAlreadyReceivedException.class,
                () -> rewardService.checkIn("customer1")
        );
    }

    @Test
    void 찜한_상품을_추가하고_삭제할_수_있다() {
        assertTrue(favoriteService.toggleFavorite("customer1", 1L).favorite());

        assertEquals(1, favoriteService.getFavorites("customer1").size());
        assertFalse(favoriteService.toggleFavorite("customer1", 1L).favorite());
        assertTrue(favoriteService.getFavorites("customer1").isEmpty());
    }
}
