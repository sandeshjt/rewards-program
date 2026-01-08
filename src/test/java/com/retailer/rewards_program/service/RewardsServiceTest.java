package com.retailer.rewards_program.service;

import com.retailer.rewards_program.repository.RewardsRepo;
import com.retailer.rewards_program.model.Customer;
import com.retailer.rewards_program.model.Reward;
import com.retailer.rewards_program.model.Transaction;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@ExtendWith(MockitoExtension.class)
public class RewardsServiceTest {

    @InjectMocks
    private RewardsService rewardsService;

    @Mock
    private RewardsRepo rewardsRepo;

    @Test
    public void testGetCustomerRewards_Success(){
        Customer customer = new Customer("Cust001","Alice","alice@email.com");
        List<Transaction> transactions = List.of(
                new Transaction("T1001","Cust001",120.0, LocalDate.of(2024,1,15)),
                new Transaction("T1002", "cust001", 75.0,LocalDate.of(2024,1,20))
        );
        Mockito.when(rewardsRepo.findCustomerById(Mockito.anyString())).thenReturn(customer);
        Mockito.when(rewardsRepo.getTransactionsByCustomerId(Mockito.anyString())).thenReturn(transactions);
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
                new Transaction("T1001","Cust001",120.0, LocalDate.of(2024,1,15)),
                new Transaction("T1002", "cust001", 75.0,LocalDate.of(2024,1,20))
        );
        Mockito.when(rewardsRepo.findCustomerById(Mockito.anyString())).thenReturn(customer);
        Mockito.when(rewardsRepo.getTransactionsByCustomerId(Mockito.anyString())).thenReturn(transactions);

        Reward reward = rewardsService.getCustomerRewards("Cust001",LocalDate.of(2024,1,16),LocalDate.of(2024,1,30));
        assertEquals("Cust001",customer.getCustomerId());
        assertEquals("Alice",customer.getName());
        //Only second transaction falls in the date range
        assertEquals(25,reward.getTotalPoints());
    }

    @Test
    public void testGetCustomerRewards_CustomerNotFound(){
        Mockito.when(rewardsRepo.findCustomerById("invalid")).thenReturn(null);
        assertThrows(NoSuchElementException.class,() -> {
            rewardsService.getCustomerRewards("invalid",null,null);
        });
    }

    @Test
    public void testAsyncSimulation() throws ExecutionException, InterruptedException {
        Customer customer = new Customer("Cust001","Alice","alice@email.com");
        List<Transaction> transactions = List.of(
                new Transaction("T1001","Cust001",120.0, LocalDate.of(2024,1,15)),
                new Transaction("T1002", "Cust001", 75.0,LocalDate.of(2024,1,20))
        );
        Mockito.when(rewardsRepo.findCustomerById(Mockito.anyString())).thenReturn(customer);
        CompletableFuture<List<Transaction>> future = CompletableFuture.supplyAsync(() -> {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {}
                    return transactions;
                });
        Mockito.when(rewardsRepo.getTransactionsByCustomerId(Mockito.anyString())).thenReturn(future.get());
        Reward reward = rewardsService.getCustomerRewards("Cust001",null,null);
        assertEquals(115,reward.getTotalPoints());
    }
}
