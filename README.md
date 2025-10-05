# Secure Authentication System

A backend authentication service built with Spring Boot 3, JWT, BCrypt, and MySQL. It supports user login, token refresh, and role-based access control. Swagger is integrated for API documentation. The database runs on a VM-hosted MySQL instance.

## Features

- JWT-based login and refresh token flow
- Password encryption using BCrypt
- Role-based access (USER, ADMIN)
- Modular architecture: Controller, Service, Repository, Model
- MySQL integration via VM
- Built with Gradle

## Tech Stack

Language: Java 17  
Framework: Spring Boot  
Security: Spring Security, JWT  
ORM: Hibernate, JPA  
Database: MySQL  
Build Tool: Gradle

## Setup Instructions

Clone the repository and run the application:

git clone https://github.com/Raksha2401/SecureAuthenticationSystem.git  
cd SecureAuthenticationSystem  
./gradlew bootRun

## API Documentation

Swagger UI: http://localhost:9898/auth/v1/swagger-ui  
OpenAPI spec: http://localhost:9898/auth/v1/v3/api-docs

## Sample API Usage

POST /auth/v1/login  
{ "username": "testuser", "password": "testpass" }

POST /auth/v1/refreshToken  
{ "token": "<refreshToken>" }

GET /auth/v1/user/profile  
Authorization: Bearer <accessToken>

## Deployment

Runs on port 9898. Connects to MySQL hosted on a VM.