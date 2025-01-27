package com.rewards.service;

import com.rewards.entity.Transaction;
import com.rewards.exception.CustomerNotFoundException;
import com.rewards.exception.DatabaseException;
import com.rewards.exception.InvalidInputException;
import com.rewards.model.Rewards;
import com.rewards.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class RewardsService {

    @Autowired
    private TransactionRepository transactionRepository;

    public Rewards getRewardsByCustomerId(String customerId) {
        validateCustomerId(customerId);

        try {
            List<List<Transaction>> transactionsLists = getLastThreeMonthsTransactions(customerId);
            if (transactionsLists.stream().allMatch(List::isEmpty)) {
                throw new CustomerNotFoundException("No transactions found for customer ID: " + customerId);
            }

            return calculateRewards(customerId, transactionsLists);
        } catch (DataAccessException e) {
            throw new DatabaseException("Error retrieving data for customer ID: " + customerId, e);
        }
    }

    public Rewards getRewardsByCustomerIdAndMonth(String customerId, String month, String year) {
        validateCustomerId(customerId);
        validateMonthAndYear(month, year);

        try {
            YearMonth yearMonth = YearMonth.of(Integer.parseInt(year), Integer.parseInt(month));
            List<Transaction> transactions = getTransactionsForMonth(customerId, yearMonth);
            if (transactions.isEmpty()) {
                throw new CustomerNotFoundException("No transactions found for customer ID: " + customerId + " in " + year + "-" + month);
            }

            return calculateRewards(customerId, Collections.singletonList(transactions));
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Invalid month or year format. Expected numeric values.");
        } catch (DataAccessException e) {
            throw new DatabaseException("Error fetching rewards for customer ID: " + customerId, e);
        }
    }

    private List<List<Transaction>> getLastThreeMonthsTransactions(String customerId) {
        return Arrays.asList(
                getTransactions(customerId, getDateBasedOnOffsetDays(30), Timestamp.from(Instant.now())),
                getTransactions(customerId, getDateBasedOnOffsetDays(60), getDateBasedOnOffsetDays(30)),
                getTransactions(customerId, getDateBasedOnOffsetDays(90), getDateBasedOnOffsetDays(60))
        );
    }

    private List<Transaction> getTransactionsForMonth(String customerId, YearMonth yearMonth) {
        Timestamp startTimestamp = Timestamp.valueOf(yearMonth.atDay(1).atStartOfDay());
        Timestamp endTimestamp = Timestamp.valueOf(yearMonth.atEndOfMonth().atTime(23, 59, 59));
        return getTransactions(customerId, startTimestamp, endTimestamp);
    }

    private List<Transaction> getTransactions(String customerId, Timestamp start, Timestamp end) {
        return transactionRepository.findAllByCustomerIdAndTransactionDateBetween(customerId, start, end);
    }



    private Rewards calculateRewards(String customerId, List<List<Transaction>> transactionsLists) {
        int totalRewards = transactionsLists.stream()
                .mapToInt(transactions -> (int) getRewardsPerMonth(transactions))
                .sum();

        Rewards rewards = new Rewards();
        rewards.setCustomerId(customerId);
        rewards.setTotalRewards(totalRewards);
        rewards.setTransaction(
                mergeTransactions(transactionsLists) // Merge the transaction lists from the three months
        );
        if (!transactionsLists.isEmpty()) rewards.setLastMonthRewardPoints((int) getRewardsPerMonth(transactionsLists.get(0)));
        if (transactionsLists.size() > 1) rewards.setLastSecondMonthRewardPoints((int) getRewardsPerMonth(transactionsLists.get(1)));
        if (transactionsLists.size() > 2) rewards.setLastThirdMonthRewardPoints((int) getRewardsPerMonth(transactionsLists.get(2)));

        return rewards;
    }

    private List<Transaction> mergeTransactions(List<List<Transaction>> transactionsLists) {
        List<Transaction> mergedTransactions = new ArrayList<>();
        for (List<Transaction> transactions : transactionsLists) {
            if (transactions != null) {
                mergedTransactions.addAll(transactions);
            }
        }
        return mergedTransactions;
    }



    private long getRewardsPerMonth(List<Transaction> transactions) {
        return transactions.stream()
                .mapToLong(this::calculateRewardPoints)
                .sum();
    }

    long calculateRewardPoints(Transaction transaction) {
        double amount = transaction.getTransactionAmount();
        if (amount > 0) {
            if (amount > 100) {
                return Math.round((amount - 100) * 2 + 50);
            } else if (amount > 50 && amount <= 100) {
                return Math.round(amount - 50);
            }
            return 0;

        }
        else{
            throw new InvalidInputException("Amount cannot be zero or negative.");

        }
    }
    private Timestamp getDateBasedOnOffsetDays(int days) {
        if (days <= 0) throw new InvalidInputException("Days offset should be a positive number.");
        return Timestamp.valueOf(LocalDateTime.now().minusDays(days));
    }

    private void validateCustomerId(String customerId) {
        if (customerId == null || customerId.trim().isEmpty() || customerId.equals("0")) {
            throw new InvalidInputException("Customer ID cannot be null, empty, or zero.");
        }
    }

    private void validateMonthAndYear(String month, String year) {
        if (month == null || month.trim().isEmpty() || year == null || year.trim().isEmpty() || Integer.parseInt(month) > 12 || Integer.parseInt(month) < 1) {
            throw new InvalidInputException("Month and year cannot be null or empty.");
        }
    }
}