# PlanMate - Local Development Setup Guide

## Prerequisites
- Java 17 JDK
- MySQL 8.0+
- Maven 3.6+
- IDE (IntelliJ IDEA / Eclipse / VS Code)

---

## Step 1: Database Setup

### Install MySQL
1. Download and install MySQL 8.0+ from [mysql.com](https://dev.mysql.com/downloads/mysql/)
2. Start MySQL service

### Create Database
```sql
-- Login to MySQL
mysql -u root -p

-- Create database
CREATE DATABASE IF NOT EXISTS planmate_dev 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- Verify
SHOW DATABASES LIKE 'planmate_dev';

-- Exit
EXIT;
```

---

## Step 2: Configure Application

### Update pom.xml
Add MySQL connector dependency (if not present):

```xml
<!-- MySQL Driver for local development -->
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <version>8.2.0</version>
</dependency>
```

### Create Local Properties File
Copy `application-local.properties` and update:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/planmate_dev?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD

# Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

## Step 3: Build Project

```bash
# Navigate to project directory
cd PlanMate/PlanMate

# Clean and install dependencies
mvn clean install

# Skip tests if needed
mvn clean install -DskipTests
```

---

## Step 4: Run Application

### Option A: Using Maven
```bash
# Run with local profile
mvn spring-boot:run -Dspring.profiles.active=local

# Or using Tomcat
mvn tomcat7:run
```

### Option B: Using IDE
1. Import project as Maven project
2. Set active profile to `local`
3. Run main application class or deploy to Tomcat

---

## Step 5: Verify Setup

### Check Database Tables
```sql
USE planmate_dev;
SHOW TABLES;
```

You should see tables for:
- `trips`
- `destinations`
- `day_plans`
- `activities`
- `expenses`
- `accommodations`
- `transportation`
- `packing_items`
- `trip_documents`
- `trip_shares`
- `trip_templates`
- `user_profile`

### Access Application
- **URL:** http://localhost:8080/PlanMate
- **Login:** Use existing user or register new account

---

## Troubleshooting

### Database Connection Error
```
Error: Access denied for user 'root'@'localhost'
```
**Solution:** Check MySQL password in `application-local.properties`

### Port Already in Use
```
Error: Port 8080 already in use
```
**Solution:** Change port in properties:
```properties
server.port=8081
```

### Hibernate DDL Error
```
Error: Table already exists
```
**Solution:** Change ddl-auto:
```properties
spring.jpa.hibernate.ddl-auto=validate
```

---

## Development Workflow

1. **Start MySQL** service
2. **Run application** with local profile
3. **Access** http://localhost:8080/PlanMate
4. **Test features** using the UI
5. **Check logs** for errors
6. **Verify database** changes

---

## Next Steps

- Create sample data
- Test all CRUD operations
- Verify UI pages load correctly
- Test new features (itinerary, budget, etc.)
- Deploy to production (Render)

---

## Useful Commands

```bash
# Check MySQL status
mysql --version

# Login to MySQL
mysql -u root -p

# Maven clean build
mvn clean package

# Run tests
mvn test

# Check dependencies
mvn dependency:tree
```

---

## Configuration Files

- **Database:** `application-local.properties`
- **Production:** `application.properties`
- **Render:** `render.yaml`
- **Init SQL:** `database/init-mysql.sql`

---

**Status:** Ready for local development ✅
