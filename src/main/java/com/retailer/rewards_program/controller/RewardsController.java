package com.retailer.rewards_program.controller;

import com.retailer.rewards_program.model.Reward;
import com.retailer.rewards_program.service.RewardsService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.NoSuchElementException;


@RestController
@RequestMapping("/api/rewards")
public class RewardsController {

    private static final Logger logger = LoggerFactory.getLogger(RewardsController.class);
    @Autowired
    private RewardsService rewardService;

    @Operation(
            summary = "Get reward details for a customer",
            description = "Returns total points, monthly points, and all transactions for the customer"
    )
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> getCustomerRewards(
            @PathVariable String customerId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate){
        if(customerId == null || customerId.trim().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Customer Id cannot be null or empty");
        }
        try {
            Reward reward = rewardService.getCustomerRewards(customerId, startDate, endDate);
            logger.info("Returning rewards response for customerId : {}",customerId);
            return ResponseEntity.ok(reward);
        } catch (NoSuchElementException e) {
            logger.error("Customer not found: {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found for customerID : "+customerId);
        }
        catch (Exception e) {
            logger.error("Internal Server Error : {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error : {} "+e.getMessage());
        }
    }
}
