package com.rewards.service;

import com.rewards.entity.Transaction;
import com.rewards.model.Rewards;
import com.rewards.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class RewardsServiceTest {

    @InjectMocks
    private RewardsService rewardsService;

    @Mock
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testGetRewardsByCustomerId_withTransactions() {
        String customerId = "1";
        List<Transaction> transactions = Arrays.asList(
                new Transaction(customerId, 60.0, Timestamp.valueOf("2025-01-10 10:00:00")),
                new Transaction(customerId, 120.0, Timestamp.valueOf("2025-01-20 10:00:00"))
        );
        Mockito.when(transactionRepository.findAllByCustomerIdAndTransactionDateBetween(
                        anyString(), any(), any()))
                .thenReturn(transactions);
        Rewards result = rewardsService.getRewardsByCustomerId(customerId);
        assertNotNull(result);
        assertEquals(300, result.getTotalRewards());
    }

    @Test
    void testCalculateRewards_forTransaction() {
        Transaction transaction1 = new Transaction("1", 60.0, Timestamp.valueOf(LocalDateTime.now()));
        Transaction transaction2 = new Transaction("1", 120.0, Timestamp.valueOf(LocalDateTime.now()));
        Transaction transaction3 = new Transaction("1", 30.0, Timestamp.valueOf(LocalDateTime.now()));
        Long reward1 = rewardsService.calculateRewards(transaction1);
        Long reward2 = rewardsService.calculateRewards(transaction2);
        Long reward3 = rewardsService.calculateRewards(transaction3);
        assertEquals(10L, reward1);
        assertEquals(90L, reward2);
        assertEquals(0L, reward3);
    }

    @Test
    void testGetDateBasedOnOffSetDays() {
        int offset = 30;
        Timestamp timestamp = rewardsService.getDateBasedOnOffSetDays(offset);
        assertNotNull(timestamp);
        assertTrue(timestamp.before(Timestamp.valueOf(LocalDateTime.now())));
    }

    @Test
    void testGetRewardsByCustomerIdAndMonth() {
        String customerId = "1";
        String month = "2025-01";
        List<Transaction> transactions = Arrays.asList(
                new Transaction(customerId, 60.0, Timestamp.valueOf("2025-01-15 10:00:00")),
                new Transaction(customerId, 120.0, Timestamp.valueOf("2025-01-20 10:00:00"))
        );
        long expectedTotalRewards = 100L;
        when(transactionRepository.findAllByCustomerIdAndTransactionDateBetween(
                eq(customerId),
                any(Timestamp.class),
                any(Timestamp.class)
        )).thenReturn(transactions);

        Rewards rewards = rewardsService.getRewardsByCustomerIdAndMonth(customerId, month);
        assertNotNull(rewards);
        assertEquals(customerId, rewards.getCustomerId());
        assertEquals(expectedTotalRewards, rewards.getTotalRewards());
    }

    @Test
    void testGetRewardsByCustomerIdAndMonth_noTransactions() {
        String customerId = "1";
        String month = "2025-01";
        when(transactionRepository.findAllByCustomerIdAndTransactionDateBetween(
                eq(customerId),
                any(Timestamp.class),
                any(Timestamp.class)
        )).thenReturn(Arrays.asList());
        Rewards rewards = rewardsService.getRewardsByCustomerIdAndMonth(customerId, month);
        assertNotNull(rewards);
        assertEquals(customerId, rewards.getCustomerId());
        assertEquals(0, rewards.getTotalRewards());
    }


    @Test
    void testCalculateRewards_AmountLessThan50() {
        List<Transaction> transactions = Arrays.asList(
                createTransaction(30,"1"),
                createTransaction(40,"1"),
                createTransaction(49,"1")
        );

        long rewardPoints = rewardsService.getRewardsPerMonth(transactions);
        assertEquals(0, rewardPoints, "No reward points should be given for amounts below 50");
    }

    @Test
    void testCalculateRewards_AmountBetween50And100() {
        List<Transaction> transactions = Arrays.asList(
                createTransaction(60,"1"),
                createTransaction(75,"1"),
                createTransaction(99,"1")
        );

        long rewardPoints = rewardsService.getRewardsPerMonth(transactions);
        assertEquals(84, rewardPoints, "Reward points calculation failed for amounts between 50 and 100");
    }

    @Test
    void testCalculateRewards_AmountGreaterThan100() {
        List<Transaction> transactions = Arrays.asList(
                createTransaction(110,"1"),
                createTransaction(150,"1"),
                createTransaction(200,"1")
        );

        long rewardPoints = rewardsService.getRewardsPerMonth(transactions);
        assertEquals(470, rewardPoints, "Reward points calculation failed for amounts greater than 100");
    }

    @Test
    void testCalculateRewards_AmountExactly100() {
        List<Transaction> transactions = Arrays.asList(
                createTransaction(100,"1")
        );

        long rewardPoints = rewardsService.getRewardsPerMonth(transactions);
        assertEquals(50, rewardPoints, "Reward points should be 50 for an exact amount of 100");
    }

    @Test
    void testCalculateRewards_MixedTransactionAmounts() {
        List<Transaction> transactions = Arrays.asList(
                createTransaction(30,"1"),
                createTransaction(60,"1"),
                createTransaction(100,"1"),
                createTransaction(150,"1")
        );

        long rewardPoints = rewardsService.getRewardsPerMonth(transactions);
        assertEquals(210, rewardPoints, "Reward points calculation failed for mixed transactions");
    }

    private Transaction createTransaction(double amount , String customerId) {
        Transaction transaction = new Transaction();
        transaction.setCustomerId(customerId);
        transaction.setTransactionAmount(amount);
        transaction.setTransactionDate(Timestamp.from(Instant.now()));
        return transaction;
    }
}