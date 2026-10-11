# ServiceHub

ServiceHub is a REST API for a service booking platform, built with Java Spring Boot. Customers can browse service providers by category, book services and leave feedback, while providers manage their bookings and profile.

## Features

- Customer and Service Provider registration, login and profile management
- Browse providers by category (public view hides email / mobile number)
- Bookings with a status life cycle (PENDING -> CONFIRMED -> COMPLETED / CANCELLED)
- Service requests (enquiries) that a provider can accept or reject
- Feedback and rating system, allowed only after a COMPLETED booking
- Provider dashboard (bookings, feedback count, average rating)
- JWT authentication with **roles** (CUSTOMER / PROVIDER)
- Swagger API documentation

## Tech Stack

- **Language:** Java 21
- **Framework:** Spring Boot 4
- **Security:** Spring Security, JWT (JJWT), BCrypt password hashing
- **Database:** MySQL, Spring Data JPA / Hibernate
- **API Docs:** Springdoc OpenAPI (Swagger UI)
- **Build Tool:** Maven

## Architecture

```
Controller  ->  Service  ->  Repository  ->  MySQL
 (HTTP/JSON)   (rules +      (Spring Data
                ownership)     JPA)
```

- **Controller:** receives the request, validates the body (`@Valid`), returns a response DTO.
- **Service:** business rules and ownership checks.
- **Repository:** database access only.
- **DTOs:** request DTOs for input, response DTOs (records) for output, so passwords and
  private data are never exposed.
- **GlobalExceptionHandler:** turns exceptions into clean HTTP codes (400, 401, 403, 404, 409).

## Security

- Passwords are hashed with **BCrypt** before being stored.
- Login returns a **JWT** containing the user's email and **role**. Every protected endpoint needs
  `Authorization: Bearer <token>`. A missing or invalid token gives **401**.
- **Layer 1 - role check (SecurityConfig):** e.g. only a CUSTOMER can create a booking,
  only a PROVIDER can open the dashboard. Wrong role gives **403**.
- **Layer 2 - ownership check (services):** a user can only read or change their own data
  (own bookings, own requests, own feedback, own dashboard). Someone else's data gives **403**.
- Sensitive configuration (DB password, JWT secret) comes from environment variables.

## Business Rules

| Area      | Rule |
|-----------|------|
| Booking   | Service date cannot be in the past; a provider can have only one active booking per day (409) |
| Booking   | Provider: PENDING -> CONFIRMED -> COMPLETED, or CANCELLED. Customer can only cancel |
| Booking   | COMPLETED and CANCELLED bookings can never change again; only a PENDING booking can be deleted |
| Request   | Provider: PENDING -> ACCEPTED / REJECTED, ACCEPTED -> COMPLETED. Customer can only cancel |
| Feedback  | Only after a COMPLETED booking with that provider; one feedback per customer per provider |

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

In IntelliJ: **Run -> Edit Configurations -> Environment variables**, and add:

```
DB_PASSWORD=your_mysql_password;JWT_SECRET=your_long_random_secret
```

### 4. Run the application

```bash
./mvnw spring-boot:run
```

The app starts on `http://localhost:8080`.

### 5. Open API documentation

http://localhost:8080/swagger-ui/index.html

## Key API Endpoints

| Method | Endpoint                          | Description                          | Who            |
|--------|-----------------------------------|--------------------------------------|----------------|
| POST   | `/customers`                      | Register a customer                  | Public         |
| POST   | `/customers/login`                | Customer login (returns JWT)         | Public         |
| POST   | `/providers`                      | Register a service provider          | Public         |
| POST   | `/providers/login`                | Provider login (returns JWT)         | Public         |
| GET    | `/providers`, `/providers/category/{name}` | Browse providers            | Logged in      |
| GET    | `/customers/{id}`                 | Own profile                          | Customer (owner) |
| GET    | `/providers/{id}`                 | Own profile                          | Provider (owner) |
| POST   | `/bookings`                       | Create a booking                     | Customer       |
| GET    | `/bookings/{id}`                  | View a booking                       | Customer or Provider of it |
| PUT    | `/bookings/{id}/status?status=`   | Change booking status                | Customer (cancel) / Provider |
| POST   | `/requests`                       | Send a service request               | Customer       |
| PUT    | `/requests/{id}/status`           | Accept / reject / cancel / complete  | Customer / Provider of it |
| POST   | `/feedbacks`                      | Rate a provider (after COMPLETED)    | Customer       |
| GET    | `/feedbacks/provider/{id}`        | Reviews of a provider                | Logged in      |
| GET    | `/dashboard/provider/{id}`        | Provider statistics                  | Provider (owner) |

Full list of endpoints is available via Swagger UI.

## Author

**Dipak Golhar**