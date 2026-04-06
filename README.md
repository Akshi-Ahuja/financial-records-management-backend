# Finance Data Processing and Access Control Backend

A role-based finance dashboard backend built with **Java 21**, **Spring Boot**, **Spring Security**, **JWT Authentication**, and **MySQL**. Designed for managing financial records, enforcing access control, and serving analytics to a frontend dashboard.

---

## Table of Contents

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Roles & Permissions](#roles--permissions)
- [Modules](#modules)
  - [Authentication](#authentication)
  - [User Management](#user-management)
  - [Financial Records](#financial-records)
  - [Dashboard & Analytics](#dashboard--analytics)
- [API Reference](#api-reference)
- [Security Design](#security-design)
- [Database Schema](#database-schema)
- [Setup & Running Locally](#setup--running-locally)
- [Design Decisions & Assumptions](#design-decisions--assumptions)
- [What Was Intentionally Not Implemented](#what-was-intentionally-not-implemented)

---

## Overview

This backend powers a **finance dashboard system** where users with different roles interact with financial data. The system supports:

- Secure JWT-based authentication
- Role-based access control (VIEWER / ANALYST / ADMIN)
- Full financial record CRUD with filtering, pagination, search, and sorting
- Aggregated dashboard analytics (summary, trends, category totals)
- Proper input validation and structured error responses

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot |
| Security | Spring Security + JWT |
| Database | MySQL |
| ORM | JPA (Hibernate) |
| Utilities | Lombok |

---

## Architecture

The project follows a clean **layered architecture** with clear separation of concerns:
```
src/
├── controller/         → HTTP layer, route definitions
├── service/            → Business logic interfaces
├── service/impl/       → Concrete service implementations
├── repository/         → JPA repositories + Specification queries
├── entity/             → Database models
├── dto/
│   ├── request/        → Incoming request payloads
│   └── response/       → Outgoing response payloads
├── enums/              → Role, Status, RecordType enums
├── exception/          → Custom exceptions + GlobalExceptionHandler
├── security/           → JWT filter, UserDetails, entry points
└── config/             → Security config, DataInitializer
```

**Key principles followed:**
- Controllers only handle HTTP — no business logic
- Services return DTOs, never `ResponseEntity`
- Builder pattern used consistently across DTOs
- `createdBy` is always derived from the authenticated user, never from the request body
- Specification-based dynamic filtering for records

---

## Roles & Permissions

| Action | VIEWER | ANALYST | ADMIN |
|---|:---:|:---:|:---:|
| View financial records | ✅ | ✅ | ✅ |
| Filter / search records | ✅ | ✅ | ✅ |
| View summary (income/expense/balance) | ✅ | ✅ | ✅ |
| View recent activity | ✅ | ✅ | ✅ |
| View category totals | ❌ | ✅ | ✅ |
| View monthly trends | ❌ | ✅ | ✅ |
| Create financial records | ❌ | ❌ | ✅ |
| Update financial records | ❌ | ❌ | ✅ |
| Delete financial records | ❌ | ❌ | ✅ |
| Manage users (create/update/status/role) | ❌ | ❌ | ✅ |

---

## Modules

### Authentication

**Endpoint:**
```
POST /api/v1/auth/login
```

**Request:**
```json
{
  "email": "admin@zorvyn.in",
  "password": "yourpassword"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

All protected routes require the header:
```
Authorization: Bearer <token>
```

A default **admin user** is bootstrapped on application startup via `DataInitializer` if no admin exists.

---

### User Management

> **Access: ADMIN only**

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/v1/users` | Create a new user |
| GET | `/api/v1/users` | Get all users |
| GET | `/api/v1/users/{id}` | Get user by ID |
| PATCH | `/api/v1/users/{id}` | Partial update (name, email, password) |
| PATCH | `/api/v1/users/{id}/status` | Update user status (ACTIVE / INACTIVE) |
| PATCH | `/api/v1/users/{id}/role` | Update user role (VIEWER / ANALYST / ADMIN) |

**Validations:**
- Email must end with `@zorvyn.in`
- Email must be unique
- Minimum password length enforced
- Default role on creation: `VIEWER`
- Default status on creation: `ACTIVE`

---

### Financial Records

> **Access: Read → VIEWER+, Write → ADMIN only**

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/v1/records` | Create a financial record |
| GET | `/api/v1/records` | Get all records (with filters, pagination, search) |
| GET | `/api/v1/records/{id}` | Get record by ID |
| PATCH | `/api/v1/records/{id}` | Partial update a record |
| DELETE | `/api/v1/records/{id}` | Delete a record |

**Record Fields:**

| Field | Type | Notes |
|---|---|---|
| `amount` | Decimal | Required, must be positive |
| `type` | Enum | `INCOME` or `EXPENSE` |
| `category` | String | Validated against type |
| `date` | Date | Record date |
| `description` | String | Optional notes |

**Filtering & Query Parameters for `GET /api/v1/records`:**

| Parameter | Description |
|---|---|
| `type` | Filter by `INCOME` or `EXPENSE` |
| `category` | Filter by category name |
| `startDate` | Filter from date (inclusive) |
| `endDate` | Filter to date (inclusive) |
| `search` | Partial match on description (case-insensitive) |
| `page` | Page number (default: 0) |
| `size` | Page size (default: 10) |

**Example:**
```
GET /api/v1/records?type=EXPENSE&category=Office&search=supplies&page=0&size=5
```

**Paginated Response:**
```json
{
  "content": [...],
  "page": 0,
  "size": 5,
  "totalElements": 23,
  "totalPages": 5
}
```

---

### Dashboard & Analytics

> **Access: varies by endpoint (see Roles & Permissions table)**

| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/api/v1/dashboard/summary` | VIEWER+ | Total income, total expense, net balance |
| GET | `/api/v1/dashboard/category-totals` | ANALYST+ | Totals grouped by category |
| GET | `/api/v1/dashboard/recent-activity` | VIEWER+ | Latest N financial records |
| GET | `/api/v1/dashboard/monthly-trends` | ANALYST+ | Monthly income/expense breakdown |

**Sample Summary Response:**
```json
{
  "totalIncome": 150000.00,
  "totalExpense": 87500.00,
  "netBalance": 62500.00
}
```

**Sample Monthly Trends Response:**
```json
[
  { "month": "2025-01", "income": 50000.00, "expense": 30000.00 },
  { "month": "2025-02", "income": 45000.00, "expense": 27500.00 }
]
```

---

## Security Design

### JWT Authentication Flow
```
Client → POST /api/v1/auth/login → Server validates credentials
       ← JWT token returned

Client → Any protected route (with Bearer token)
       → JwtAuthenticationFilter extracts & validates token
       → Sets SecurityContext
       → Request proceeds or returns 401/403
```

### Components

| Component | Role |
|---|---|
| `JwtService` | Token generation and validation |
| `JwtAuthenticationFilter` | Intercepts requests, validates tokens |
| `CustomUserDetailsService` | Loads user by email for Spring Security |
| `CustomAuthenticationEntryPoint` | Returns 401 for unauthenticated requests |
| `CustomAccessDeniedHandler` | Returns 403 for unauthorized role access |

### Password Security

Passwords are stored as **BCrypt hashes** — never in plain text. Login uses `passwordEncoder.matches()` for verification.

---

## Database Schema

### `users`

| Column | Type | Notes |
|---|---|---|
| `id` | BIGINT | Primary key |
| `name` | VARCHAR | Required |
| `email` | VARCHAR | Unique, must be `@zorvyn.in` |
| `password` | VARCHAR | BCrypt hashed |
| `role` | ENUM | `VIEWER`, `ANALYST`, `ADMIN` |
| `status` | ENUM | `ACTIVE`, `INACTIVE` |
| `created_at` | TIMESTAMP | Auto-set |

### `financial_records`

| Column | Type | Notes |
|---|---|---|
| `id` | BIGINT | Primary key |
| `amount` | DECIMAL | Required, positive |
| `type` | ENUM | `INCOME`, `EXPENSE` |
| `category` | VARCHAR | Validated against type |
| `date` | DATE | Record date |
| `description` | TEXT | Optional |
| `created_by` | BIGINT | FK → users.id |
| `created_at` | TIMESTAMP | Auto-set |
| `updated_at` | TIMESTAMP | Auto-updated |

---

## Setup & Running Locally

### Prerequisites

- Java 21
- MySQL 8+
- Maven

### 1. Clone the Repository
```bash
git clone https://github.com/Akshi-Ahuja/financial-records-management-backend.git
cd financial-records-management-backend
```

### 2. Configure the Database

Create a MySQL database:
```sql
CREATE DATABASE finance_dashboard_db;
```

In `src/main/resources/application.properties`, fill in your own values for the following:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/finance_dashboard_db
spring.datasource.username=YOUR_MYSQL_USERNAME
spring.datasource.password=YOUR_MYSQL_PASSWORD

spring.jpa.hibernate.ddl-auto=update

app.jwt.secret=YOUR_JWT_SECRET_KEY
app.jwt.expiration=86400000
```

### 3. Build and Run
```bash
mvn clean install
mvn spring-boot:run
```

The server starts at: `http://localhost:8080/api/v1`

### 4. Default Admin User

On first startup, a default admin is automatically created via `DataInitializer`:
```
Email:    admin@zorvyn.in
Password: admin@123
Role:     ADMIN
```

### 5. Test the API

**Login and get token:**
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@zorvyn.in","password":"admin@123"}'
```

**Use the token:**
```bash
curl http://localhost:8080/api/v1/dashboard/summary \
  -H "Authorization: Bearer <your_token>"
```

---

## Error Handling

All errors return consistent, structured JSON responses:
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "User not found with id: 5",
  "timestamp": "2025-04-06T18:30:00"
}
```

| Scenario | HTTP Status |
|---|---|
| Resource not found | 404 |
| Duplicate email | 409 |
| Invalid email domain | 400 |
| Validation errors | 400 |
| Unauthorized (no token) | 401 |
| Forbidden (wrong role) | 403 |
| Internal server error | 500 |

**Custom Exceptions:**
- `UserNotFoundException`
- `FinancialRecordNotFoundException`
- `DuplicateEmailException`
- `InvalidEmailDomainException`
- `InvalidOperationException`

---

## Design Decisions & Assumptions

| Decision | Reasoning |
|---|---|
| Email domain restricted to `@zorvyn.in` | Matches company domain for an internal system |
| `createdBy` from JWT, not request body | Prevents users from spoofing record ownership |
| Specification-based filtering | Allows clean combination of multiple dynamic filters without query explosion |
| Services return DTOs, not `ResponseEntity` | Keeps service layer framework-agnostic and testable |
| Partial updates via `PATCH` | Avoids overwriting unchanged fields, follows REST best practices |
| BCrypt for password hashing | Industry standard — safe even if DB is compromised |
| Builder pattern on all DTOs | Readable, immutable object construction |
| Default role = VIEWER on creation | Principle of least privilege — admins explicitly promote users |
| DataInitializer for admin bootstrap | Ensures system is usable from first startup without manual DB seeding |
| Base URL `/api/v1` | Follows REST versioning conventions for maintainability |

---

## What Was Intentionally Not Implemented

| Feature | Reason |
|---|---|
| Swagger / OpenAPI docs | Not required for assignment scope; API is fully documented in this README |
| Unit / integration tests | Prioritized complete feature coverage and code quality over test suite |
| Rate limiting | Beyond internship assignment scope |
| Soft delete | Not required; hard delete is sufficient for this use case |
| Pagination on user endpoints | Low volume expected; not a meaningful use case for user management |

---

## Project Summary

This backend demonstrates:

- ✅ Clean layered architecture with proper separation of concerns
- ✅ Secure authentication with JWT + BCrypt
- ✅ Role-based access control at the method level
- ✅ Full CRUD with dynamic filtering, pagination, sorting, and search
- ✅ Aggregated analytics for a finance dashboard
- ✅ Consistent validation and structured error handling
- ✅ Thoughtful design decisions documented and justified

---

*Built as part of the Backend Developer Intern screening assignment for Zorvyn Fintech.*
