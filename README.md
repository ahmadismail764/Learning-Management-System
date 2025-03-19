# Learning Management System (LMS)

## Project Overview
This is a Java-based Learning Management System (LMS) developed using Spring Boot. The system is designed to manage and organize online courses, assessments, and performance tracking from the perspective of students and instructors. It supports a range of user needs with specific features for different user types.

## Features

### User Management
- **User Types**: Admin, Instructor, Student
- **Role-based Access**: Different permissions based on user roles
- **Profile Management**: View and update profile information

### Course Management
- **Course Creation**: Instructors can create courses with details like title, description, duration, etc.
- **Media Upload**: Support for uploading various media files (videos, PDFs, audio, etc.)
- **Enrollment Management**: Students can view and enroll in available courses
- **Lesson Organization**: Courses are organized into lessons for structured learning

### Attendance Management
- **OTP Generation**: Instructors can generate OTPs per lesson to track attendance
- **Attendance Tracking**: Students can mark attendance by entering the OTP

### Assessment & Grading
- **Quiz Creation**: Support for various question types (MCQ, true/false, short answers)
- **Question Bank**: Instructors can create a repository of questions
- **Assignment Submission**: File upload functionality for assignments
- **Grading System**: Instructors can grade submissions and provide feedback

### Performance Tracking
- **Progress Monitoring**: Track quiz scores, assignments, and attendance

## Technical Stack

### Backend
- Java 17
- Spring Boot 3.4.0
- Spring Security
- Spring Data JPA
- JWT for Authentication

### Database
- MySQL

### Testing
- JUnit
- Mockito

## Project Structure
```
src/
├── main/
│   ├── java/org/software/lms/
│   │   ├── controller/        # REST API controllers
│   │   ├── dto/               # Data Transfer Objects
│   │   ├── exception/         # Custom exceptions
│   │   ├── model/             # Entity classes
│   │   ├── repository/        # Repository interfaces
│   │   ├── security/          # JWT & Spring Security configurations
│   │   ├── service/           # Service layer implementation
│   │   └── LmsApplication.java # Main application class
│   └── resources/
│       └── application.properties # Configuration file
├── test/
│   └── java/org/software/lms/
│       └── service/           # Service layer tests
├── pom.xml                    # Maven configuration
```

## Prerequisites
- Java 17 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher
- Git

## Installation & Setup

### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/Learning-Management-System.git
cd Learning-Management-System
```

### 2. Database Setup
Create a MySQL database named `lms`:
```sql
CREATE DATABASE lms;
```

### 3. Configure Database Connection
Update the `application.properties` file with your database credentials:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/lms?useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.username=your_username
spring.datasource.password=your_password
```

## Testing
The project includes unit tests using JUnit and Mockito:

## File Upload Configuration
The system supports file uploads with the following configurations:
```properties
file.upload.directory=/path/to/upload/directory
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
file.upload-dir=./uploads
spring.servlet.multipart.enabled=true
```

## Security
- JWT (JSON Web Token) based authentication
- Role-based authorization
- Secured REST endpoints

## API Endpoints
The application provides RESTful API endpoints for all functionalities. A Postman collection is available for testing and documentation purposes.
