# Student Management REST API

A secured REST API for managing students, built with **Spring Boot 3**, **Spring Security 6**, **JWT authentication**, **Spring Data JPA** and **MySQL**.

## Features
- User registration and login with **BCrypt**-hashed passwords
- **JWT** token generated at login; every student API requires a valid token
- Full student CRUD (add, view all, view by ID, update, delete)
- Input validation and clean JSON error responses (400, 401, 404, 409)
- Layered architecture: controller, service, repository, entity, dto, security, config

## Tech Stack
Java 17 · Spring Boot 3.5 · Spring Security 6 · Spring Data JPA (Hibernate) · MySQL · jjwt 0.12 · Maven · Postman

## Project Structure
```
src/main/java/com/shubham/student_management
├── controller   # AuthController, StudentController
├── service      # AuthService, StudentService
├── repository   # UserRepository, StudentRepository
├── entity       # User, Student, Role
├── dto          # RegisterRequest, LoginRequest, AuthResponse
├── security     # JwtUtil, JwtAuthenticationFilter, CustomUserDetailsService
├── config       # SecurityConfig, PasswordConfig
└── exception    # GlobalExceptionHandler and custom exceptions
```

## Setup
**Prerequisites:** Java 17, Maven (or the included `mvnw`), MySQL 8, Postman.

1. Clone the repo
```bash
   git clone https://github.com/shu6hamm/student-management-api.git
   cd student-management-api
```
2. Create the database: run `database/student_db_script.sql` in MySQL Workbench (or just run `CREATE DATABASE student_db;`; the tables are created automatically on startup).
3. Create `src/main/resources/application-local.properties` with your own MySQL password:
```properties
   spring.datasource.password=your_mysql_password
```
If your MySQL username is not `root`, also add `spring.datasource.username=your_user`.
4. Run the app
```bash
   ./mvnw spring-boot:run        # Windows: mvnw.cmd spring-boot:run
```
The API starts at `http://localhost:8080`.

## API Endpoints

| Method | URL | Auth | Description |
|---|---|---|---|
| POST | `/api/auth/register` | No | Register a new user |
| POST | `/api/auth/login` | No | Login and receive a JWT |
| GET | `/api/students` | Bearer token | Get all students |
| GET | `/api/students/{id}` | Bearer token | Get a student by ID |
| POST | `/api/students` | Bearer token | Add a student |
| PUT | `/api/students/{id}` | Bearer token | Update a student |
| DELETE | `/api/students/{id}` | Bearer token | Delete a student |

Send the token on protected requests as: `Authorization: Bearer <token>`

### Example requests
**Register**
```json
POST /api/auth/register
{ "username": "demo_user", "email": "demo@example.com", "password": "Demo@123" }
```
**Login**
```json
POST /api/auth/login
{ "username": "demo_user", "password": "Demo@123" }
```
Response:
```json
{ "token": "eyJhbGciOi...", "type": "Bearer", "username": "demo_user", "role": "USER" }
```
**Add student**
```json
POST /api/students
{ "name": "Aarav Patil", "email": "aarav@example.com", "branch": "IT", "year": 2 }
```

## How authentication works
1. The user registers; the password is stored as a BCrypt hash.
2. On login, Spring Security checks the credentials and the server returns a signed JWT (valid for 1 hour).
3. For every later request, `JwtAuthenticationFilter` reads the `Authorization` header, validates the token and marks the request as authenticated.
4. `SecurityConfig` permits `/api/auth/**` and requires authentication for everything else. Sessions are stateless.

## Error responses
| Status | When |
|---|---|
| 400 | Validation failed or malformed JSON |
| 401 | Missing or invalid token, or wrong login credentials |
| 404 | Student not found |
| 409 | Duplicate username or email |

## Postman Screenshots
All test screenshots are in the [`postman-screenshots`](postman-screenshots/) folder.
A Postman collection is included for quick testing.

## Author
Your Name, IT Engineering