package com.retailer.rewards_program.controller;

import com.retailer.rewards_program.dto.Reward;
import com.retailer.rewards_program.service.RewardsService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/rewards")
@Validated
public class RewardsController {

    private static final Logger logger = LoggerFactory.getLogger(RewardsController.class);

    private final RewardsService rewardService;

    public RewardsController(RewardsService rewardService) {
        this.rewardService = rewardService;
    }

    /**
     * API that return rewards of the customer based on startDate and endDate.
     * If startDate and endDate not provided, return the rewards of last 3 month.
     * @param customerId customerId
     * @param startDate startDate
     * @param endDate endDate
     * @return ResponseEntity
     */

    @Operation(
            summary = "Get reward details for a customer.",
            description = "Returns total points, monthly points, and transactions for the customer based on startDate " +
                    "and endDate. If dates are not provided, return rewards for 3 month. "
    )
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> getCustomerRewards(
            @PathVariable String customerId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate){
        if(customerId == null || customerId.trim().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Customer Id cannot be null or empty");
        }
            Reward reward = rewardService.getCustomerRewards(customerId, startDate, endDate);
            logger.info("Returning rewards response for customerId : {}",customerId);
            return ResponseEntity.ok(reward);
    }

    /**
     * API that returns all the rewards of the customer.
     * @param customerId customerId
     * @return ResponseEntity
     */
    @Operation(
            summary = "Get all reward details for a customer",
            description = "Returns total points, monthly points, and all transactions for the customer."
    )
    @GetMapping("/customer/{customerId}/all")
    public ResponseEntity<?> getCustomerAllRewards(@RequestParam String customerId){
        if(customerId == null || customerId.trim().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Customer Id cannot be null or empty");
        }
        Reward reward = rewardService.getCustomerAllRewards(customerId);
        logger.info("Returning rewards response for customerId : {}",customerId);
        return ResponseEntity.ok(reward);
    }
}
