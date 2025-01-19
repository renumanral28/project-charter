package com.rewards.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.rewards.model.Rewards;
import com.rewards.service.RewardsService;

@RestController
public class RewardsController {

    @Autowired
    RewardsService rewardsService;

    @GetMapping(value = "/{customerId}/rewards")
    public ResponseEntity<Rewards> getRewardsByCustomerId(@PathVariable("customerId") Long customerId) {

         return new ResponseEntity<>(rewardsService.getRewardsByCustomerId(customerId), HttpStatus.OK);
        }


}

