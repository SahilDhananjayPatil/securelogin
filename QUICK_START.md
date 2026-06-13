# 📋 Quick Start Guide - 5 Minutes

## 1️⃣ Prerequisites (2 minutes)
- ✅ MySQL Server running (https://dev.mysql.com/downloads/mysql/)
- ✅ Java 17+ installed (https://www.oracle.com/java/technologies/downloads/)
- ✅ Maven available (`mvn -version`)

## 2️⃣ Database Setup (1 minute)

Open MySQL and run:
```sql
CREATE DATABASE login_registration_db;
```

## 3️⃣ Update Credentials (1 minute)

Edit: `src/main/resources/application.properties`

Change these lines to match YOUR MySQL credentials:
```properties
spring.datasource.username=root                    # YOUR username
spring.datasource.password=password                # YOUR password
```

## 4️⃣ Run the App (1 minute)

**Windows:**
```bash
mvnw.cmd spring-boot:run
```

**Linux/Mac:**
```bash
./mvnw spring-boot:run
```

## ✅ Done! Access the App

Open in browser:
```
http://localhost:8080
```

---

## 🎯 First Time User

1. Click **"Register here"** link
2. Fill in the form with your details
3. Click **"Register"**
4. Click **"Login here"**
5. Enter email and password
6. Click **"Login"**
7. You'll see welcome page with your name!

---

## 🆘 Quick Troubleshooting

| Problem | Solution |
|---------|----------|
| "Can't connect to MySQL" | Start MySQL service (Services → MySQL80 → Start) |
| "Database doesn't exist" | Run `CREATE DATABASE login_registration_db;` |
| "Access denied" | Check username/password in application.properties |
| "Port 8080 in use" | Change port in application.properties: `server.port=9090` |
| "Build fails" | Run: `mvnw.cmd clean install` |

---

## 📁 Project Files Overview

| File | Purpose |
|------|---------|
| `Application.java` | Application entry point |
| `AuthController.java` | Handles login/register/home routes |
| `UserService.java` | Business logic (encrypts passwords, validates) |
| `UserRepository.java` | Database queries |
| `User.java` | Database user model |
| `register.html` | Registration form page |
| `login.html` | Login form page |
| `home.html` | Welcome page after login |
| `application.properties` | Database configuration |

---

## 🔒 How Security Works

1. **Registration:**
   - Password is BCrypt encrypted (not stored in plain text)
   - Email checked for duplicates
   - User saved to database

2. **Login:**
   - Email looked up in database
   - Password compared with BCrypt hash
   - If match → Session created → Access granted

3. **Protected Pages:**
   - /home requires active session
   - Without login → Redirected to /login

---

## 💾 Database Structure

```
users table:
├── id (auto-increment primary key)
├── name (required)
├── email (required, unique)
├── password (BCrypt encrypted)
└── created_at (timestamp)
```

---

## 🧪 Test Cases

### ✅ Successful Registration
- Name: John Doe
- Email: john@test.com
- Password: Test1234
- Confirm: Test1234
→ Success message

### ❌ Duplicate Email
- Use same email as previous registration
→ Error: "Email already registered"

### ✅ Successful Login
- Email: john@test.com
- Password: Test1234
→ Home page with welcome message

### ❌ Wrong Password
- Email: john@test.com
- Password: WrongPass
→ Error: "Invalid email or password"

---

## 📞 Getting Help

1. **Check console logs** - Shows detailed error messages
2. **Read code comments** - Every file has detailed explanations
3. **Review README_COMPLETE.md** - Full documentation
4. **Check SETUP_AND_RUN.md** - Detailed setup instructions

---

**That's it! Your fully functional login/registration app is ready! 🚀**

---

## 🎓 Learning Path

After this works, try:
1. Add "Remember Me" functionality
2. Add email verification
3. Add password reset feature
4. Add user profile page
5. Add two-factor authentication
6. Add role-based access control

---

## 📊 Project Statistics

- **Total Files Created:** 13
  - 7 Java files (Controllers, Services, Repos, Entities, Config, Exceptions)
  - 3 HTML files (Thymeleaf templates)
  - 2 Configuration files
  - 1 Main application file

- **Lines of Code:** ~2000+
  - With detailed comments for beginners

- **Key Features:** 6
  - Registration, Login, Logout, Home, Error Handling, Session Management

- **Security Measures:** 3
  - BCrypt password hashing, Email uniqueness, Session validation

---

**Happy Coding! 🎉**

