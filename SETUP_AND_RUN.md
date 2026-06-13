# 🚀 Complete Setup and Running Guide

## Step 1: Configure MySQL Database

### Option A: Using MySQL Command Line

```bash
# Open MySQL command prompt
mysql -u root -p

# Enter your MySQL password
# Then run:

CREATE DATABASE login_registration_db;
USE login_registration_db;

-- You can verify with:
SHOW TABLES;
-- (Should be empty for now - Hibernate will create the 'users' table)
```

### Option B: Using MySQL Workbench

1. Open MySQL Workbench
2. Create new connection or use existing
3. Click "Create a new SQL tab for executing queries"
4. Paste and run:
```sql
CREATE DATABASE login_registration_db;
```

---

## Step 2: Update Database Credentials

Edit: `src/main/resources/application.properties`

```properties
# Current settings (update these with YOUR MySQL credentials)
spring.datasource.url=jdbc:mysql://localhost:3306/login_registration_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root          # ← Change to YOUR MySQL username
spring.datasource.password=password      # ← Change to YOUR MySQL password
```

**If MySQL is running on different host/port:**
```properties
# For remote MySQL:
spring.datasource.url=jdbc:mysql://your-host:3306/login_registration_db

# For non-standard port:
spring.datasource.url=jdbc:mysql://localhost:3307/login_registration_db
```

---

## Step 3: Build the Project

### Using Maven (Recommended)

**Windows:**
```bash
# Navigate to project directory
cd C:\Users\91727\Desktop\copilot-project

# Build project
mvnw.cmd clean install

# Wait for build to complete
```

**Linux/Mac:**
```bash
cd ~/Desktop/copilot-project
./mvnw clean install
```

**What happens during build:**
- Downloads all dependencies from Maven Central Repository
- Compiles Java source files
- Runs tests
- Packages into JAR file
- Creates `target/` directory with compiled code

---

## Step 4: Run the Application

### Option A: Using Maven (Easiest)

**Windows:**
```bash
mvnw.cmd spring-boot:run
```

**Linux/Mac:**
```bash
./mvnw spring-boot:run
```

**Output to look for:**
```
Started CopilotProjectApplication in X.XXX seconds (JVM running for X.XXX)
Tomcat started on port 8080
```

### Option B: Using Java directly

```bash
# After successful build:
cd target
java -jar copilot-project-0.0.1-SNAPSHOT.jar
```

### Option C: Using IDE (IntelliJ IDEA / Eclipse)

**IntelliJ IDEA:**
1. Right-click on `CopilotProjectApplication.java`
2. Click "Run 'CopilotProjectApplication.main()'"
3. Or press `Shift + F10`

**Eclipse:**
1. Right-click on project root
2. Run As → Spring Boot App

---

## Step 5: Test the Application

### 1. Access the Application

Open your browser and go to:
```
http://localhost:8080
```

You'll be automatically redirected to:
```
http://localhost:8080/login
```

### 2. First Time Setup

**Create a test account:**

1. Click "Register here" link on login page
2. Or go directly to: `http://localhost:8080/register`
3. Fill in the form:
   - **Name:** Test User
   - **Email:** test@example.com
   - **Password:** Test@1234
   - **Confirm Password:** Test@1234
4. Click "Register" button
5. You should see: "Registration successful! Please login with your credentials."
6. Click "Login here" link

### 3. Test Login

1. Enter email: `test@example.com`
2. Enter password: `Test@1234`
3. Click "Login" button
4. You should see the Welcome page with your name: "Welcome, Test User!"

### 4. Test Logout

1. Click "Logout" button
2. You'll be redirected to login page
3. Try accessing `http://localhost:8080/home`
4. You'll be automatically redirected to login page (access denied without session)

---

## Step 6: Verify Database

After testing, you can verify your data was saved:

```bash
# Connect to MySQL
mysql -u root -p

# Use the database
USE login_registration_db;

# View all users
SELECT * FROM users;

# View specific user
SELECT id, name, email, created_at FROM users WHERE email = 'test@example.com';

# The password column will show the BCrypt hash, like:
# $2a$10$KSfa.nC1Ky5PZ3VYP3fZ8OhMzIkJqXrXBG...
```

---

## 📊 Project File Structure

```
copilot-project/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── copilot_project/
│   │   │               ├── CopilotProjectApplication.java    # ✅ MAIN CLASS
│   │   │               ├── controller/
│   │   │               │   └── AuthController.java           # ✅ ROUTES
│   │   │               ├── service/
│   │   │               │   └── UserService.java              # ✅ BUSINESS LOGIC
│   │   │               ├── repository/
│   │   │               │   └── UserRepository.java           # ✅ DATABASE ACCESS
│   │   │               ├── entity/
│   │   │               │   └── User.java                     # ✅ DATA MODEL
│   │   │               ├── exception/
│   │   │               │   ├── UserAlreadyExistsException.java
│   │   │               │   ├── InvalidCredentialsException.java
│   │   │               │   └── GlobalExceptionHandler.java   # ✅ ERROR HANDLING
│   │   │               └── config/
│   │   │                   └── SecurityConfig.java           # ✅ SECURITY CONFIG
│   │   └── resources/
│   │       ├── application.properties                        # ✅ DATABASE CONFIG
│   │       ├── static/                                       # ✅ CSS, JS, IMAGES (if needed)
│   │       └── templates/
│   │           ├── register.html                            # ✅ REGISTRATION PAGE
│   │           ├── login.html                               # ✅ LOGIN PAGE
│   │           └── home.html                                # ✅ WELCOME PAGE
│   └── test/
│       └── java/...                                          # ✅ TEST FILES
├── target/                                                   # ✅ BUILD OUTPUT (auto-generated)
├── pom.xml                                                   # ✅ MAVEN CONFIGURATION
├── mvnw / mvnw.cmd                                           # ✅ MAVEN WRAPPER
└── README_COMPLETE.md                                        # ✅ DOCUMENTATION
```

---

## ✔️ Checklist Before Running

- [ ] **MySQL Server is running** - Check MySQL service status
- [ ] **Database created** - `login_registration_db` exists in MySQL
- [ ] **application.properties updated** - Correct username and password
- [ ] **Java 17+ installed** - Run `java -version` to verify
- [ ] **Maven available** - Run `mvn -version` to verify
- [ ] **No port 8080 conflicts** - Tomcat will try to use port 8080
- [ ] **All files created** - Check all Java files exist in IDE

---

## 🆘 Common Issues & Solutions

### Issue 1: "MySQL rejected connection"

**Error:** `java.sql.SQLException: Access denied for user 'root'@'localhost'`

**Solution:**
1. Verify MySQL is running
2. Check username and password in `application.properties`
3. Verify database exists: `SHOW DATABASES;`
4. Try connecting manually: `mysql -u root -p`

---

### Issue 2: "database does not exist"

**Error:** `Unknown database 'login_registration_db'`

**Solution:**
1. Create the database:
```sql
CREATE DATABASE login_registration_db;
```
2. Verify it was created:
```sql
SHOW DATABASES;
```

---

### Issue 3: "Port 8080 already in use"

**Error:** `Address already in use: bind`

**Solution - Option A: Kill existing process**
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Linux/Mac
lsof -i :8080
kill -9 <PID>
```

**Solution - Option B: Use different port**

Edit `application.properties`:
```properties
server.port=9090
```

Then access: `http://localhost:9090`

---

### Issue 4: "Build fails with dependency errors"

**Error:** `[ERROR] BUILD FAILURE`

**Solution:**
1. Clear Maven cache:
```bash
# Windows
rmdir /s /q %USERPROFILE%\.m2\repository

# Linux/Mac
rm -rf ~/.m2/repository
```

2. Rebuild:
```bash
mvnw.cmd clean install
```

---

### Issue 5: "Template not found"

**Error:** `org.springframework.web.servlet.NoHandlerFound`

**Solution:**
1. Check file names are correct (case-sensitive)
2. Ensure files are in `src/main/resources/templates/`
3. Verify file extensions are `.html`

---

### Issue 6: "Login page blank or styles not loading"

**Error:** No styling appears on pages

**Solution:**
1. Check browser console for errors (F12)
2. Clear browser cache (Ctrl+Shift+Delete)
3. Hard refresh (Ctrl+Shift+R)
4. Check console logs for Thymeleaf errors

---

## 🎯 Testing Scenarios

### Test 1: Basic Registration
```
Name: Alice Student
Email: alice@test.com
Password: Pass1234
Confirm: Pass1234
Expected: Register success → Login page
```

### Test 2: Duplicate Email
```
Email: alice@test.com (same as Test 1)
Expected: Error "Email already registered"
```

### Test 3: Wrong Confirm Password
```
Password: Pass1234
Confirm: Different1234
Expected: Error "Passwords do not match"
```

### Test 4: Short Password
```
Password: abc
Expected: Error "Password must be at least 6 characters"
```

### Test 5: Successful Login
```
Email: alice@test.com
Password: Pass1234
Expected: Home page → "Welcome, Alice Student!"
```

### Test 6: Wrong Password
```
Email: alice@test.com
Password: WrongPass
Expected: Error "Invalid email or password"
```

### Test 7: Non-existent Email
```
Email: nonexistent@test.com
Password: AnyPass123
Expected: Error "Invalid email or password"
```

### Test 8: Session Timeout
```
1. Login successfully
2. Access /home
3. Click Logout
4. Try accessing /home again
Expected: Redirected to /login
```

---

## 📝 Logs to Watch For

**Successful startup:**
```
2024-06-08 10:30:45 INFO Starting CopilotProjectApplication
2024-06-08 10:30:46 INFO Hibernate: create table users ...
2024-06-08 10:30:47 INFO Tomcat started on port(s): 8080 (http)
2024-06-08 10:30:47 INFO Started CopilotProjectApplication in 2.456 seconds
```

**Successful registration:**
```
2024-06-08 10:35:12 INFO Attempting to register user with email: alice@test.com
2024-06-08 10:35:12 INFO User registered successfully: alice@test.com
```

**Successful login:**
```
2024-06-08 10:36:00 INFO Processing login for email: alice@test.com
2024-06-08 10:36:00 INFO User authenticated successfully: alice@test.com
```

---

## 🎓 Code Structure Quick Reference

### Adding a new user endpoint

1. Add method in `AuthController`:
```java
@GetMapping("/newroute")
public String newRoute() {
    return "template-name"; // Returns template-name.html
}
```

2. Create template: `src/main/resources/templates/template-name.html`

### Adding validation

In `User.java`:
```java
@NotBlank(message = "Field cannot be empty")
@Email(message = "Must be valid email")
private String fieldName;
```

### Adding business logic

In `UserService.java`:
```java
public void newLogic(User user) {
    // Your code here
    userRepository.save(user); // Save to database
}
```

---

## 📞 Need Help?

1. **Check logs** - The console shows detailed error messages
2. **Read comments** - All Java code has detailed comments
3. **Review README_COMPLETE.md** - Full documentation
4. **Google the error** - Search error messages online
5. **Check Spring Boot docs** - https://spring.io

---

**You're all set! Start the application and enjoy! 🎉**

