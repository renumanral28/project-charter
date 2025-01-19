package com.rewards.service;

import com.rewards.entity.Transaction;
import com.rewards.model.Rewards;
import com.rewards.repository.TransactionRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;


public class RewardsService {

    @Autowired
    TransactionRepository transactionRepository;

    public Rewards getRewardsByCustomerId(Long customerId) {

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

        Long lastMonthRewardPoints = getRewardsPerMonth(lastMonthTransactions);
        Long lastSecondMonthRewardPoints = getRewardsPerMonth(lastSecondMonthTransactions);
        Long lastThirdMonthRewardPoints = getRewardsPerMonth(lastThirdMonthTransactions);

        Rewards customerRewards = new Rewards();
        customerRewards.setCustomerId(customerId);
        customerRewards.setLastMonthRewardPoints(lastMonthRewardPoints);
        customerRewards.setLastSecondMonthRewardPoints(lastSecondMonthRewardPoints);
        customerRewards.setLastThirdMonthRewardPoints(lastThirdMonthRewardPoints);
        customerRewards.setTotalRewards(lastMonthRewardPoints + lastSecondMonthRewardPoints + lastThirdMonthRewardPoints);

        return customerRewards;

    }

    private Long getRewardsPerMonth(List<Transaction> transactions) {
        return transactions.stream().map(transaction -> calculateRewards(transaction)).mapToLong(r -> r.longValue()).sum();
    }

    private Long calculateRewards(Transaction t) {
        if (t.getTransactionAmount() > 50 && t.getTransactionAmount() <= 100) {
            return Math.round(t.getTransactionAmount() - 50);
        } else if (t.getTransactionAmount() > 100) {
            return Math.round(t.getTransactionAmount() - 100) * 2
                    + (100 - 50);
        } else
            return 0l;

    }

    public Timestamp getDateBasedOnOffSetDays(int days) {
        return Timestamp.valueOf(LocalDateTime.now().minusDays(days));
    }
}
