# E-Commerce Platform 🛒

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![JWT](https://img.shields.io/badge/JWT-Auth-orange.svg)](https://jwt.io/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

A full-featured e-commerce application built with Spring Boot, featuring user authentication, product management, cart functionality, order processing, and more.

## ✨ Features

- **User Management** 👤
  - Registration and authentication with JWT
  - User profiles with multiple addresses
  - Role-based access control (User/Admin)

- **Product Management** 📦
  - Product categories and tags
  - Product search and filtering
  - Product reviews and ratings
  - Featured products

- **Shopping Experience** 🛍️
  - Shopping cart functionality
  - Order processing
  - Payment integration (ready for implementation)
  - Discount and promo code system

- **Admin Dashboard** 📊
  - User management
  - Product management
  - Order tracking and management
  - Analytics (ready for implementation)

## 🔧 Technology Stack

- **Backend**: ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=flat&logo=spring-boot&logoColor=white) ![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?style=flat&logo=spring-security&logoColor=white) ![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?style=flat&logo=spring&logoColor=white)
- **Database**: ![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=mysql&logoColor=white)
- **Authentication**: ![JWT](https://img.shields.io/badge/JWT-000000?style=flat&logo=json-web-tokens&logoColor=white)
- **Build Tool**: ![Maven](https://img.shields.io/badge/Maven-C71A36?style=flat&logo=apache-maven&logoColor=white)

## 🚀 Getting Started

### Prerequisites

- ![Java](https://img.shields.io/badge/Java->=11-007396?style=flat&logo=java&logoColor=white) JDK 11 or higher
- ![Maven](https://img.shields.io/badge/Maven->=3.6-C71A36?style=flat&logo=apache-maven&logoColor=white) Maven 3.6.x or higher
- ![MySQL](https://img.shields.io/badge/MySQL->=8.0-4479A1?style=flat&logo=mysql&logoColor=white) MySQL 8.0 or higher

### Installation

1. Clone the repository:
   ```bash
   git clone https://https://github.com/Piyush12-kumar/E-Commerce_Platform
   cd ecommerce-project
   ```

2. Configure the database:
   - Create a MySQL database named `ecommerce_db`
   - Copy `application.properties.example` to `application.properties`
   - Update database credentials in `application.properties`

3. Build and run the application:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. The application will be available at `http://localhost:8080`

## 📚 API Endpoints

### 🔐 Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration
- `POST /api/auth/logout` - User logout

### 👤 User Management
- `GET /api/users/profile` - Get user profile
- `PUT /api/users/update` - Update user profile
- `POST /api/users/address/add` - Add user address

### 📦 Product Management
- `GET /api/products` - Get all products
- `GET /api/products/get/{id}` - Get product by ID
- `GET /api/products/category/{id}` - Get products by category

### 🛒 Cart Operations
- `GET /api/cart` - View cart
- `POST /api/cart/add` - Add product to cart
- `DELETE /api/cart/remove/{itemId}` - Remove item from cart
- `PUT /api/cart/update/{itemId}` - Update cart item quantity

### 📋 Order Management
- `POST /api/orders/create` - Create new order
- `GET /api/orders/{id}` - Get order details
- `GET /api/orders/user` - Get user orders

## ⚙️ Configuration

The application requires several configurations which should be set in the `application.properties` file:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db
spring.datasource.username=your_username
spring.datasource.password=your_password

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update

# JWT Configuration
jwt.secret=your_jwt_secret_key
jwt.expiration=86400000
```

## 📁 Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── example/
│   │           └── ecommerce_project/
│   │               ├── config/        # Configuration classes
│   │               ├── controller/    # REST API controllers
│   │               ├── Dao/           # Repository interfaces
│   │               ├── DTO/           # Data Transfer Objects
│   │               ├── exception/     # Exception handling
│   │               ├── model/         # Entity models
│   │               └── service/       # Business logic
│   └── resources/
│       ├── static/               # Static resources
│       │   └── images/           # Product images
│       ├── application.properties # Application configuration
│       └── templates/            # Template files
└── test/                        # Test classes
```

## 🔒 Security

The application implements security using Spring Security and JWT authentication. All sensitive endpoints are protected and require proper authentication to access.

## 📜 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

