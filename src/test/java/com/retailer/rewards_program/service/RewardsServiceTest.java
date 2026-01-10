package com.retailer.rewards_program.service;

import com.retailer.rewards_program.repository.CustomerRepository;
import com.retailer.rewards_program.entity.Customer;
import com.retailer.rewards_program.dto.Reward;
import com.retailer.rewards_program.entity.Transaction;
import com.retailer.rewards_program.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@ExtendWith(MockitoExtension.class)
public class RewardsServiceTest {

    @InjectMocks
    private RewardsService rewardsService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Test
    public void testGetCustomerRewards_Success(){
        Customer customer = new Customer("Cust001","Alice","alice@email.com");
        List<Transaction> transactions = List.of(
                new Transaction("T1001","Cust001",120.0, LocalDate.of(2025,11,15)),
                new Transaction("T1002", "cust001", 75.0,LocalDate.of(2026,1,5))
        );
        Mockito.when(customerRepository.findByCustomerId(Mockito.anyString())).thenReturn(Optional.of(customer));
        Mockito.when(transactionRepository.findByCustomerId(Mockito.anyString())).thenReturn(Optional.of(transactions));
        Reward reward = rewardsService.getCustomerRewards("Cust001",null,null);
        assertEquals("Cust001",customer.getCustomerId());
        assertEquals("Alice",customer.getName());
        // Calculate points: 120 -> 90, 75 -> 25 => total 115
        assertEquals(115,reward.getTotalPoints());
    }

    @Test
    public void testGetCustomerRewards_SuccessWithDate(){
        Customer customer = new Customer("Cust001","Alice","alice@email.com");
        List<Transaction> transactions = List.of(
                new Transaction("T1001","Cust001",120.0, LocalDate.of(2025,10,15)),
                new Transaction("T1002", "cust001", 75.0,LocalDate.of(2026,1,20))
        );
        Mockito.when(customerRepository.findByCustomerId(Mockito.anyString())).thenReturn(Optional.of(customer));
        Mockito.when(transactionRepository.findByCustomerId(Mockito.anyString())).thenReturn(Optional.of(transactions));

        Reward reward = rewardsService.getCustomerRewards("Cust001",LocalDate.of(2025,9,16),LocalDate.of(2025,11,30));
        assertEquals("Cust001",customer.getCustomerId());
        assertEquals("Alice",customer.getName());
        //Only second transaction falls in the date range
        assertEquals(90,reward.getTotalPoints());
    }

    @Test
    public void testGetCustomerRewards_CustomerNotFound(){
        Mockito.when(customerRepository.findByCustomerId("invalid")).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class,() -> {
            rewardsService.getCustomerRewards("invalid",null,null);
        });
    }

    @Test
    public void testAsyncSimulation() throws ExecutionException, InterruptedException {
        Customer customer = new Customer("Cust001","Alice","alice@email.com");
        List<Transaction> transactions = List.of(
                new Transaction("T1001","Cust001",120.0, LocalDate.of(2025,10,15)),
                new Transaction("T1002", "Cust001", 75.0,LocalDate.of(2026,1,5))
        );
        Mockito.when(customerRepository.findByCustomerId(Mockito.anyString())).thenReturn(Optional.of(customer));
        CompletableFuture<List<Transaction>> futureTransactions = CompletableFuture.supplyAsync(()->transactions);
        Mockito.when(transactionRepository.findByCustomerId("Cust001")).thenReturn(Optional.ofNullable(futureTransactions.join()));
        Reward reward = rewardsService.getCustomerRewards("Cust001",null,null);
        assertEquals(115,reward.getTotalPoints());
    }

    @Test
    public void testGetCustomerAllRewards_Success(){
        Customer customer = new Customer("Cust001","Alice","alice@email.com");
        List<Transaction> transactions = List.of(
                new Transaction("T1001","Cust001",120.0, LocalDate.of(2025,11,15)),
                new Transaction("T1002", "cust001", 75.0,LocalDate.of(2026,1,5))
        );
        Mockito.when(customerRepository.findByCustomerId(Mockito.anyString())).thenReturn(Optional.of(customer));
        Mockito.when(transactionRepository.findByCustomerId(Mockito.anyString())).thenReturn(Optional.of(transactions));
        Reward reward = rewardsService.getCustomerAllRewards("Cust001");
        assertEquals("Cust001",customer.getCustomerId());
        assertEquals("Alice",customer.getName());
        // Calculate points: 120 -> 90, 75 -> 25 => total 115
        assertEquals(115,reward.getTotalPoints());
    }

    @Test
    public void testGetCustomerAllRewards_CustomerNotFound(){
        Mockito.when(customerRepository.findByCustomerId("invalid")).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class,() -> {
            rewardsService.getCustomerAllRewards("invalid");
        });
    }

    @Test
    public void testAsyncSimulation2() throws ExecutionException, InterruptedException {
        Customer customer = new Customer("Cust001","Alice","alice@email.com");
        List<Transaction> transactions = List.of(
                new Transaction("T1001","Cust001",120.0, LocalDate.of(2025,10,15)),
                new Transaction("T1002", "Cust001", 75.0,LocalDate.of(2026,1,5))
        );
        Mockito.when(customerRepository.findByCustomerId(Mockito.anyString())).thenReturn(Optional.of(customer));
        CompletableFuture<List<Transaction>> futureTransactions = CompletableFuture.supplyAsync(()->transactions);
        Mockito.when(transactionRepository.findByCustomerId("Cust001")).thenReturn(Optional.ofNullable(futureTransactions.join()));
        Reward reward = rewardsService.getCustomerAllRewards("Cust001");
        assertEquals(115,reward.getTotalPoints());
    }
}
