package com.retailer.rewards_program.dto;

import com.retailer.rewards_program.entity.Transaction;

import java.time.Month;
import java.util.List;
import java.util.Map;

public class Reward {
    private String customerId;
    private String customerName;
    private Map<Month, Integer> monthlyPoints;
    private int totalPoints;
    private List<Transaction> transactions;

    public Reward(String customerId, String customerName, Map<Month, Integer> monthlyPoints, int totalPoints, List<Transaction> transactions) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.monthlyPoints = monthlyPoints;
        this.totalPoints = totalPoints;
        this.transactions = transactions;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Map<Month, Integer> getMonthlyPoints() {
        return monthlyPoints;
    }

    public void setMonthlyPoints(Map<Month, Integer> monthlyPoints) {
        this.monthlyPoints = monthlyPoints;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
