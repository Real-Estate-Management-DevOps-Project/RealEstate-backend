# Real Estate Management System

A comprehensive backend system for managing real estate properties, tenants, owners, leases, payments, and maintenance requests built with Spring Boot and Kotlin.

## Prerequisites

- Java 24
- PostgreSQL 16+
- Gradle 8.x

## Tech Stack

- **Framework**: Spring Boot 4.0.0-M3
- **Language**: Kotlin 2.2.10
- **Database**: PostgreSQL
- **ORM**: Spring Data JPA / Hibernate
- **Migration**: Liquibase
- **API Documentation**: SpringDoc OpenAPI (Swagger)
- **Security**: Spring Security
- **Test Data**: DataFaker

## Getting Started

### 1. Database Setup

Create a PostgreSQL database and user:

```sql
CREATE DATABASE real_estate_db;
CREATE USER realestate_user WITH PASSWORD 'realestate_pass';
GRANT ALL PRIVILEGES ON DATABASE real_estate_db TO realestate_user;
```

### 2. Configuration

The application is pre-configured in `src/main/resources/application.properties`. Update if needed:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/real_estate_db
spring.datasource.username=realestate_user
spring.datasource.password=realestate_pass
```

### 3. Run the Application

```bash
./gradlew bootRun
```

The application will:
- Run Liquibase migrations to create the database schema
- Seed the database with sample data (in dev profile)
- Start on `http://localhost:8080`

### 4. Access API Documentation

Once running, access the Swagger UI at:

**http://localhost:8080/swagger-ui.html**

## API Endpoints

### Properties
- `GET /api/properties` - List all properties
- `GET /api/properties/{id}` - Get property by ID
- `POST /api/properties` - Create new property
- `PUT /api/properties/{id}` - Update property
- `DELETE /api/properties/{id}` - Delete property

### Tenants
- `GET /api/tenants` - List all tenants
- `GET /api/tenants/{id}` - Get tenant by ID
- `POST /api/tenants` - Create new tenant
- `PUT /api/tenants/{id}` - Update tenant
- `DELETE /api/tenants/{id}` - Delete tenant

### Owners
- `GET /api/owners` - List all owners
- `GET /api/owners/{id}` - Get owner by ID
- `POST /api/owners` - Create new owner
- `PUT /api/owners/{id}` - Update owner
- `DELETE /api/owners/{id}` - Delete owner

## Authentication

The API uses HTTP Basic Authentication for protected endpoints.

**Default Credentials:**
- Username: `admin`
- Password: `admin123`

Swagger UI and API documentation endpoints are publicly accessible.

## Data Models

- **Property**: Real estate properties with details like address, type, size, bedrooms, bathrooms
- **Owner**: Property owners with contact information
- **Tenant**: Renters with contact information
- **Agent**: Real estate agents managing properties
- **Lease**: Rental agreements between tenants and properties
- **Payment**: Rent payment records
- **MaintenanceRequest**: Property maintenance and repair requests

## Development

### Running Tests

```bash
./gradlew test
```

### Building the Project

```bash
./gradlew build
```

### Database Migrations

Liquibase migrations are located in `src/main/resources/db/changelog/`

To create a new migration, add a changeset to the changelog files.

### Resetting the Database

To clear and reseed the database:

```sql
TRUNCATE TABLE owners CASCADE;
```

Then restart the application.

## Project Structure

```
src/main/kotlin/com/group3/realestate/
├── config/          # Configuration classes (Security, etc.)
├── controller/      # REST API controllers
├── models/          # JPA entities
│   └── enums/       # Enumeration types
├── repository/      # Spring Data JPA repositories
└── seeder/          # Database seeding utilities
```