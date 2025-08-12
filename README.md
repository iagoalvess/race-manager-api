# Race Manager API

API for managing races and sporting events with complete functionalities for organizers and participants.

## 🚀 Features

### For Organizers (JWT Authentication)
- **Race Management**: Create, edit, delete and list races
- **Category Management**: Define categories by age and gender
- **Participant Management**: Register and manage participants
- **Race Day Operations**:
  - Start race with start time registration
  - Register finish time for each runner
  - View real-time classification table

### For Participants (Public Access)
- View race results
- Check classifications by category
- Track positions in real-time

## 🛠️ Technologies Used

- **Java 17**
- **Spring Boot 3.5.4**
- **Spring Security + JWT**
- **Spring Data JPA**
- **PostgreSQL**
- **MapStruct**
- **Lombok**
- **OpenAPI/Swagger**
- **Caffeine Cache**
- **TestContainers**

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+
- Docker (optional, for TestContainers)

## 🔧 Setup

### 1. Database
```sql
CREATE DATABASE race_manager_db;
```

### 2. Application Configuration
Edit the `application.properties` file:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/race_manager_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Run the Application
```bash
mvn spring-boot:run
```

## 📚 API Documentation

Access the Swagger documentation at: http://localhost:8080/api/v1/swagger-ui.html

## 🔐 Authentication

### Organizer Registration
```http
POST /api/v1/auth/register
Content-Type: application/json

{
  "name": "Organizer Name",
  "email": "organizer@email.com",
  "password": "password123"
}
```

### Login
```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "email": "organizer@email.com",
  "password": "password123"
}
```

### Using the Token
```http
Authorization: Bearer <your_jwt_token>
```

## 🏃‍♂️ Main Endpoints

### Races
- `GET /api/v1/race-events` - List races (public)
- `POST /api/v1/race-events` - Create race (organizer)
- `GET /api/v1/race-events/{id}` - Get race (public)
- `PUT /api/v1/race-events/{id}` - Update race (organizer)
- `DELETE /api/v1/race-events/{id}` - Delete race (organizer)

### Race Day Operations
- `POST /api/v1/race-events/{id}/start` - Start race (organizer)
- `POST /api/v1/race-events/{id}/finish` - Register finish time (organizer)
- `GET /api/v1/race-events/{id}/results` - View results (public)
- `GET /api/v1/race-events/{id}/results/category/{categoryId}` - Results by category (public)

### Participants
- `GET /api/v1/race-events/{id}/participants` - List participants (public)
- `POST /api/v1/race-events/{id}/participants` - Register participant (organizer)
- `GET /api/v1/race-events/{id}/participants/{participantId}` - Get participant (public)
- `DELETE /api/v1/race-events/{id}/participants/{participantId}` - Delete participant (organizer)

## 📊 Pagination and Sorting

All listing endpoints support pagination and sorting:

```http
GET /api/v1/race-events?page=0&size=20&sort=eventDate,desc
GET /api/v1/race-events/{id}/participants?page=0&size=10&sort=bibNumber,asc
GET /api/v1/race-events/{id}/results?page=0&size=50&sort=position,asc
```

## 🏆 Classification System

The system automatically calculates:
- **General Position**: Sorting by total time
- **Category Position**: Sorting by total time within category
- **Total Time**: Difference between finish and start time
- **Status**: REGISTERED, STARTED, FINISHED, DNF, DNS

## 📦 Build

```bash
# Build the project
mvn clean package

# Run JAR
java -jar target/race-manager-api-0.0.1-SNAPSHOT.jar
```

## 🔄 Cache

The application uses Caffeine cache to improve performance:
- Race results
- Participant lists
- Race data
- Configuration: maximum 500 entries, expiration in 10 minutes

## 🚨 Error Handling

The API returns standardized error codes:
- `400` - Validation error
- `401` - Not authenticated
- `403` - Not authorized
- `404` - Resource not found
- `409` - Conflict (e.g., CPF already registered)
- `500` - Internal server error

## 📈 Monitoring

- Structured logs with SLF4J
- Cache metrics
- Security logs
- Race operation logs

## 🤝 Contributing

1. Fork the project
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is under the MIT license. See the [LICENSE](LICENSE) file for more details.

## 📞 Support

For support, send an email to contact@racemanager.com or open an issue in the repository.
