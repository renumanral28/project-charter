package com.rewards.service;

import com.rewards.entity.Transaction;
import com.rewards.exception.CustomerNotFoundException;
import com.rewards.exception.DatabaseException;
import com.rewards.exception.InvalidInputException;
import com.rewards.model.Rewards;
import com.rewards.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.dao.DataAccessException;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RewardsServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private RewardsService rewardsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Normal Case for Rewards
    @Test
    void testGetRewardsByCustomerId_Success() {
        String customerId = "123";
        List<Transaction> lastMonthTransactions = createTransactions(120.0, 60.0);
        List<Transaction> lastSecondMonthTransactions = createTransactions(80.0, 55.0);
        List<Transaction> lastThirdMonthTransactions = createTransactions(130.0);
        when(transactionRepository.findAllByCustomerIdAndTransactionDateBetween(any(), any(), any()))
                .thenReturn(lastMonthTransactions)
                .thenReturn(lastSecondMonthTransactions)
                .thenReturn(lastThirdMonthTransactions);

        Rewards rewards = rewardsService.getRewardsByCustomerId(customerId);

        assertNotNull(rewards);
        assertEquals(customerId, rewards.getCustomerId());
        assertEquals(245, rewards.getTotalRewards());
        assertEquals(100, rewards.getLastMonthRewardPoints());
        assertEquals(35, rewards.getLastSecondMonthRewardPoints());
        assertEquals(110, rewards.getLastThirdMonthRewardPoints());
    }

    @Test
    void testGetRewardsByCustomerId_InvalidCustomerId() {
        String invalidCustomerId = "0";

        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            rewardsService.getRewardsByCustomerId(invalidCustomerId);
        });
        assertEquals("Customer ID cannot be null, empty, or zero.", exception.getMessage());
    }

    @Test
    void testGetRewardsByCustomerId_EmptyCustomerId() {
        String emptyCustomerId = "";

        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            rewardsService.getRewardsByCustomerId(emptyCustomerId);
        });
        assertEquals("Customer ID cannot be null, empty, or zero.", exception.getMessage());
    }

    @Test
    void testGetRewardsByCustomerId_NoTransactionsFound() {
        String customerId = "123";
        when(transactionRepository.findAllByCustomerIdAndTransactionDateBetween(any(), any(), any()))
                .thenReturn(Collections.emptyList());

        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> {
            rewardsService.getRewardsByCustomerId(customerId);
        });
        assertEquals("No transactions found for customer ID: " + customerId, exception.getMessage());
    }

    @Test
    void testGetRewardsByCustomerIdAndMonth_NoTransactionsFound() {
        String customerId = "123";
        String month = "01";
        String year = "2025";
        when(transactionRepository.findAllByCustomerIdAndTransactionDateBetween(any(), any(), any()))
                .thenReturn(Collections.emptyList());

        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> {
            rewardsService.getRewardsByCustomerIdAndMonth(customerId, month, year);
        });
        assertEquals("No transactions found for customer ID: " + customerId + " in 2025-01", exception.getMessage());
    }

    @Test
    void testGetRewardsByCustomerIdAndMonth_InvalidMonthFormat() {
        String customerId = "123";
        String month = "13";
        String year = "2025";

        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            rewardsService.getRewardsByCustomerIdAndMonth(customerId, month, year);
        });
        assertEquals("Month and year cannot be null or empty.", exception.getMessage());
    }

    @Test
    void testCalculateRewards_TransactionExactly50() {
        Transaction transaction = new Transaction();
        transaction.setTransactionAmount(50.0);
        long reward = rewardsService.calculateRewardPoints(transaction);

        assertEquals(0, reward);
    }

    @Test
    void testCalculateRewards_TransactionExactly100() {
        Transaction transaction = new Transaction();
        transaction.setTransactionAmount(100.0);

        long reward = rewardsService.calculateRewardPoints(transaction);

        assertEquals(50, reward);
    }

    @Test
    void testCalculateRewards_NegativeAmount() {
        Transaction transaction = new Transaction();
        transaction.setTransactionAmount(-50.0);

        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            rewardsService.calculateRewardPoints(transaction);
        });
        assertEquals("Amount cannot be zero or negative.", exception.getMessage());
    }

    @Test
    void testDatabaseException() {
        String customerId = "123";
        when(transactionRepository.findAllByCustomerIdAndTransactionDateBetween(any(), any(), any()))
                .thenThrow(new DataAccessException("Database error") {});

        DatabaseException exception = assertThrows(DatabaseException.class, () -> {
            rewardsService.getRewardsByCustomerId(customerId);
        });
        assertTrue(exception.getMessage().contains("Error retrieving data for customer ID"));
    }

    @Test
    void testTransactionsOnMonthBoundary() {
        String customerId = "123";
        String month = "01";
        String year = "2025";
        List<Transaction> transactions = createTransactions(120.0, 60.0);

        when(transactionRepository.findAllByCustomerIdAndTransactionDateBetween(any(), any(), any()))
                .thenReturn(transactions);

        Rewards rewards = rewardsService.getRewardsByCustomerIdAndMonth(customerId, month, year);

        assertNotNull(rewards);
        assertEquals(100, rewards.getTotalRewards());
    }

    private List<Transaction> createTransactions(Double... amounts) {
        List<Transaction> transactions = new java.util.ArrayList<>();
        for (Double amount : amounts) {
            Transaction transaction = new Transaction();
            transaction.setTransactionAmount(amount);
            transactions.add(transaction);
        }
        return transactions;
    }
}