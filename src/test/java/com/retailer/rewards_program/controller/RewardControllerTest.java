package com.retailer.rewards_program.controller;

import com.retailer.rewards_program.dto.Reward;
import com.retailer.rewards_program.service.RewardsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@ExtendWith(MockitoExtension.class)
public class RewardControllerTest {

    @InjectMocks
    private RewardsController rewardsController;

    @Mock
    private RewardsService rewardsService;

    @Test
    public void testGetCustomerRewards_Success(){
        Reward reward = new Reward("cust123", "tesName", Map.of(), 100, List.of());
        Mockito.when(rewardsService.getCustomerRewards(Mockito.anyString(),Mockito.any(),Mockito.any())).thenReturn(reward);
        ResponseEntity<?> responseEntity = rewardsController.getCustomerRewards("cust123", LocalDate.now(),LocalDate.now());
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
    }

    @Test
    public void testGetCustomerRewards_CustomerIdNullOrEmpty(){
        ResponseEntity<?> responseEntity1 = rewardsController.getCustomerRewards(null, LocalDate.now(),LocalDate.now());
        assertEquals(HttpStatus.BAD_REQUEST,responseEntity1.getStatusCode());
        assertEquals("Customer Id cannot be null or empty", responseEntity1.getBody());

        ResponseEntity<?> responseEntity2 = rewardsController.getCustomerRewards(" ", LocalDate.now(),LocalDate.now());
        assertEquals(HttpStatus.BAD_REQUEST,responseEntity1.getStatusCode());
        assertEquals("Customer Id cannot be null or empty", responseEntity2.getBody());
    }

    @Test
    public void testGetCustomerRewards_CustomerNotFound(){
        Mockito.when(rewardsService.getCustomerRewards(Mockito.anyString(),Mockito.any(),Mockito.any()))
                .thenThrow(new NoSuchElementException("Customer not found for customerID: cust102"));
        Exception exception = assertThrows(NoSuchElementException.class,()->{
            rewardsController.getCustomerRewards("cust123", LocalDate.now(),LocalDate.now());
        });
        assertEquals("Customer not found for customerID: cust102", exception.getMessage());
    }

    @Test
    public void testGetCustomerRewards_InternalServerError(){
        Mockito.when(rewardsService.getCustomerRewards(Mockito.anyString(),Mockito.any(),Mockito.any()))
                .thenThrow(new RuntimeException("Internal Error"));
        Exception exception = assertThrows(RuntimeException.class,()-> {
            rewardsController.getCustomerRewards("cust123", LocalDate.now(),LocalDate.now());
        });
        assertEquals("Internal Error", exception.getMessage());
    }

    @Test
    public void testGetCustomerAllRewards_Success(){
        Reward reward = new Reward("cust123", "tesName", Map.of(), 100, List.of());
        Mockito.when(rewardsService.getCustomerAllRewards(Mockito.anyString())).thenReturn(reward);
        ResponseEntity<?> responseEntity = rewardsController.getCustomerAllRewards("cust123");
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
    }

    @Test
    public void testGetCustomerAllRewards_CustomerIdNullOrEmpty(){
        ResponseEntity<?> responseEntity1 = rewardsController.getCustomerAllRewards(null);
        assertEquals(HttpStatus.BAD_REQUEST,responseEntity1.getStatusCode());
        assertEquals("Customer Id cannot be null or empty", responseEntity1.getBody());

        ResponseEntity<?> responseEntity2 = rewardsController.getCustomerAllRewards(" ");
        assertEquals(HttpStatus.BAD_REQUEST,responseEntity1.getStatusCode());
        assertEquals("Customer Id cannot be null or empty", responseEntity2.getBody());
    }

    @Test
    public void testGetCustomerAllRewards_CustomerNotFound(){
        Mockito.when(rewardsService.getCustomerAllRewards(Mockito.anyString()))
                .thenThrow(new NoSuchElementException("Customer not found for customerID: cust102"));
        Exception exception = assertThrows(NoSuchElementException.class,()->{
            rewardsController.getCustomerAllRewards("cust123");
        });
        assertEquals("Customer not found for customerID: cust102", exception.getMessage());
    }

    @Test
    public void testGetAllCustomerRewards_InternalServerError(){
        Mockito.when(rewardsService.getCustomerAllRewards(Mockito.anyString()))
                .thenThrow(new RuntimeException("Internal Error"));
        Exception exception = assertThrows(RuntimeException.class,()-> {
            rewardsController.getCustomerAllRewards("cust123");
        });
        assertEquals("Internal Error", exception.getMessage());
    }
}
