package com.retailer.rewards_program.controller;

import com.retailer.rewards_program.model.Reward;
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
        ResponseEntity<?> responseEntity = rewardsController.getCustomerRewards("cust102", LocalDate.now(),LocalDate.now());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Customer not found for customerID : cust102", responseEntity.getBody());
    }

    @Test
    public void testGetCustomerRewards_InternalServerError(){
        Mockito.when(rewardsService.getCustomerRewards(Mockito.anyString(),Mockito.any(),Mockito.any()))
                .thenThrow(new RuntimeException("Internal Error"));
        ResponseEntity<?> responseEntity = rewardsController.getCustomerRewards("cust123", LocalDate.now(),LocalDate.now());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Internal Server Error : {} Internal Error", responseEntity.getBody());
    }
}
