package com.retailer.rewards_program.service;

import com.retailer.rewards_program.repository.CustomerRepository;
import com.retailer.rewards_program.entity.Customer;
import com.retailer.rewards_program.dto.Reward;
import com.retailer.rewards_program.entity.Transaction;
import com.retailer.rewards_program.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RewardsService {

    private static final Logger logger = LoggerFactory.getLogger(RewardsService.class);

    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;

    public RewardsService(CustomerRepository customerRepository, TransactionRepository transactionRepository) {
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
    }

    public Reward getCustomerRewards(String customerId, LocalDate startDate, LocalDate endDate){
        Customer customer = customerRepository.findByCustomerId(customerId)
                .orElseThrow(()->new NoSuchElementException("Customer not found for id: "+customerId));
        List<Transaction> customerTransactions = transactionRepository.findByCustomerId(customerId)
                .orElse(Collections.emptyList());

        LocalDate effectiveEndDate = (endDate != null) ? endDate : LocalDate.now();
        LocalDate effectiveStartDate = (startDate != null) ? startDate : effectiveEndDate.minusMonths(3);

        customerTransactions = customerTransactions.stream()
                .filter(transaction -> {
                    LocalDate transactionDate = transaction.getTransactionDate();
                    return !transactionDate.isBefore(effectiveStartDate) && !transactionDate.isAfter(effectiveEndDate);
                }).collect(Collectors.toList());

        Map<Month, Integer> monthlyPoints = new EnumMap<>(Month.class);
        int totalPoints = 0;

        for (Transaction transaction : customerTransactions) {
            int points = calculateRewardPoints(transaction.getAmount());
            Month month = transaction.getTransactionDate().getMonth();
            monthlyPoints.merge(month, points, Integer::sum);
            totalPoints += points;
        }

        return new Reward(
                customer.getCustomerId(),
                customer.getName(),
                monthlyPoints,
                totalPoints,
                customerTransactions
        );
    }

    public Reward getCustomerAllRewards(String customerId){
        Customer customer = customerRepository.findByCustomerId(customerId)
                .orElseThrow(()->new NoSuchElementException("Customer not found for id: "+customerId));
        List<Transaction> customerTransactions = transactionRepository.findByCustomerId(customerId)
                .orElse(Collections.emptyList());

        Map<Month, Integer> monthlyPoints = new EnumMap<>(Month.class);
        int totalPoints = 0;

        for (Transaction transaction : customerTransactions) {
            int points = calculateRewardPoints(transaction.getAmount());
            Month month = transaction.getTransactionDate().getMonth();
            monthlyPoints.merge(month, points, Integer::sum);
            totalPoints += points;
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
