package com.retailer.rewards_program.service;

import com.retailer.rewards_program.repository.RewardsRepo;
import com.retailer.rewards_program.model.Customer;
import com.retailer.rewards_program.model.Reward;
import com.retailer.rewards_program.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class RewardsService {

    private static final Logger logger = LoggerFactory.getLogger(RewardsService.class);

    @Autowired
    private RewardsRepo rewardsRepo;

    public Reward getCustomerRewards(String customerId, LocalDate startDate, LocalDate endDate){
        Customer customer = rewardsRepo.findCustomerById(customerId);
        if(customer == null){
            logger.error("Customer not found for customerId : {}",customerId);
            throw new NoSuchElementException("Customer not found for id: "+customerId);
        }

        List<Transaction> customerTransactions = rewardsRepo.getTransactionsByCustomerId(customerId);

        if(customerTransactions != null && startDate != null && endDate != null){
            customerTransactions = customerTransactions.stream()
                    .filter(transaction -> {
                        LocalDate transactionDate = transaction.getTransactionDate();
                        return ((transactionDate.isEqual(startDate) || transactionDate.isAfter(startDate)) &&
                                (transactionDate.isEqual(endDate) || transactionDate.isBefore(endDate)));
                            }
                    ).collect(Collectors.toList());
        }

        Map<Month, Integer> monthlyPoints = new EnumMap<>(Month.class);
        int totalPoints = 0;

        if(null != customerTransactions){
            for (Transaction transaction : customerTransactions) {
                int points = calculateRewardPoints(transaction.getAmount());
                Month month = transaction.getTransactionDate().getMonth();
                monthlyPoints.merge(month, points, Integer::sum);
                totalPoints += points;
            }
        }

        return new Reward(
                customer.getCustomerId(),
                customer.getName(),
                monthlyPoints,
                totalPoints,
                customerTransactions == null ? Collections.emptyList() : customerTransactions
        );

    }

    private int calculateRewardPoints(double amount) {
        int points = 0;

        if (amount > 100) {
            points += (amount - 100) * 2;
            points += 50; // from 50 to 100
        } else if (amount > 50) {
            points += (amount - 50);
        }

        return points;
    }
}
