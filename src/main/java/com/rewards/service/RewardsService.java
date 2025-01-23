package com.rewards.service;

import com.rewards.entity.Transaction;
import com.rewards.model.Rewards;
import com.rewards.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;

@Service
public class RewardsService {

    @Autowired
    TransactionRepository transactionRepository;

    public Rewards getRewardsByCustomerId(String customerId) {

        if (customerId.equals("0")  ||  customerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer ID cannot be null or empty.");
        }
        try {
            Timestamp lastMonthTimestamp = getDateBasedOnOffSetDays(30);
            Timestamp lastSecondMonthTimestamp = getDateBasedOnOffSetDays(60);
            Timestamp lastThirdMonthTimestamp = getDateBasedOnOffSetDays(90);

            List<Transaction> lastMonthTransactions = transactionRepository.findAllByCustomerIdAndTransactionDateBetween(
                    customerId, lastMonthTimestamp, Timestamp.from(Instant.now()));
            List<Transaction> lastSecondMonthTransactions = transactionRepository
                    .findAllByCustomerIdAndTransactionDateBetween(customerId, lastSecondMonthTimestamp, lastMonthTimestamp);
            List<Transaction> lastThirdMonthTransactions = transactionRepository
                    .findAllByCustomerIdAndTransactionDateBetween(customerId, lastThirdMonthTimestamp,
                            lastSecondMonthTimestamp);

            int lastMonthRewardPoints = Math.toIntExact(getRewardsPerMonth(lastMonthTransactions));
            int lastSecondMonthRewardPoints = Math.toIntExact(getRewardsPerMonth(lastSecondMonthTransactions));
            int lastThirdMonthRewardPoints = Math.toIntExact(getRewardsPerMonth(lastThirdMonthTransactions));

            Rewards customerRewards = new Rewards();
            customerRewards.setCustomerId(customerId);
            customerRewards.setLastMonthRewardPoints(lastMonthRewardPoints);
            customerRewards.setLastSecondMonthRewardPoints(lastSecondMonthRewardPoints);
            customerRewards.setLastThirdMonthRewardPoints(lastThirdMonthRewardPoints);
            customerRewards.setTotalRewards(lastMonthRewardPoints + lastSecondMonthRewardPoints + lastThirdMonthRewardPoints);

            return customerRewards;

        }

        catch (DataAccessException e) {
            throw new RuntimeException("Database error occurred while fetching rewards.", e);
        }
    }

    public Rewards getRewardsByCustomerIdAndMonth(String customerId, String month) {

        if (customerId == null || customerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer ID cannot be null or empty.");
        }
        if (month == null || month.trim().isEmpty()) {
            throw new IllegalArgumentException("Month cannot be null or empty.");
        }

        try {
            LocalDate monthStartDate = LocalDate.parse(month + "-01");  // e.g., "2025-01" -> "2025-01-01"
            LocalDate monthEndDate = monthStartDate.withDayOfMonth(monthStartDate.lengthOfMonth());

            Timestamp startTimestamp = Timestamp.valueOf(monthStartDate.atStartOfDay());
            Timestamp endTimestamp = Timestamp.valueOf(monthEndDate.atStartOfDay().plusDays(1)); // Include the full day

            List<Transaction> transactions = transactionRepository.findAllByCustomerIdAndTransactionDateBetween(
                    customerId, startTimestamp, endTimestamp);

            int monthRewardPoints = Math.toIntExact(getRewardsPerMonth(transactions));

            Rewards customerRewards = new Rewards();
            customerRewards.setCustomerId(customerId);
            customerRewards.setLastMonthRewardPoints(monthRewardPoints);
            customerRewards.setTotalRewards(monthRewardPoints);

            return customerRewards;
        }
        catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected format is YYYY-MM.", e);
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error occurred while fetching rewards.", e);
        }
    }


    Long getRewardsPerMonth(List<Transaction> transactions) {
        return transactions.stream()
                .mapToLong(this::calculateRewards)
                .sum();    }

    Long calculateRewards(Transaction t) {

        if (Objects.isNull(t)) {
            throw new IllegalArgumentException("Invalid transaction data.");
        }
        if (t.getTransactionAmount() > 50 && t.getTransactionAmount() <= 100) {
            return Math.round(t.getTransactionAmount() - 50);
        } else if (t.getTransactionAmount() > 100) {
            return Math.round(t.getTransactionAmount() - 100) * 2
                    + 50;

        } else
            return 0L;

    }

    public Timestamp getDateBasedOnOffSetDays(int days) {
        if (days <= 0) {
            throw new IllegalArgumentException("Days offset should be a positive number.");
        }
        return Timestamp.valueOf(LocalDateTime.now().minusDays(days));
    }
}
