package com.retailer.rewards_program.repository;

import com.retailer.rewards_program.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {

    @Override
    List<Transaction> findAll();

    Optional<List<Transaction>> findByCustomerId(String customerId);
}
