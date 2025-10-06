# Transaction System - Microservices Architecture

A complete transaction system built with Spring Boot microservices architecture for selling products with barcode, price, and transaction date tracking.

## 🏗️ System Architecture

Client → API Gateway → Service Discovery → [Order Service, Stock Service, Payment Service]


Each service has its own independent database and communicates synchronously through REST APIs.

## 📋 Services Overview

| Service | Port | Database | Description |
|---------|------|----------|-------------|
| Eureka Server | 8761 | - | Service discovery and registration |
| API Gateway | 8080 | - | Single entry point with routing, auth, and filters |
| Order Service | 8081 | H2 (`~/orderdb`) | Handles order creation and management |
| Stock Service | 8082 | H2 (`~/stockdb`) | Manages product inventory and availability |
| Payment Service | 8083 | H2 (`~/paymentdb`) | Processes payment transactions |

## 🚀 Quick Start

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- (Optional) Redis for rate limiting

### 1. Clone and Build

```bash
# Clone the project
git clone <repository-url>
cd transaction-system

# Build all services
mvn clean package

# Or build individual service
mvn clean package -pl order-service
```
### 2. Run Services
Start services in order:
### Terminal 1 - Service Discovery
mvn spring-boot:run -pl service-discovery

### Terminal 2 - Stock Service
mvn spring-boot:run -pl stock-service

### Terminal 3 - Payment Service
mvn spring-boot:run -pl payment-service

### Terminal 4 - Order Service
mvn spring-boot:run -pl order-service

### Terminal 5 - API Gateway
mvn spring-boot:run -pl api-gateway

### 3. Verify Services
Check Eureka Dashboard: http://localhost:8761
You should see all services registered:

API-GATEWAY

ORDER-SERVICE

STOCK-SERVICE

PAYMENT-SERVICE

### Transaction Sequence

1. Client → API Gateway → Order Service (Create Order)
2. Order Service → Stock Service (Check Availability)
3. Order Service → Payment Service (Process Payment)
4. Order Service → Stock Service (Update Inventory)
5. Order Service → Client (Order Confirmation)

### Sample API Calls

1. Initialize Stock Inventory:

   POST /api/stocks

   Headers:
     ```bash
       Content-Type: application/json
       X-API-Key: test-api-key-2024
   ```
  
   Body:
```bash
   {
   "barcode": "123456",
   "productName": "Gaming Laptop",
   "price": 999.99,
   "quantity": 10
   }
```
2. Check Product Stock

   Get Stock Information

   GET /api/stocks/123456

    Headers:
```bash
   X-API-Key: test-api-key-2024
```
3. Create New Order:

   Place Order for Gaming Laptop

   POST /api/orders

    Headers:
```bash
   X-API-Key: test-api-key-2024
```
   Body:
   ```bash
   {
   "barcode": "123456",
    "price": 999.99,
    "quantity": 2
   }
```
Expected Response (Success):
 ```bash
   {
    "id": 1,
    "barcode": "123456",
    "price": 999.99,
    "quantity": 2,
    "transactionDate": "2024-01-15T10:30:00",
    "status": "CONFIRMED",
    "paymentId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890"
   }
 ```
Possible Response (Payment Failed):
```bash
   {
    "id": 1,
    "barcode": "123456",
    "price": 999.99,
    "quantity": 2,
    "transactionDate": "2024-01-15T10:30:00",
    "status": "CANCELLED",
    "paymentId": null
   }
 ```
4. Check Order Status

    Get Order Details
    
    GET /api/orders/1
    
    Headers:
```bash
   X-API-Key: test-api-key-2024
  ```
5. Check Payment Status

    Get Payment Details
    
    GET /api/payments/a1b2c3d4-e5f6-7890-abcd-ef1234567890

 Expected Response (Success):
 ```bash
   {
    "paymentId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "orderId": 1,
    "amount": 1999.98,
    "paymentDate": "2024-01-15T10:30:05",
    "status": "SUCCESS",
    "transactionId": "TXN1705314605000"
   }
 ```
Expected Response (Failure):
 ```bash
   {
     "paymentId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "orderId": 1,
    "amount": 1999.98,
    "paymentDate": "2024-01-15T10:30:05",
    "status": "FAILED",
    "transactionId": null
   }
 ```
