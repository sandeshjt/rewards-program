package com.retailer.rewards_program.repository;

import com.retailer.rewards_program.model.Customer;
import com.retailer.rewards_program.model.Transaction;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class RewardsRepo {

    private final List<Customer> customers = Arrays.asList(
            new Customer("cust001","Alice","alice@email.com"),
            new Customer("cust002","Bob","bob@email.com"),
            new Customer("cust003","Charles","charles@email.com")
    );

    private final List<Transaction> transactions = Arrays.asList(
            new Transaction("T1001", "cust001", 120.0, LocalDate.parse("2024-01-15")),
            new Transaction("T1002", "cust001", 75.0,  LocalDate.parse("2024-01-20")),
            new Transaction("T1003", "cust001", 200.0, LocalDate.parse("2024-02-10")),
            new Transaction("T1004", "cust001", 45.0,  LocalDate.parse("2024-03-05")),

            new Transaction("T2001", "cust002", 60.0,  LocalDate.parse("2024-01-10")),
            new Transaction("T2002", "cust002", 110.0, LocalDate.parse("2024-02-14")),
            new Transaction("T2003", "cust002", 130.0, LocalDate.parse("2024-03-22")),

            new Transaction("T3001", "cust003", 40.0,  LocalDate.parse("2024-01-05")),
            new Transaction("T3002", "cust003", 55.0,  LocalDate.parse("2024-02-18")),
            new Transaction("T3003", "cust003", 95.0,  LocalDate.parse("2024-03-12"))
    );

    public List<Customer> getAllCustomers(){
        return customers;
    }

    public Customer findCustomerById(String customerId){
        return customers.stream().filter(c->c.getCustomerId()
                .equals(customerId))
                .findFirst()
                .orElse(null);
    }

    public List<Transaction> getAllTransactions(){
        return transactions;
    }

    public List<Transaction> getTransactionsByCustomerId(String customerId){
        return transactions.stream()
                .filter(t->t.getCustomerId().equals(customerId))
                .collect(Collectors.toList());
    }

}
