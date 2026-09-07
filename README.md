# Pensionat Booking Backend

Booking and room management API for the Pensionat Booking System, built with Java and Spring Boot.

The application manages rooms, availability and the complete booking lifecycle. It stores only the customer ID associated with each booking and communicates with the Customer Service to verify that a customer exists before creating a booking.

---

## Related Repositories

**Customer Service:** [pensionat-customer-service](https://github.com/igor-gomes-academic/pensionat-customer-service)

**Frontend:** [pensionat-booking-frontend](https://github.com/igor-gomes-academic/pensionat-booking-frontend)

---

## Architecture Overview

```text
User
  │
  ▼
Frontend
  │
  │ Booking and room requests
  ▼
Booking Service
  ├── Booking and room data ──► Booking MySQL Database
  │
  └── Customer existence check before booking creation ──► Customer Service
```

Each service owns its own database. The Booking Service never reads from or writes directly to the Customer Service database.

- The Frontend provides the user interface
- The Booking Service manages rooms and bookings
- The Customer Service manages customer accounts

---

## Technologies

- Java 17
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Hibernate
- MySQL
- Maven
- Dotenv
- Bean Validation
- JUnit
- Docker
- Docker Compose

---

## Project Structure

The Booking Service is organized into separate layers:

- **Controller layer:** Handles booking and room API requests and responses
- **Service layer:** Contains booking, room and availability business logic
- **Repository layer:** Handles database access through Spring Data JPA
- **Client layer:** Handles REST communication with the Customer Service
- **Entity classes:** Represent booking and room database tables
- **DTOs:** Define request and response data
- **Exceptions:** Provide clear error handling and HTTP status codes
- **Configuration:** Provides CORS, security and initial room data configuration

---

## Functionality

The Booking Service supports:

1. Retrieving all rooms
2. Creating rooms
3. Searching for available rooms by date interval
4. Retrieving all bookings
5. Creating bookings
6. Updating bookings
7. Cancelling bookings
8. Preventing overlapping active bookings for the same room
9. Handling room types and extra beds
10. Verifying that a customer exists before creating a booking
11. Checking whether a customer has active bookings
12. Returning DTO responses with appropriate HTTP status codes

---

## Database

The Booking Service uses its own MySQL database.

The main entities are:

- **BookingEntity**
- **RoomEntity**

The Booking Service does not store customer information. Each booking contains only the customer ID supplied by the Customer Service domain.

Ten rooms are added automatically when the booking database is empty.

---

## Business Rules

- A customer must exist before a booking can be created
- The check-out date must be after the check-in date
- A room cannot have overlapping active bookings
- Extra beds are only available for double rooms
- A cancelled booking cannot be updated
- A booking can only be updated using the customer ID associated with it
- The Customer Service can check for active bookings before deleting a customer account
- If the Customer Service is unavailable during booking creation, the Booking Service returns a clear service unavailable response

---

## API Communication

The frontend sends booking and room requests to the Booking Service on port `8080`.

Before creating a booking, the Booking Service sends a REST request to the Customer Service on port `8081`:

```text
GET /api/customers/{customerId}
```

The response determines whether the supplied customer ID belongs to an existing customer.

The Booking Service also provides the following endpoint for the Customer Service:

```text
GET /api/bookings/customer/{customerId}/has-active
```

The Customer Service uses this response to prevent deletion of a customer account with active bookings.

---

## Testing

The Booking Service includes integration tests that start the Spring Boot application on a random port and perform real HTTP requests against the booking API.

The shared Docker Compose environment also provides isolated test databases and dedicated test runners for both services.

From the shared environment directory, run:

```console
docker compose --profile test up --build booking-tests customer-tests
```

After the tests finish, stop and remove the test containers and network:

```console
docker compose --profile test down
```

> The test databases are separate from the normal application databases.

---

## Production Build

The service uses a multi-stage Dockerfile that separates dependency installation, integration testing, application packaging and runtime execution.

The production image contains only the executable JAR and the Java Runtime Environment. Build tools, source files and test dependencies are not included in the final image.

The container runs as a non-root user and exposes the service on port `8080`.

The `.dockerignore` file excludes local, development and generated files from the Docker build context.

---

## Running the Complete System with Docker Compose

The shared Docker Compose environment and setup instructions are maintained in the Customer Service repository:

[pensionat-customer-service: Running the Complete System with Docker Compose](https://github.com/igor-gomes-academic/pensionat-customer-service#running-the-complete-system-with-docker-compose)

Follow those instructions to configure and start the Frontend, Booking Service, Customer Service and both MySQL databases.

---

## Team

- Patric Westman
- Daniel Lyytikäinen
- Niklas Dahlström
- Igor Gomes
