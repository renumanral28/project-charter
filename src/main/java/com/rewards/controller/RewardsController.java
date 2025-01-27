package com.rewards.controller;

import com.rewards.exception.InvalidInputException;
import com.rewards.model.Rewards;
import com.rewards.service.RewardsService;
import com.rewards.exception.CustomerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        try {
            Rewards rewards = rewardsService.getRewardsByCustomerId(customerId);
            return new ResponseEntity<>(rewards, HttpStatus.OK);
        } catch (CustomerNotFoundException e) {
            logger.error("Customer do not exist: {}", customerId);
            throw e;
        }
        catch (InvalidInputException e) {
            logger.error("Invalid Customer {}", customerId);
            throw e; 
        }
        catch (Exception e) {
            logger.error("Unexpected error while fetching rewards for customerId: {}", customerId, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{year}/{month}")
    public ResponseEntity<Rewards> getRewardsByCustomerIdAndDate(
            @PathVariable("customerId") String customerId,
            @PathVariable("year") String year,
            @PathVariable("month") String month) {
        logger.info("Fetching rewards for customerId: {}, year: {}, month: {}", customerId, year, month);
        try {
            Rewards rewards = rewardsService.getRewardsByCustomerIdAndMonth(customerId, month, year);
            return new ResponseEntity<>(rewards, HttpStatus.OK);
        } catch (CustomerNotFoundException e) {
            logger.error("Customer not found: {}", customerId);
            throw e; 
        } catch (Exception e) {
            logger.error("Unexpected error while fetching rewards for customerId: {}, year: {}, month: {}",
                    customerId, year, month, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}