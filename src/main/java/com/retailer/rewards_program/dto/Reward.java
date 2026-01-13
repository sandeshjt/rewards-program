package com.retailer.rewards_program.dto;

import com.retailer.rewards_program.entity.Transaction;
import java.util.List;


public class Reward {
    private String customerId;
    private String customerName;
    private int totalPoints;
    private List<MonthlyPoints> monthlyPoints;
    private List<Transaction> transactions;

    public Reward(String customerId, String customerName,int totalPoints, List<MonthlyPoints> monthlyPoints, List<Transaction> transactions) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.totalPoints = totalPoints;
        this.monthlyPoints = monthlyPoints;
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

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public List<MonthlyPoints> getMonthlyPoints() {
        return monthlyPoints;
    }

    public void setMonthlyPoints(List<MonthlyPoints> monthlyPoints) {
        this.monthlyPoints = monthlyPoints;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
