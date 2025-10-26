# FusionModel Microservice

## Overview

**FusionModel** is an AI model fusion microservice designed to intelligently combine responses from multiple large language models.

It can:

1. Call **doubao** and **qwen** models in parallel
2. Use the **deepseek** model to score both responses
3. Return the response with the **highest evaluation score**

---

## System Requirements

Before you begin, ensure your system meets the following requirements:

### Required
- **Java Development Kit (JDK)** - Version 17 or higher
- **Maven** - Version 3.6.0 or higher
- **Operating System** - Linux, macOS, or Windows
- **Memory** - Minimum 4GB RAM
- **Storage** - At least 1GB free space
- **Network** - Internet connection for downloading dependencies
- **Port Availability** - Port 8081 must be free

### Optional but Recommended
- **IDE** - IntelliJ IDEA, Eclipse, or VS Code with Java extensions
- **Docker** - Version 20.10.0 or higher (if using containerization)
- **Git** - Version 2.0 or higher (for version control)

### Development Tools Versions
```bash
# Verify your installations with these commands
java --version        # Should show Java 17 or higher
mvn --version        # Should show Maven 3.6.0 or higher
git --version        # Should show Git 2.0 or higher (if using)
docker --version     # Should show Docker 20.10.0 or higher (if using)
```

---

## Configuration

The service runs on **port 8081**, and the AI gateway is configured in the `application.yml` file:

```yaml
server:
  port: 8081

aigateway:
  baseurl: http://localhost:8080/chat/completions
```

---

## Dependencies

The project uses the following key dependencies:

- **Spring Boot 3.5.6** - Base framework and web server
- **Spring WebFlux** - Reactive web support for parallel API calls
- **Spring Validation** - Request parameter validation
- **Spring Actuator** - Health checks and monitoring
- **Lombok** - Reduces boilerplate code
- **Jackson** - JSON processing
- **SpringDoc OpenAPI** - API documentation (Swagger UI)

You can find the full dependency list in `pom.xml`:

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-webflux</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springdoc</groupId>
        <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
        <version>2.2.0</version>
    </dependency>
</dependencies>
```

---

## API Endpoints

### 1. Get the Best Response

**Endpoint:**  
`POST /api/chat/best-response`

**Description:**  
Sends a chat message to the microservice, which will:

- Send the same query to both **doubao** and **qwen** models
- Evaluate both results using the **deepseek** model
- Return the response with the highest score

**Request Example:**

```json
{
  "messages": [
    {
      "role": "user",
      "content": "Hello, please introduce the history of artificial intelligence."
    }
  ]
}
```

**Response Example:**

```json
{
  "model": "doubao",
  "content": "AI originated in the 1950s with the idea of building machines capable of intelligent behavior...",
  "score": 8.5
}
```

---

### 2. Health Check

**Endpoint:**  
`GET /api/chat/health`

**Description:**  
Used to verify that the service is running properly.  
A typical response might include a simple status message or HTTP 200 OK.

---

## Running the Service

You can start the service using Maven or directly with the generated JAR file.

**Option 1 — Using Maven:**

```bash
mvn spring-boot:run
```

**Option 2 — Using the JAR file:**

```bash
java -jar target/FusionModel-0.0.1-SNAPSHOT.jar
```

---

## Testing Example

You can test the API using `curl`:

```bash
curl -X POST http://localhost:8081/api/chat/best-response   -H "Content-Type: application/json"   -d '{
    "messages": [
      {
        "role": "user",
        "content": "What is machine learning?"
      }
    ]
  }'
```

**Expected Response:**

```json
{
  "model": "qwen",
  "content": "Machine learning is a subfield of artificial intelligence that enables systems to learn from data without explicit programming.",
  "score": 9.2
}
```

---

## Notes

- The service currently supports **doubao**, **qwen**, and **deepseek** models.
- Model selection and scoring logic can be customized in the source code.
- Make sure the **AI Gateway (port 8080)** is running before starting the FusionModel service.

---

© 2025 FusionModel Project — All rights reserved.