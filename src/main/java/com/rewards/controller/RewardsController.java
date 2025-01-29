package com.rewards.controller;

import com.rewards.model.Rewards;
import com.rewards.service.RewardsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/rewards/{customerId}")
public class RewardsController {

    private static final Logger logger = LoggerFactory.getLogger(RewardsController.class);

    @Autowired
    private RewardsService rewardsService;

    @GetMapping
    public ResponseEntity<Rewards> getRewardsByCustomerId(@PathVariable("customerId") String customerId) {
        logger.info("Fetching rewards for customerId: {}", customerId);
        Rewards rewards = rewardsService.getRewardsByCustomerId(customerId);
        return ResponseEntity.ok(rewards);
    }

    @GetMapping("/{year}/{month}")
    public ResponseEntity<Rewards> getRewardsByCustomerIdAndDate(
            @PathVariable("customerId") String customerId,
            @PathVariable("year") String year,
            @PathVariable("month") String month) {
        logger.info("Fetching rewards for customerId: {}, year: {}, month: {}", customerId, year, month);
        Rewards rewards = rewardsService.getRewardsByCustomerIdAndMonth(customerId, month, year);
        return ResponseEntity.ok(rewards);
    }
}