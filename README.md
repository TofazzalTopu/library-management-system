
# Spring Boot REST API for Library Management System

Sagger UI: http://localhost:8080/swagger-ui/index.html


#Technologies Used:
- Java 17
- Spring Boot
- Spring Data JPA
- H2 Database
- Gradle
- Lombok
- Swagger/OpenAPI
- JUnit 5
- Mockito
- Flyway
- Docker
- Jenkins
- Kubernetes

# Features:
- CRUD operations for Books, Borrowers, and Borrowing Records

# SQL Queries:
CREATE TABLE borrower (
id BIGINT AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(100),
email VARCHAR(50)
);

SELECT * FROM borrower;

DELETE FROM borrower WHERE id = 1;
