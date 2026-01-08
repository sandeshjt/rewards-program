# Spring Boot Application - Reward calculator
This application helps to calculate the reward points that is offered to the retail customers 
based on each recorded purchase.
---
## **How reward point is calculated:**
- A customer receives 2 points for every dollar spent over `$100` in each transaction, plus 1 point for every 
dollar spent between `$50` and `$100` in each transaction.
(e.g. a `$120` purchase = `2x$20 + 1x$50 = 90` points).

## **Features**
- Calculate the monthly reward points.
- Calculate the total reward points.
- Application hold the transaction record for the duration of 3 month.

## **Technologies Used**
- Java 17
- Spring Boot - 4.0.1
- Maven
- Rest Controller
- Swagger
- Testing using JUnit5 and Mockito

## **Postmann curl**
```bash
postman request 'http://localhost:8080/api/rewards/customer/cust001?startDate=null&endDate=null' \
  --header 'accept: */*'
```

