package com.rewards.controller;

import com.rewards.model.Rewards;
import com.rewards.service.RewardsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Month;

@RestController
public class RewardsController {

    @Autowired
    private RewardsService rewardsService;
    @GetMapping(value = "/{customerId}/rewards")
    public ResponseEntity<Rewards> getRewardsByCustomerId(
        @PathVariable("customerId") String customerId,
        @RequestParam(value = "month", required = false) String month) {

    Rewards rewards;

    if (month != null && !month.isEmpty()) {
        rewards = rewardsService.getRewardsByCustomerIdAndMonth(customerId, month);
    } else {
        rewards = rewardsService.getRewardsByCustomerId(customerId);
    }

    if (rewards == null) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(rewards, HttpStatus.OK);
}
}
