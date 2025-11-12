# 🧩 Historical Prices Service

A clean, performant API for managing **products and their historical prices**, built using **Java 21**, **Spring Boot 3**, and **Hexagonal Architecture**.

---

## 📘 Overview

The API allows you to:
- Create products
- Add historical prices to each product
- Retrieve the active price on a specific date
- Retrieve the full price history of a product

This project is structured as a **multi-module Maven application** using a **hexagonal architecture** (domain-driven, with clear separation between business logic and infrastructure).

---

## ⚙️ Architecture and Technical Decisions

### 🧱 Architecture
- **Hexagonal (Ports and Adapters)** — ensures clean separation between:
    - `domain` → business entities and rules
    - `application` → use cases
    - `infrastructure` → REST controllers, persistence, and configuration
    - `api` → OpenAPI contract and generated DTOs (optional)

### 🧰 Tech Stack
- **Language:** Java 21
- **Framework:** Spring Boot 3.x
- **Persistence:** Spring Data JPA (H2 in-memory for testing; supports PostgreSQL easily)
- **API Specification:** OpenAPI 3 (API-first approach)
- **Testing:** JUnit 5 + Spring Boot Test
- **Containerization:** Docker + Docker Compose
- **Performance Benchmarking:** Simple bash-based load test
- **Documentation:** Swagger

---

## 📂 File Structure

```      
│ pom.xml
│ docker-compose.yml
│ Dockerfile
│ README.md
│ Historical Prices API.postman_collection.json
│
├── api
│ └── src/main/resources/openapi.yaml # OpenAPI specification
│
├── application
│ ├── price/
│ ├── product/
│ └── dto/
│
├── domain
│ ├── price/
│ ├── product/
│ └── shared/
│
└── infrastructure
├── in/rest/controller/
├── out/persistence/
├── config/
├── resources/
└── HistoricalPricesApplication.java # Primary Spring Boot application class
```

---

## 🧠 Business Rules

- Each product can have multiple prices.
- Prices **must not overlap** — defined using half-open date intervals `[start, end)`.
- `endDate` can be `null` → means “active indefinitely”.
- When querying a price for a specific date, the API returns the price active on that date.
- `initDate` must always be before `endDate` (if present).

---

## 🚀 Endpoints

### 1. Create a Product
`POST /api/products`
```json
{
  "name": "Running Shoes",
  "description": "Limited 2025 Edition"
}
```
`Response:`
```json
{
  "id": 1,
  "name": "Running Shoes",
  "description": "Limited 2025 Edition"
}
```

### 2. Add a Price to a Product
`POST /api/products/{productId}/prices`
```json
{
  "value": 99.99,
  "currency": "EUR",
  "initDate": "2024-01-01",
  "endDate": "2024-06-30"
}
```

`Response:`
```json
{
  "id": 1,
  "value": 99.99,
  "currency": "EUR",
  "initDate": "2024-01-01",
  "endDate": "2024-06-30"
}
```

### 3. Get Active Price on a Specific Date
`GET /api/products/{productId}/prices?date=2024-03-15`
`Response:`
```json
{
  "value": 99.99,
  "currency": "EUR"
}
```

### 4. Get Full Price History of a Product
`GET /api/products/{productId}/prices`
`Response:`
```json
{
  "name": "Running Shoes",
  "description": "Limited 2025 Edition",
  "prices": [
    {
      "value": 99.99,
      "currency": "EUR",
      "initDate": "2024-01-01",
      "endDate": "2024-06-30"
    },
    {
      "value": 129.99,
      "currency": "EUR",
      "initDate": "2024-07-01",
      "endDate": "2024-12-31"
    }
  ]
}
```

### 📅 Date and Currency Format
- Dates use YYYY-MM-DD and map to LocalDate.
- Currency follows ISO 4217 (^[A-Z]{3}$).

---

## 🛠️ Building the Project (Maven Multi-Module)

At the project root:
```bash
mvnw clean package -pl api -am -DskipTests
mvnw clean package -pl infrastructure -am -DskipTests
```
This builds all modules and produces the executable JAR inside:
- `infrastructure/target/infrastructure-X.X.X.jar`

### ▶️ Running Locally
After building:
```bash
java -jar infrastructure/target/historicalprices.infrastructure-X.X.X.jar
```
Swagger UI will be available at: `http://localhost:8080/api/swagger`

### 🐳 Running with Docker
docker-compose.yml is provided for easy setup:
```bash
docker-compose up
```
Also, there is a 'docker-compose.benchmark.yml' for performance benchmarking.
```bash
docker-compose -f docker-compose.benchmark.yml up
```

---

## 🧪 Testing
Run tests with:
```bash
mvnw test
```
This runs unit and integration tests across all modules.

The code coverage of the project is maintained above 80% including unit and integration tests.

---

## 📊 Postman aceptance tests
Import the provided Postman collection `Historical Prices API.postman_collection.json` to run acceptance tests against the running API.

---

## Justification and Assumptions

- **Hexagonal Architecture**: Chosen for its clear separation of concerns, making the codebase maintainable and testable.
- **Java 21 & Spring Boot 3**: Leveraged for their modern features, performance, and strong community support.
- **In-Memory Database (H2)**: Used for simplicity in testing; can be easily switched to PostgreSQL for production.
- **OpenAPI-First**: Ensures a well-defined API contract, facilitating client-server communication.
- **Dockerization**: Simplifies deployment and testing across different environments.
- **Performance Benchmarking**: Included to validate the API's performance under load, ensuring it meets expected standards.
- **Testing Strategy**: Comprehensive tests ensure reliability and correctness of business logic.




