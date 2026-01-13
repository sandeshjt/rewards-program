# Rewards program Service
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

## **Swagger**
Base URL: http://localhost:8080/swagger-ui/index.html#/

![img.png](img.png)

## **Endpoints**
## ***GET /api/rewards/customer/{customerId}***

```bash
curl -X 'GET' \
  'http://localhost:8080/api/rewards/customer/cust001' \
  -H 'accept: application/json'
```
```bash
{
  "customerId": "cust001",
  "customerName": "Alice",
  "totalPoints": 365,
  "monthlyPoints": [
    {
      "month": "October",
      "points": 90
    },
    {
      "month": "November",
      "points": 25
    },
    {
      "month": "December",
      "points": 250
    },
    {
      "month": "January",
      "points": 0
    }
  ],
  "transactions": [
    {
      "transactionId": "T1001",
      "customerId": "cust001",
      "amount": 120,
      "transactionDate": "2025-10-15"
    },
    {
      "transactionId": "T1002",
      "customerId": "cust001",
      "amount": 75,
      "transactionDate": "2025-11-20"
    },
    {
      "transactionId": "T1003",
      "customerId": "cust001",
      "amount": 200,
      "transactionDate": "2025-12-10"
    },
    {
      "transactionId": "T1004",
      "customerId": "cust001",
      "amount": 45,
      "transactionDate": "2026-01-05"
    }
  ]
}
````

## ***GET /api/rewards/customer/{customerId}/all***
```bash
curl -X 'GET' \
  'http://localhost:8080/api/rewards/customer/{customerId}/all?customerId=cust002' \
  -H 'accept: application/json'
```
```bash
{
  "customerId": "cust002",
  "customerName": "Bob",
  "totalPoints": 190,
  "monthlyPoints": [
    {
      "month": "January",
      "points": 10
    },
    {
      "month": "February",
      "points": 70
    },
    {
      "month": "March",
      "points": 110
    }
  ],
  "transactions": [
    {
      "transactionId": "T2001",
      "customerId": "cust002",
      "amount": 60,
      "transactionDate": "2025-01-10"
    },
    {
      "transactionId": "T2002",
      "customerId": "cust002",
      "amount": 110,
      "transactionDate": "2025-02-14"
    },
    {
      "transactionId": "T2003",
      "customerId": "cust002",
      "amount": 130,
      "transactionDate": "2026-03-22"
    }
  ]
}
```
