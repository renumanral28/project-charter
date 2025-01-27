package com.rewards.model;

import com.rewards.entity.Transaction;

import java.util.List;

public class Rewards {
    private String customerId;
	private int lastMonthRewardPoints;
    private int lastSecondMonthRewardPoints;
    private int lastThirdMonthRewardPoints;
    private int totalRewards;
	private List<Transaction> transaction;

    public String getCustomerId() {
        return customerId;
    }

    public int getLastMonthRewardPoints() {
		return lastMonthRewardPoints;
	}

	public void setLastMonthRewardPoints(int lastMonthRewardPoints) {
		this.lastMonthRewardPoints = lastMonthRewardPoints;
	}

	public int getLastSecondMonthRewardPoints() {
		return lastSecondMonthRewardPoints;
	}

	public void setLastSecondMonthRewardPoints(int lastSecondMonthRewardPoints) {
		this.lastSecondMonthRewardPoints = lastSecondMonthRewardPoints;
	}

	public int getLastThirdMonthRewardPoints() {
		return lastThirdMonthRewardPoints;
	}

	public void setLastThirdMonthRewardPoints(int lastThirdMonthRewardPoints) {
		this.lastThirdMonthRewardPoints = lastThirdMonthRewardPoints;
	}

	public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    

    public int getTotalRewards() {
        return totalRewards;
    }

	public List<Transaction> getTransaction() {
		return transaction;
	}

	public void setTransaction(List<Transaction> transaction) {
		this.transaction = transaction;
	}

	public void setTotalRewards(int totalRewards) {
        this.totalRewards = totalRewards;
    }


}


