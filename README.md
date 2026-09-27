# ServiceHub

ServiceHub is a REST API for a business service management platform, built with Java Spring Boot. It allows customers to browse service providers by category, book services, and leave feedback — while providers manage their bookings and profile.

## Features

- Customer registration, login, and profile management
- Service Provider registration, login, and profile management
- Service category and provider browsing
- Booking management (create, view, update status)
- Feedback and rating system
- JWT-based authentication
- Role-based access — users can only view/update/delete their own data
- Swagger API documentation

## Tech Stack

- **Language:** Java 21
- **Framework:** Spring Boot 4
- **Security:** Spring Security, JWT (JJWT), BCrypt password hashing
- **Database:** MySQL, Spring Data JPA / Hibernate
- **Testing:** JUnit 5, Mockito
- **API Docs:** Springdoc OpenAPI (Swagger UI)
- **Build Tool:** Maven

## Security

- Passwords are hashed with **BCrypt** before being stored — never saved as plain text.
- Authentication is handled via **JWT tokens**; protected endpoints require a valid `Authorization: Bearer <token>` header.
- **Ownership-based authorization** — a logged-in customer or provider can only view, update, or delete their own record, not anyone else's.
- Sensitive configuration (DB password, JWT secret) is externalized via environment variables, not hardcoded.

## Getting Started

### Prerequisites

- Java 21
- Maven (or use the included `mvnw` wrapper)
- MySQL 8+

### 1. Clone the repository

```bash
git clone https://github.com/dipak-7507/ServiceHub.git
cd ServiceHub
```

### 2. Create the database

```sql
CREATE DATABASE servicehub;
```

### 3. Set environment variables

The app reads DB credentials and the JWT secret from environment variables:

| Variable      | Description                              |
|---------------|-------------------------------------------|
| `DB_USERNAME` | MySQL username (defaults to `root`)       |
| `DB_PASSWORD` | MySQL password                            |
| `JWT_SECRET`  | Any long random string (32+ characters)   |

In IntelliJ: **Run → Edit Configurations → Environment variables**, and add:

DB_PASSWORD=your_mysql_password;JWT_SECRET=your_long_random_secret


### 4. Run the application

```bash
./mvnw spring-boot:run
```

The app starts on `http://localhost:8080`.

### 5. Open API documentation

http://localhost:8080/swagger-ui/index.html


## Key API Endpoints

| Method | Endpoint                          | Description                        | Auth Required |
|--------|------------------------------------|-------------------------------------|----------------|
| POST   | `/customers`                       | Register a new customer             | No             |
| POST   | `/customers/login`                 | Customer login (returns JWT)        | No             |
| GET    | `/customers/{id}`                  | Get customer by ID (owner only)     | Yes            |
| POST   | `/providers`                       | Register a new service provider     | No             |
| POST   | `/providers/login`                 | Provider login (returns JWT)        | No             |
| GET    | `/providers/category/{name}`       | Get providers by category           | Yes            |
| POST   | `/bookings`                        | Create a booking                    | Yes            |
| GET    | `/bookings/{id}`                   | Get booking by ID                   | Yes            |

Full list of endpoints is available via Swagger UI.

## Running Tests

```bash
./mvnw test
```

Unit tests for the service layer are written using JUnit 5 and Mockito.

## Author

**Dipak Golhar**