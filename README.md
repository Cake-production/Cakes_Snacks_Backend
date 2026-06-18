# Cakes & Snacks - Spring Boot Backend API

A complete, production-ready Spring Boot backend for the Cakes & Snacks premium artisanal bakery e-commerce platform.

## 📋 Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Database Schema](#database-schema)
- [Entity List](#entity-list)
- [API Endpoints](#api-endpoints)
- [Setup & Installation](#setup--installation)
- [Configuration](#configuration)
- [Database Migration](#database-migration)
- [Authentication](#authentication)
- [API Documentation](#api-documentation)

## 🎯 Overview

This backend provides complete RESTful APIs for:
- **Customer Management** - User registration, login, profile management
- **Product Management** - CRUD operations, category filtering, top sellers
- **Order Management** - Create orders, track status, order history
- **Payment Processing** - Payment method management, transaction processing
- **Analytics & Reporting** - Dashboard stats, revenue tracking, top products
- **Delivery Tracking** - Order delivery status and tracking
- **Reviews & Ratings** - Product reviews and customer feedback
- **Notification Preferences** - User notification settings

## 🏗️ Architecture

### Technology Stack
- **Framework**: Spring Boot 3.1.5
- **Database**: MySQL 8.0
- **ORM**: Hibernate/JPA
- **Migration**: Liquibase
- **Authentication**: JWT (JSON Web Tokens)
- **Security**: Spring Security
- **Build**: Maven
- **Java Version**: 17

### Project Structure
```
cakes-backend/
├── src/main/java/com/cakesandsnacks/
│   ├── config/              # Spring configuration classes
│   ├── controller/          # REST API endpoints
│   ├── dto/                 # Data Transfer Objects
│   ├── entity/              # JPA entities
│   ├── exception/           # Exception handling
│   ├── repository/          # Data access layer
│   ├── security/            # JWT and security
│   ├── service/             # Business logic layer
│   └── CakesAndSnacksApplication.java
├── src/main/resources/
│   ├── db/migration/        # Liquibase migration files
│   ├── application.yml      # Application configuration
│   └── application-prod.yml # Production configuration
└── pom.xml
```

## 📊 Database Schema

### Entity Relationships

```
User (1) -----> (Many) Orders
User (1) -----> (Many) Addresses
User (1) -----> (Many) PaymentMethods
User (1) -----> (Many) Reviews
User (1) -----> (1) UserProfile
User (1) -----> (Many) NotificationPreferences

Order (1) -----> (Many) OrderItems
Order (1) -----> (1) Payment
Order (1) -----> (1) Delivery

Product (1) -----> (Many) OrderItems
Product (1) -----> (Many) Reviews

PaymentMethod (1) -----> (Many) Orders
```

## 🔧 Entity List

### 1. **User Entity**
Represents users with role-based access (CUSTOMER, MANAGER, ADMIN)
- Fields: email, password, firstName, lastName, phone, role, profileImage, isActive, emailVerified
- Relations: orders, addresses, paymentMethods, reviews, userProfile, notificationPreferences

### 2. **Product Entity**
Represents bakery products available for purchase
- Fields: name, description, price, stock, category, badge, imageUrl, rating, reviewCount, sales
- Relations: orderItems, reviews

### 3. **Order Entity**
Represents customer orders
- Fields: orderNumber, status, subtotal, shippingCost, taxAmount, totalAmount, deliveryOption
- Relations: user, orderItems, payment, delivery, shippingAddress, paymentMethod

### 4. **OrderItem Entity**
Represents individual items within orders
- Fields: quantity, unitPrice, lineTotal, specialInstructions
- Relations: order, product

### 5. **Address Entity**
Stores customer addresses (billing/shipping)
- Fields: firstName, lastName, street, city, state, zipCode, country, phone, instructions, isDefault, addressType
- Relations: user

### 6. **PaymentMethod Entity**
Stores customer payment methods
- Fields: paymentType, cardBrand, lastFourDigits, expiryMonth, expiryYear, cardholderName, stripePaymentMethodId, isDefault, isActive
- Relations: user, orders

### 7. **Payment Entity**
Records payment transactions
- Fields: transactionId, amount, status, paymentMethod, responseData
- Relations: order

### 8. **Delivery Entity**
Tracks order delivery information
- Fields: status, trackingNumber, carrier, estimatedDeliveryDate, actualDeliveryDate, deliveryNotes, driverName, driverPhone
- Relations: order

### 9. **Review Entity**
Product reviews and ratings from customers
- Fields: rating, title, comment, isVerifiedPurchase, helpfulCount
- Relations: product, user

### 10. **UserProfile Entity**
Extended user profile information
- Fields: dateOfBirth, companyName, businessType, bio, preferredLanguage, timezone, twoFactorEnabled, loyaltyPoints, membershipTier
- Relations: user

### 11. **NotificationPreference Entity**
User notification settings
- Fields: notificationType, emailEnabled, smsEnabled, pushEnabled, inAppEnabled
- Relations: user

## 🔌 API Endpoints

### Authentication
```
POST /api/auth/register          - Register new user
POST /api/auth/login             - Login user
POST /api/auth/refresh-token     - Refresh JWT token
POST /api/auth/logout            - Logout user
```

### Users
```
GET /api/users/{id}              - Get user by ID
GET /api/users                   - Get all customers
GET /api/users/email/{email}     - Get user by email
PUT /api/users/{id}              - Update user profile
DELETE /api/users/{id}           - Delete user (deactivate)
```

### Products
```
GET /api/products                - Get all products
GET /api/products/{id}           - Get product by ID
GET /api/products/category/{category}  - Get products by category
GET /api/products/top-selling    - Get top selling products
GET /api/products/latest         - Get latest products
POST /api/products               - Create product (MANAGER only)
PUT /api/products/{id}           - Update product (MANAGER only)
DELETE /api/products/{id}        - Delete product (MANAGER only)
```

### Orders
```
POST /api/orders                 - Create order
GET /api/orders/{id}             - Get order by ID
GET /api/orders/user/{userId}    - Get user's orders
PUT /api/orders/{id}/status      - Update order status
DELETE /api/orders/{id}/cancel   - Cancel order
```

### Payments
```
POST /api/payments/process       - Process payment
GET /api/payments/{id}           - Get payment by ID
GET /api/payments/order/{orderId} - Get payment for order
POST /api/payments/{id}/refund   - Refund payment
```

### Analytics
```
GET /api/analytics/dashboard     - Get dashboard statistics
GET /api/analytics/top-products  - Get top selling products
GET /api/analytics/monthly-stats - Get monthly statistics
GET /api/analytics/revenue       - Get total revenue
GET /api/analytics/orders-count  - Get total orders count
GET /api/analytics/customers-count - Get total customers count
```

## 📦 Setup & Installation

### Prerequisites
- Java 17 or higher
- Maven 3.8+
- MySQL 8.0+
- Git

### Step 1: Clone Repository
```bash
git clone <repository-url>
cd cakes-backend
```

### Step 2: Create Database
```sql
CREATE DATABASE cakes_and_snacks;
CREATE USER 'cakes_user'@'localhost' IDENTIFIED BY 'secure_password';
GRANT ALL PRIVILEGES ON cakes_and_snacks.* TO 'cakes_user'@'localhost';
FLUSH PRIVILEGES;
```

### Step 3: Configure Database
Edit `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/cakes_and_snacks
    username: cakes_user
    password: secure_password
```

### Step 4: Install Dependencies
```bash
mvn clean install
```

### Step 5: Run Database Migrations
```bash
mvn liquibase:update
```

### Step 6: Start Application
```bash
mvn spring-boot:run
```

The API will be available at: `http://localhost:8080/api`

## ⚙️ Configuration

### Environment Variables
Create `.env` file or set system environment variables:
```
JWT_SECRET=your-secret-key-min-32-characters
STRIPE_API_KEY=sk_test_your_stripe_key
STRIPE_WEBHOOK_SECRET=whsec_your_webhook_secret
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password
```

### Application Properties
```yaml
# Server
server:
  port: 8080
  servlet:
    context-path: /api

# Database
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/cakes_and_snacks
    username: root
    password: root
    
# JWT
jwt:
  secret: your-secret-key
  expiration: 86400000  # 24 hours
  refresh-token-expiration: 604800000  # 7 days
```

## 🗄️ Database Migration

### How Liquibase Works
The project uses Liquibase for version-controlled database migrations. Migrations are executed automatically on application startup.

### Migration Files
```
src/main/resources/db/migration/
├── master.xml                              # Master changelog
├── 001-create-users-table.xml
├── 002-create-addresses-table.xml
├── 003-create-products-table.xml
├── 004-create-orders-table.xml
├── 005-create-order-items-table.xml
├── 006-create-payment-methods-table.xml
├── 007-create-payments-table.xml
├── 008-create-deliveries-table.xml
├── 009-create-reviews-table.xml
├── 010-create-user-profiles-table.xml
├── 011-create-notification-preferences-table.xml
├── 012-create-indexes.xml
└── 013-insert-initial-data.xml
```

### Adding New Migrations
1. Create new XML file in `db/migration/` directory
2. Follow naming convention: `NNN-description.xml`
3. Add include statement in `master.xml`
4. Run `mvn liquibase:update`

## 🔐 Authentication

### JWT Flow
1. User logs in with email/password
2. Server validates credentials
3. Server generates JWT token
4. Client includes token in Authorization header: `Bearer <token>`
5. Server validates token on each request

### JWT Token Structure
```
Header: {
  "alg": "HS512",
  "typ": "JWT"
}

Payload: {
  "sub": "user@example.com",
  "role": "CUSTOMER",
  "iat": 1234567890,
  "exp": 1234571490
}
```

### Example Login Request
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "customer@cakesandsnacks.com",
    "password": "password123"
  }'
```

### Example Protected Request
```bash
curl -X GET http://localhost:8080/api/users/1 \
  -H "Authorization: Bearer <your-jwt-token>"
```

## 📖 API Documentation

### Request/Response Format
All API requests and responses use JSON format.

### Status Codes
- `200 OK` - Request succeeded
- `201 Created` - Resource created successfully
- `400 Bad Request` - Invalid input
- `401 Unauthorized` - Missing or invalid token
- `403 Forbidden` - Insufficient permissions
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error

### Sample Request
```json
{
  "email": "customer@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "phone": "+1-555-0101",
  "password": "SecurePassword123!"
}
```

### Sample Response
```json
{
  "id": 1,
  "email": "customer@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "phone": "+1-555-0101",
  "role": "CUSTOMER",
  "isActive": true,
  "createdAt": "2024-01-15T10:30:00"
}
```

## 🧪 Testing

### Run Tests
```bash
mvn test
```

### Test Coverage
```bash
mvn clean test jacoco:report
```

## 🚀 Deployment

### Build for Production
```bash
mvn clean package -DskipTests
```

### Docker Setup
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/cakes-backend-1.0.0.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

## 📝 Logging

### Configuration
Edit `application.yml`:
```yaml
logging:
  level:
    root: INFO
    com.cakesandsnacks: DEBUG
    org.springframework.web: INFO
```

### Log Locations
- Console: Real-time logs
- File: `logs/application.log`

## 🔄 API Integration with Frontend

### CORS Configuration
The backend is configured to accept requests from:
- `http://localhost:3000` (React dev server)
- `http://localhost:3001` (Alternative port)

### Example Frontend Integration
```javascript
const API_URL = 'http://localhost:8080/api';

// Login
const response = await fetch(`${API_URL}/auth/login`, {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({ email, password })
});
const data = await response.json();
localStorage.setItem('token', data.token);

// Authenticated request
const productsResponse = await fetch(`${API_URL}/products`, {
  headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` }
});
```

## 🛠️ Troubleshooting

### Database Connection Error
```
Solution: Verify MySQL is running and credentials in application.yml are correct
```

### Migration Error
```
Solution: Check Liquibase XML syntax and run mvn liquibase:update
```

### JWT Token Error
```
Solution: Ensure JWT_SECRET environment variable is set correctly
```

### CORS Error
```
Solution: Add frontend URL to corsRegistry in CakesAndSnacksApplication.java
```

## 📞 Support

For issues or questions:
1. Check application logs: `logs/application.log`
2. Verify database migrations: `DATABASECHANGELOG` table
3. Review API documentation in this README
4. Check Spring Boot error messages

## 📄 License

MIT License - See LICENSE file for details

---

**Created for Cakes & Snacks Premium Bakery E-commerce Platform**
**Version 1.0.0 | Last Updated: 2024**
