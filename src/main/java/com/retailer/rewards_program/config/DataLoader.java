package com.retailer.rewards_program.config;

import com.retailer.rewards_program.entity.Customer;
import com.retailer.rewards_program.entity.Transaction;
import com.retailer.rewards_program.repository.CustomerRepository;
import com.retailer.rewards_program.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * This class is to load the data in H2 in-memory database.
 */
@Configuration
public class DataLoader {

    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    @Bean
    CommandLineRunner loadData(CustomerRepository customerRepository, TransactionRepository transactionRepository){
        List<Customer> customers = Arrays.asList(
                new Customer("cust001","Alice","alice@email.com"),
                new Customer("cust002","Bob","bob@email.com"),
                new Customer("cust003","Charles","charles@email.com")
        );

        List<Transaction> transactions = Arrays.asList(
                new Transaction("T1001", "cust001", 120.0, LocalDate.parse("2025-10-15")),
                new Transaction("T1002", "cust001", 75.0,  LocalDate.parse("2025-11-20")),
                new Transaction("T1003", "cust001", 200.0, LocalDate.parse("2025-12-10")),
                new Transaction("T1004", "cust001", 45.0,  LocalDate.parse("2026-01-05")),

                new Transaction("T2001", "cust002", 60.0,  LocalDate.parse("2025-01-10")),
                new Transaction("T2002", "cust002", 110.0, LocalDate.parse("2025-02-14")),
                new Transaction("T2003", "cust002", 130.0, LocalDate.parse("2026-03-22")),

                new Transaction("T3001", "cust003", 40.0,  LocalDate.parse("2025-08-05")),
                new Transaction("T3002", "cust003", 55.0,  LocalDate.parse("2025-10-18")),
                new Transaction("T3003", "cust003", 95.0,  LocalDate.parse("2026-12-12"))
        );

        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                customerRepository.saveAll(customers);
                transactionRepository.saveAll(transactions);
                logger.info("H2 Database initialized with Customers data and Transactions");
            }
        };
    }
}
