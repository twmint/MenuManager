# Menu Manager

Backend REST API for managing restaurant menus. Handles user authentication, menu items with search, and image uploads.

## Features

- User registration and login (JWT)
- Password reset by email
- Menu item CRUD with search and pagination
- Image upload for menu items

## Stack

- Java 17
- Spring Boot
- MongoDB
- Maven

## Getting Started

### Prerequisites

- JDK 17+
- MongoDB instance (Atlas or local)

### Configuration

Set the following in `src/main/resources/application.properties`:

- `spring.mongodb.uri`: MongoDB connection string
- `jwt.secret`: secret for signing JWTs
- `spring.mail.*`: SMTP settings for password reset emails
- `app.base-url`: public URL of this API
- `app.fe-base-url`: frontend URL, used in reset links

### Run

```bash
./mvnw spring-boot:run
```
