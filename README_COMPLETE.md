# Login and Registration Web Application

A complete Java Spring Boot web application for user registration and authentication with password encryption, email validation, and session management.

## 🎯 Features

- ✅ **User Registration** - Create new user accounts with validation
- ✅ **User Authentication** - Secure login with BCrypt password encryption
- ✅ **Email Uniqueness** - Ensures no duplicate email registrations
- ✅ **Password Encoding** - BCrypt hashing for secure password storage
- ✅ **Session Management** - Secure user sessions after login
- ✅ **Thymeleaf Templates** - Dynamic HTML pages with server-side rendering
- ✅ **MySQL Database** - Persistent data storage
- ✅ **Exception Handling** - Global exception handling with user-friendly messages
- ✅ **Layered Architecture** - Clean separation of concerns (Controller → Service → Repository)
- ✅ **Input Validation** - Server-side validation for all user inputs

---

## 📁 Project Structure

```
src/main/
├── java/com/example/copilot_project/
│   ├── CopilotProjectApplication.java      # Main Spring Boot application entry point
│   ├── controller/
│   │   └── AuthController.java             # Handles HTTP requests for auth flows
│   ├── service/
│   │   └── UserService.java                # Business logic for registration & login
│   ├── repository/
│   │   └── UserRepository.java             # Data access layer (Spring Data JPA)
│   ├── entity/
│   │   └── User.java                       # Database entity representing users
│   ├── exception/
│   │   ├── UserAlreadyExistsException.java # Custom exception for duplicate email
│   │   ├── InvalidCredentialsException.java # Custom exception for auth failure
│   │   └── GlobalExceptionHandler.java     # Centralized exception handling
│   └── config/
│       └── SecurityConfig.java             # Spring configuration for BCrypt encoding
│
└── resources/
    ├── application.properties               # Database and app configuration
    └── templates/
        ├── register.html                    # Registration form page
        ├── login.html                       # Login form page
        └── home.html                        # Welcome page after successful login
```

---

## 🏗️ Layered Architecture

```
┌─────────────────────────────────────────────────────┐
│                 USER (Browser)                       │
└─────────────────────────────────────────────────────┘
                      ↓ HTTP Request/Response
┌─────────────────────────────────────────────────────┐
│         CONTROLLER LAYER (AuthController)            │
│  - Handles HTTP requests (@GetMapping, @PostMapping) │
│  - Receives form data                                │
│  - Calls service layer                               │
│  - Returns Thymeleaf views                           │
└─────────────────────────────────────────────────────┘
                      ↓
┌─────────────────────────────────────────────────────┐
│           SERVICE LAYER (UserService)                │
│  - Contains business logic                           │
│  - Password encoding with BCrypt                     │
│  - User validation                                   │
│  - Calls repository layer                            │
└─────────────────────────────────────────────────────┘
                      ↓
┌─────────────────────────────────────────────────────┐
│        REPOSITORY LAYER (UserRepository)             │
│  - Spring Data JPA interface                         │
│  - Database queries                                  │
│  - CRUD operations                                   │
└─────────────────────────────────────────────────────┘
                      ↓
┌─────────────────────────────────────────────────────┐
│              DATABASE (MySQL)                        │
│  - Stores user data                                  │
│  - Persists user information                         │
└─────────────────────────────────────────────────────┘
```

---

## 🔐 Security Implementation

### BCrypt Password Encoding

BCrypt is a robust password hashing algorithm that:

1. **Generates a Salt**: A random value unique to each password
2. **Applies the Salt**: The salt is combined with the password before hashing
3. **Hashes Multiple Times**: Uses 2^10 (1024) rounds to make brute-force attacks slow
4. **Stores the Hash**: The salted hash is stored in the database, never the plain password

**Example:**
```
Plain Password: "MyPassword123"
BCrypt Hash: $2a$10$KSfa.nC1Ky5PZ3VYP3fZ8OhMzIkJqXrXBGI9K0OlXN4Xe2jI1O7fC

When user logs in:
1. Take their input: "MyPassword123"
2. Compare with stored hash using: passwordEncoder.matches(input, hash)
3. If match → Login successful
```

### Session Management

After successful login:
```
1. Create HttpSession
2. Store userId, userName, userEmail in session
3. Session cookies sent to browser
4. Browser sends cookie with each request
5. Server validates session before allowing access to protected pages
6. Session invalidated on logout
```

---

## 📋 File Descriptions

### Entity Layer
**User.java**
- JPA entity representing the `users` database table
- Fields: id, name, email, password (BCrypt encoded), createdAt
- Annotations: @Entity, @Table, @Id, @GeneratedValue, @Column, @Email, @NotBlank
- Constraints: Email is unique, all fields are required

### Repository Layer
**UserRepository.java**
- Spring Data JPA interface extending JpaRepository<User, Long>
- Custom methods:
  - `findByEmail(String email)` - Find user by email
  - `existsByEmail(String email)` - Check if email exists
- Spring Data JPA auto-generates SQL queries based on method names

### Service Layer
**UserService.java**
- `registerUser(User user)` - Register new user with validation and BCrypt encoding
- `authenticateUser(String email, String password)` - Validate login credentials
- `findByEmail(String email)` - Find user by email
- `findById(Long id)` - Find user by ID
- Business logic separation from controller

### Controller Layer
**AuthController.java**
- `GET /register` - Display registration form
- `POST /register` - Handle registration submission
- `GET /login` - Display login form
- `POST /login` - Handle login submission with session creation
- `GET /home` - Display welcome page (requires active session)
- `GET /logout` - Invalidate session and logout
- `GET /` - Redirect to home or login based on session

### Configuration
**SecurityConfig.java**
- Creates PasswordEncoder bean using BCryptPasswordEncoder
- Strength parameter set to 10 for security/performance balance

### Exception Handling
**GlobalExceptionHandler.java**
- Catches UserAlreadyExistsException
- Catches InvalidCredentialsException
- Catches generic exceptions
- Returns appropriate error messages to user

### Templates
**register.html** - User registration form with validation messages
**login.html** - User login form
**home.html** - Welcome page after successful login

---

## 🚀 Getting Started

### Prerequisites

1. **Java 17+** - Download from https://www.oracle.com/java/technologies/downloads/
2. **MySQL Server** - Download from https://dev.mysql.com/downloads/mysql/
3. **Maven** - Download from https://maven.apache.org/download.cgi (or use mvnw included in project)
4. **IDE** - IntelliJ IDEA, Eclipse, or VS Code with Java extension

### Setup Steps

#### Step 1: Create MySQL Database

Open MySQL command line or MySQL Workbench and run:

```sql
-- Create database
CREATE DATABASE login_registration_db;

-- Use the database
USE login_registration_db;

-- Hibernate will auto-create the users table from the User entity
-- But you can optionally create it manually:
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at BIGINT
);
```

#### Step 2: Update application.properties

Edit `src/main/resources/application.properties`:

```properties
# Update these with your MySQL credentials
spring.datasource.url=jdbc:mysql://localhost:3306/login_registration_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root                    # Change to your MySQL username
spring.datasource.password=password                # Change to your MySQL password
```

#### Step 3: Build the Project

```bash
# Navigate to project directory
cd C:\Users\91727\Desktop\copilot-project

# Build with Maven (Windows)
mvnw.cmd clean install

# Or on Linux/Mac
./mvnw clean install
```

#### Step 4: Run the Application

```bash
# Option 1: Using Maven (Windows)
mvnw.cmd spring-boot:run

# Option 2: Using Maven (Linux/Mac)
./mvnw spring-boot:run

# Option 3: Using Java (after build)
java -jar target/copilot-project-0.0.1-SNAPSHOT.jar
```

#### Step 5: Access the Application

Open your browser and go to:
```
http://localhost:8080
```

You'll be redirected to: `http://localhost:8080/login`

---

## 📝 User Flow

### Registration Flow
```
1. User visits http://localhost:8080/register
2. Fills in: Name, Email, Password, Confirm Password
3. Clicks "Register" button
4. Server validates:
   - Passwords match
   - Password length ≥ 6 characters
   - Email doesn't already exist
5. If valid:
   - Password is BCrypt encoded
   - User object saves to database
   - Redirects to login with success message
6. If invalid:
   - Error message displayed
   - Stays on registration page
```

### Login Flow
```
1. User visits http://localhost:8080/login
2. Enters Email and Password
3. Clicks "Login" button
4. Server validates:
   - User exists with given email
   - Password matches stored BCrypt hash
5. If valid:
   - Creates HttpSession with userId, userName, userEmail
   - Redirects to /home
   - Home page displays personalized welcome message
6. If invalid:
   - Error message displayed: "Invalid email or password"
   - Stays on login page
```

### Home Page Flow
```
1. User can only access /home if logged in (session exists)
2. If not logged in:
   - Redirected to /login
3. If logged in:
   - Welcome message with user's name is displayed
   - Logout button available
   - User can click logout to invalidate session
```

### Logout Flow
```
1. User clicks "Logout" button
2. Server invalidates HttpSession
3. All session attributes cleared
4. Redirects to /login
5. User must login again to access /home
```

---

## 🧪 Testing the Application

### Test User 1 - Registration Test
1. Go to `http://localhost:8080/register`
2. Fill in:
   - Name: John Doe
   - Email: john@example.com
   - Password: Password123
   - Confirm Password: Password123
3. Click Register
4. Should see success message and redirect to login

### Test User 2 - Duplicate Email Test
1. Go to `http://localhost:8080/register`
2. Fill in:
   - Name: Jane Doe
   - Email: john@example.com (same as before)
   - Password: Password456
   - Confirm Password: Password456
3. Click Register
4. Should see error: "Email already registered. Please use a different email."

### Test User 3 - Login Test
1. Go to `http://localhost:8080/login`
2. Fill in:
   - Email: john@example.com
   - Password: Password123
3. Click Login
4. Should see home page with: "Welcome, John Doe!"

### Test User 4 - Wrong Password Test
1. Go to `http://localhost:8080/login`
2. Fill in:
   - Email: john@example.com
   - Password: WrongPassword
3. Click Login
4. Should see error: "Invalid email or password."

### Test User 5 - Password Mismatch Test
1. Go to `http://localhost:8080/register`
2. Fill in:
   - Name: Bob Smith
   - Email: bob@example.com
   - Password: Password789
   - Confirm Password: DifferentPassword
3. Click Register
4. Should see error: "Passwords do not match!"

---

## 💾 Database Structure

### users Table

```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,           -- Auto-incrementing primary key
    name VARCHAR(100) NOT NULL,                     -- User's full name (required)
    email VARCHAR(100) NOT NULL UNIQUE,             -- Email (required, must be unique)
    password VARCHAR(255) NOT NULL,                 -- BCrypt encoded password
    created_at BIGINT                               -- Timestamp of account creation
);
```

### Example Data (after BCrypt encryption)

```
id  | name       | email            | password (BCrypt)                      | created_at
----|------------|------------------|----------------------------------------|------------
1   | John Doe   | john@example.com | $2a$10$KSfa.nC1Ky5PZ3VYP3fZ8...      | 1717939200000
2   | Jane Smith | jane@example.com | $2a$10$A7xB2nQ9Lm5PZ3VYP3fZ8...      | 1717939300000
```

---

## 🛠️ Technologies Used

| Component | Technology | Version |
|-----------|-----------|---------|
| Framework | Spring Boot | 4.0.6 |
| Web MVC | Spring MVC | 4.0.6 |
| Data | Spring Data JPA | 4.0.6 |
| Template Engine | Thymeleaf | Standard (with Spring Boot) |
| Security | Spring Security + BCrypt | Included |
| Database | MySQL | 8.0.33 |
| ORM | Hibernate (via JPA) | Included |
| Build Tool | Maven | 3.x |
| Java Version | Java | 17+ |
| Utility | Lombok | Latest |

---

## 📚 Key Concepts Explained

### Spring Boot @SpringBootApplication
Combines @Configuration, @EnableAutoConfiguration, and @ComponentScan to:
- Mark class as configuration source
- Enable auto-configuration of Spring Boot
- Scan for and register Spring components

### Constructor Injection (@RequiredArgsConstructor)
```java
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository; // Injected via constructor
}
```
Benefits:
- Dependency is immutable (final)
- Easy to test (dependencies can be mocked)
- No setter injection needed

### Custom Exceptions
```java
throw new UserAlreadyExistsException("Email already registered");
```
Benefits:
- Specific error handling
- Clear intent
- Centralized exception handling

### HttpSession for Auth
```java
session.setAttribute("userId", user.getId());
session.invalidate(); // Logout
```
Benefits:
- Stateful authentication
- Server-side storage
- Secure cookies

### BCrypt Password Encoding
```java
String encoded = passwordEncoder.encode("plainPassword");
boolean matches = passwordEncoder.matches(input, encoded);
```
Benefits:
- Automatic salting
- Multiple hashing rounds
- No plain passwords stored

### Spring Data JPA Query Methods
```java
Optional<User> findByEmail(String email);
```
Spring automatically generates SQL:
```sql
SELECT * FROM users WHERE email = ?
```

---

## 🔍 Troubleshooting

### Issue: "Can't connect to MySQL server"
**Solution:**
- Ensure MySQL service is running
- Check connection URL in application.properties
- Verify username and password are correct
- Ensure database `login_registration_db` exists

### Issue: "Table 'users' doesn't exist"
**Solution:**
- Delete database and let Hibernate create it with `spring.jpa.hibernate.ddl-auto=create`
- Or manually create table using SQL script

### Issue: "Password encoder not found"
**Solution:**
- Ensure SecurityConfig.java is in the same package
- Check if @Bean method for PasswordEncoder exists

### Issue: "Thymeleaf template not found"
**Solution:**
- Ensure template files are in `src/main/resources/templates/`
- Check file names match exactly (case-sensitive)
- Check return statements in controller

### Issue: "Session lost after redirect"
**Solution:**
- Session should persist automatically
- Check if cookies are enabled in browser
- Verify HttpSession isn't being cleared unexpectedly

---

## 📖 Learning Resources

1. **Spring Boot Official Guide**: https://spring.io/guides/gs/serving-web-content/
2. **Spring Data JPA**: https://spring.io/projects/spring-data-jpa
3. **Thymeleaf**: https://www.thymeleaf.org/
4. **BCrypt Algorithm**: https://www.mindrot.org/projects/bcrypt/
5. **REST Best Practices**: https://restfulapi.net/

---

## 📄 License

This project is open source and available under the MIT License.

---

## 🤝 Contributing

Contributions are welcome! Feel free to:
1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

---

## 📞 Support

For questions or issues, please:
1. Check the Troubleshooting section
2. Review the code comments
3. Check Spring Boot documentation
4. Open an issue on the repository

---

**Happy Coding! 🚀**

