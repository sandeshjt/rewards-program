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
        Customer customer = getCustomer(customerId);
        List<Transaction> customerTransactions = getTransactionForDateRange(customerId,startDate,endDate);
        return buildReward(customer,customerTransactions);
    }

    public Reward getCustomerAllRewards(String customerId){
        Customer customer = getCustomer(customerId);
        List<Transaction> customerTransactions = getAllTransaction(customerId);
        return buildReward(customer,customerTransactions);
    }

    private Customer getCustomer(String customerId){
        logger.info("Fetching customer details for customerId: {}",customerId);
        Customer customer = customerRepository.findByCustomerId(customerId)
                .orElseThrow(()->{
                    logger.error("Customer not found for id: "+customerId);
                    return new NoSuchElementException("Customer not found for id: "+customerId);
                });
        return customer;
    }

    private List<Transaction> getAllTransaction(String customerId){
        logger.info("Fetching all transactions for customerId: {}",customerId);
        return transactionRepository.findByCustomerId(customerId)
                .orElse(Collections.emptyList());
    }

    private List<Transaction> getTransactionForDateRange(String customerId, LocalDate startDate, LocalDate endDate){
        List<Transaction> customerTransactions = getAllTransaction(customerId);

        LocalDate effectiveEndDate = (endDate != null) ? endDate : LocalDate.now();
        LocalDate effectiveStartDate = (startDate != null) ? startDate : effectiveEndDate.minusMonths(3);
        logger.debug("Filtering transactions from {} to {}: ",effectiveStartDate,effectiveEndDate);

        customerTransactions = customerTransactions.stream()
                .filter(transaction -> {
                    LocalDate transactionDate = transaction.getTransactionDate();
                    return !transactionDate.isBefore(effectiveStartDate) && !transactionDate.isAfter(effectiveEndDate);
                }).collect(Collectors.toList());
        return customerTransactions;
    }

    private Reward buildReward(Customer customer, List<Transaction> customerTransactions){

        logger.info("Calculating reward points for customerId {} : ",customer.getCustomerId());
        Map<Month, Integer> monthlyPoints = new EnumMap<>(Month.class);
        int totalPoints = 0;

        for (Transaction transaction : customerTransactions) {
            int points = calculateRewardPoints(transaction.getAmount());
            Month month = transaction.getTransactionDate().getMonth();
            monthlyPoints.merge(month, points, Integer::sum);
            totalPoints += points;
        }
        logger.info("Calculating total : {} for customerId {} : ",totalPoints, customer.getCustomerId());
        return new Reward(
                customer.getCustomerId(),
                customer.getName(),
                monthlyPoints,
                totalPoints,
                customerTransactions
        );
    }

    private int calculateRewardPoints(double amount) {

        if(amount < 0){
            throw new IllegalArgumentException("Transaction amount cannot be negative.");
        }

        int points = 0;

        if (amount > 100) {
            points += (int)(amount - 100) * 2;
            points += 50; // from 50 to 100
        } else if (amount > 50) {
            points += (int)(amount - 50);
        }

        return points;
    }
}
