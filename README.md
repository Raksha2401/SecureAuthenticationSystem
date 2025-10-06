# Secure Authentication System

A backend authentication service built with Spring Boot 3, JWT, BCrypt, and MySQL. It supports user login, token refresh, and role-based access control. Swagger is integrated for API documentation. The database runs on a VM-hosted MySQL instance.

---

## Features

- JWT-based login and refresh token flow
- Password encryption using BCrypt
- Role-based access (USER, ADMIN)
- Modular architecture: Controller, Service, Repository, Model
- MySQL integration via VM
- Built with Gradle
- Dockerized for easy deployment

---

## Tech Stack

- **Language:** Java 21
- **Framework:** Spring Boot 3
- **Security:** Spring Security, JWT
- **ORM:** Hibernate, JPA
- **Database:** MySQL
- **Build Tool:** Gradle
- **Containerization:** Docker

---

## Setup Instructions (Local)

1. Clone the repository:

```bash
git clone https://github.com/Raksha2401/SecureAuthenticationSystem.git
cd SecureAuthenticationSystem
```

2. Build and run the application:

```bash
./gradlew clean build
./gradlew bootRun
```

> **Note:** Make sure to build the project first. The jar file (`SecureAuthenticationSystem-plain.jar`) will be generated in `app/build/libs/` and is required for running the Docker image.

The service runs on port `9898` by default.

---

## Run with Docker

1. Build the Docker image:

```bash
docker build -t secure-auth .
```

2. Run the container:

```bash
docker run -p 9898:9898 secure-auth
```

3. Access the service:

```
http://localhost:9898
```

> Ensure the Dockerfile copies the correct jar (`app/build/libs/SecureAuthenticationSystem-plain.jar`) and uses **Java 21** to match the compiled classes.

---

## API Documentation

- **Swagger UI:** http://localhost:9898/auth/v1/swagger-ui
- **OpenAPI spec:** http://localhost:9898/auth/v1/v3/api-docs

---

## Sample API Usage

- **POST /auth/v1/login**

```json
{ "username": "testuser", "password": "testpass" }
```

- **POST /auth/v1/refreshToken**

```json
{ "token": "<refreshToken>" }
```

- **GET /auth/v1/user/profile**

```
Authorization: Bearer <accessToken>
```

---

## Deployment

- Runs on port `9898` (local)
- Docker container exposes port `9898`
- Connects to MySQL hosted on a VM
- Gradle wrapper included, no need to install Gradle globally

---

## Notes

- Docker uses Java 21; local builds should match the Java version
- Swagger is integrated for API testing and documentation