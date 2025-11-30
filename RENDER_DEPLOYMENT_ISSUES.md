# PlanMate Render Deployment - Issues Report

**Generated:** November 30, 2025  
**Domain:** planmates.dpdns.org  
**Deployment Platform:** Render

---

## 🔴 CRITICAL ISSUES

### 1. Hardcoded Database Credentials in Repository
**Severity:** CRITICAL - Security Vulnerability  
**Location:** `src/main/resources/application.properties` (Lines 13-15)

**Issue:**
```properties
spring.datasource.url=jdbc:postgresql://dpg-d3hvh7juibrs73b8lof0-a.oregon-postgres.render.com:5432/planmate?sslmode=require
spring.datasource.username=root
spring.datasource.password=zEznbKzx37qpsv2DLwFrZs9urig2IXqT
```

**Impact:**
- Database credentials are exposed in version control
- Anyone with repository access can access the production database
- Violates security best practices

**Recommended Fix:**
- Remove hardcoded credentials from `application.properties`
- Use environment variables only (already configured but defaults expose secrets)
- Add `application.properties` to `.gitignore` or use a template file
- Rotate database password immediately
- Configure credentials in Render dashboard as environment variables

---

### 2. Missing Render Configuration File
**Severity:** HIGH  
**Location:** Root directory

**Issue:**
- No `render.yaml` file found in project
- Deployment configuration is not version-controlled
- Manual configuration increases deployment errors

**Recommended Fix:**
Create `render.yaml` in project root:
```yaml
services:
  - type: web
    name: planmate
    env: docker
    region: oregon
    plan: free
    dockerfilePath: ./Dockerfile
    dockerContext: .
    envVars:
      - key: DB_DRIVER
        value: org.postgresql.Driver
      - key: DB_URL
        fromDatabase:
          name: planmate
          property: connectionString
      - key: DB_USERNAME
        fromDatabase:
          name: planmate
          property: user
      - key: DB_PASSWORD
        fromDatabase:
          name: planmate
          property: password
      - key: HIBERNATE_DIALECT
        value: org.hibernate.dialect.PostgreSQL95Dialect
    
databases:
  - name: planmate
    databaseName: planmate
    user: planmate_user
    region: oregon
    plan: free
```

---

### 3. Insecure Password Encoding Without Spring Security Configuration
**Severity:** HIGH  
**Location:** `src/main/java/com/org/planmet/Iservice/UserProfileServiceImpl.java`

**Issue:**
- Using `BCryptPasswordEncoder` directly without Spring Security properly configured
- Password encoder instantiated as instance variable: `new BCryptPasswordEncoder()` 
- No security context or authentication mechanism configured
- Spring Security dependency exists but no `SecurityConfig` bean
- Session-based authentication without proper security filters

**Impact:**
- Passwords are hashed but authentication is manually implemented
- No CSRF protection
- No security headers
- Session hijacking vulnerability
- No role-based access control enforcement at framework level

**Recommended Fix:**
- Create Spring Security configuration class
- Define `PasswordEncoder` as a Spring bean
- Implement `UserDetailsService`
- Configure HTTP security with CSRF protection
- Add security filter chain for authentication/authorization

---

### 4. Dockerfile Uses Java 17 but POM Uses Java 11
**Severity:** HIGH - Configuration Mismatch  
**Location:** `Dockerfile` (Line 4) vs `pom.xml` (Lines 15-16)

**Issue:**
```dockerfile
# Dockerfile specifies JDK 17
FROM maven:3.9.6-eclipse-temurin-17 AS builder
```

```xml
<!-- pom.xml specifies Java 11 -->
<maven.compiler.source>11</maven.compiler.source>
<maven.compiler.target>11</maven.compiler.target>
```

**Impact:**
- Potential runtime incompatibilities
- Different behavior in development vs production
- May cause deployment failures

**Recommended Fix:**
Choose ONE version and update both files:
- **Option A:** Downgrade Dockerfile to Java 11
- **Option B:** Upgrade pom.xml to Java 17 (recommended for newer features)

---

## 🟡 HIGH PRIORITY ISSUES

### 5. Connection Pool Not Configured for Production
**Severity:** MEDIUM-HIGH  
**Location:** `src/main/webapp/WEB-INF/spring-servlet.xml` (Line 42)

**Issue:**
```xml
<bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
```

**Impact:**
- `DriverManagerDataSource` creates new connection for each request
- No connection pooling
- Poor performance under load
- Database connection exhaustion
- Not suitable for production environments

**Recommended Fix:**
Replace with HikariCP or Apache Commons DBCP2:

**Option A - HikariCP (Recommended):**
```xml
<bean id="dataSource" class="com.zaxxer.hikari.HikariDataSource" destroy-method="close">
    <property name="driverClassName" value="${spring.datasource.driver-class-name}"/>
    <property name="jdbcUrl" value="${spring.datasource.url}"/>
    <property name="username" value="${spring.datasource.username}"/>
    <property name="password" value="${spring.datasource.password}"/>
    <property name="maximumPoolSize" value="10"/>
    <property name="minimumIdle" value="2"/>
    <property name="connectionTimeout" value="30000"/>
    <property name="idleTimeout" value="600000"/>
</bean>
```

Add to `pom.xml`:
```xml
<dependency>
    <groupId>com.zaxxer</groupId>
    <artifactId>HikariCP</artifactId>
    <version>5.0.1</version>
</dependency>
```

---

### 6. Hibernate DDL Auto-Update in Production
**Severity:** MEDIUM-HIGH  
**Location:** `src/main/resources/application.properties` (Line 23)

**Issue:**
```properties
hibernate.hbm2ddl.auto=update
```

**Impact:**
- Schema changes can occur automatically in production
- Risk of data loss or corruption
- No migration tracking or rollback capability
- Difficult to reproduce schema across environments

**Recommended Fix:**
- Change to `validate` for production
- Use environment-specific properties or profiles
- Implement database migrations with Flyway or Liquibase

Example:
```properties
# Development
hibernate.hbm2ddl.auto=${HIBERNATE_DDL_AUTO:validate}

# Set in Render environment variables
HIBERNATE_DDL_AUTO=validate
```

---

### 7. SQL Logging Enabled in Production
**Severity:** MEDIUM  
**Location:** `src/main/resources/application.properties` (Lines 21-22)

**Issue:**
```properties
hibernate.show_sql=true
hibernate.format_sql=true
```

**Impact:**
- Performance overhead
- Logs can expose sensitive data
- Increased disk usage
- Potential security risk

**Recommended Fix:**
```properties
hibernate.show_sql=${HIBERNATE_SHOW_SQL:false}
hibernate.format_sql=${HIBERNATE_FORMAT_SQL:false}
```

Set to `true` only in development environment.

---

### 8. No Health Check Endpoint
**Severity:** MEDIUM  
**Location:** Missing

**Issue:**
- No `/health` or `/actuator/health` endpoint
- Render cannot properly monitor application health
- Difficult to diagnose deployment issues

**Recommended Fix:**
Add Spring Boot Actuator or create custom health check controller:

```java
@RestController
@RequestMapping("/health")
public class HealthController {
    
    @Autowired
    private SessionFactory sessionFactory;
    
    @GetMapping
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> status = new HashMap<>();
        
        try {
            // Check database connection
            sessionFactory.getCurrentSession();
            status.put("status", "UP");
            status.put("database", "connected");
        } catch (Exception e) {
            status.put("status", "DOWN");
            status.put("database", "disconnected");
            return ResponseEntity.status(503).body(status);
        }
        
        return ResponseEntity.ok(status);
    }
}
```

Configure in Render dashboard: Health Check Path = `/health`

---

## 🟢 MEDIUM PRIORITY ISSUES

### 9. Outdated PostgreSQL Dialect
**Severity:** MEDIUM  
**Location:** `src/main/resources/application.properties` (Line 16)

**Issue:**
```properties
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQL95Dialect
```

**Impact:**
- Using deprecated dialect for PostgreSQL 9.5
- Modern PostgreSQL features not available
- Performance optimizations missed

**Recommended Fix:**
```properties
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

This will auto-detect the PostgreSQL version.

---

### 10. Lazy Load Without Transaction Issue
**Severity:** MEDIUM  
**Location:** `src/main/resources/application.properties` (Line 25)

**Issue:**
```properties
hibernate.enable_lazy_load_no_trans=true
```

**Impact:**
- Anti-pattern enabled
- Masks transaction boundary issues
- Can cause N+1 query problems
- Performance degradation

**Recommended Fix:**
- Remove this property
- Properly manage transactions and fetch strategies
- Use `@Transactional` appropriately
- Consider using JOIN FETCH in queries

---

### 11. Missing CORS Configuration
**Severity:** MEDIUM  
**Location:** Missing

**Issue:**
Based on conversation history, CORS issues were encountered previously. No CORS configuration found in:
- `spring-servlet.xml`
- Java configuration classes

**Impact:**
- Frontend applications from different origins cannot access API
- AJAX requests may fail

**Recommended Fix:**
Add CORS configuration in `spring-servlet.xml`:

```xml
<mvc:cors>
    <mvc:mapping path="/**"
                 allowed-origins="${ALLOWED_ORIGINS:http://localhost:4200,https://yourdomain.com}"
                 allowed-methods="GET,POST,PUT,DELETE,OPTIONS"
                 allowed-headers="*"
                 allow-credentials="true"
                 max-age="3600"/>
</mvc:cors>
```

Or create Java configuration:
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(getAllowedOrigins())
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
    
    private String[] getAllowedOrigins() {
        String origins = System.getenv("ALLOWED_ORIGINS");
        return origins != null ? origins.split(",") : 
               new String[]{"http://localhost:4200"};
    }
}
```

---

### 12. Inconsistent Transaction Annotations
**Severity:** LOW-MEDIUM  
**Location:** `src/main/java/com/org/planmet/Iservice/UserProfileServiceImpl.java`

**Issue:**
Using `javax.transaction.Transactional` instead of Spring's

```java
import javax.transaction.Transactional;  // JTA transaction
```

**Impact:**
- Mixing JTA and Spring transactions
- Inconsistent behavior
- Could cause transaction management issues

**Recommended Fix:**
Use Spring's `@Transactional`:
```java
import org.springframework.transaction.annotation.Transactional;
```

Apply consistently across all service classes.

---

### 13. No Error Pages Configured
**Severity:** LOW-MEDIUM  
**Location:** `web.xml`

**Issue:**
- No custom error pages defined
- Default Tomcat error pages expose server information
- Poor user experience on errors

**Recommended Fix:**
Add to `web.xml`:
```xml
<error-page>
    <error-code>404</error-code>
    <location>/WEB-INF/views/error/404.html</location>
</error-page>
<error-page>
    <error-code>500</error-code>
    <location>/WEB-INF/views/error/500.html</location>
</error-page>
<error-page>
    <exception-type>java.lang.Exception</exception-type>
    <location>/WEB-INF/views/error/general.html</location>
</error-page>
```

---

### 14. Port Configuration May Conflict
**Severity:** LOW-MEDIUM  
**Location:** `Dockerfile` (Line 27)

**Issue:**
```dockerfile
EXPOSE 8080
```

**Notes:**
- Render requires binding to `$PORT` environment variable
- Tomcat defaults to 8080, which should work
- Verify Render is configured to use port 8080

**Recommended Fix:**
Add to Render environment variables (if not already set):
```
PORT=8080
```

Or modify Tomcat configuration to read from `$PORT` if Render expects dynamic port.

---

### 15. No Logging Configuration
**Severity:** LOW  
**Location:** Missing `logback.xml` or `log4j2.xml`

**Issue:**
- Using SLF4J with simple implementation
- No log rotation
- No log levels configured
- All logs to console only

**Impact:**
- Logs not persisted properly
- Difficult to debug production issues
- No structured logging

**Recommended Fix:**
Create `src/main/resources/logback.xml`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <property name="LOG_LEVEL" value="${LOG_LEVEL:-INFO}"/>
    
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <logger name="com.org.planmet" level="${LOG_LEVEL}"/>
    <logger name="org.hibernate.SQL" level="${HIBERNATE_LOG_LEVEL:-WARN}"/>
    <logger name="org.springframework" level="INFO"/>

    <root level="INFO">
        <appender-ref ref="CONSOLE"/>
    </root>
</configuration>
```

Update `pom.xml`:
```xml
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.4.14</version>
</dependency>
```

Remove:
```xml
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-simple</artifactId>
    <version>1.7.36</version>
</dependency>
```

---

## 🔵 LOW PRIORITY / RECOMMENDATIONS

### 16. Dependency Version Updates
**Severity:** LOW  
**Location:** `pom.xml`

**Issue:**
Some dependencies could be updated to newer versions:
- Spring 5.3.32 → Consider Spring 6.x or Spring Boot
- Hibernate 5.6.15 → Consider Hibernate 6.x
- MySQL Connector 8.0.33 → 8.0.35+
- Thymeleaf layout dialect could use newer version

**Recommended Fix:**
- Test compatibility before upgrading
- Consider migrating to Spring Boot for easier dependency management
- Create a separate branch for version upgrades

---

### 17. Docker Image Optimization
**Severity:** LOW  
**Location:** `Dockerfile`

**Recommendations:**
Current Dockerfile is good but could be improved:

```dockerfile
# Use smaller base image for runtime
FROM tomcat:9.0-jdk17-temurin-alpine

# Add non-root user
RUN addgroup -S tomcat && adduser -S tomcat -G tomcat
USER tomcat

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/health || exit 1
```

---

### 18. Missing .dockerignore
**Severity:** LOW  
**Location:** Root directory

**Issue:**
- No `.dockerignore` file
- Unnecessary files copied to Docker context
- Slower build times

**Recommended Fix:**
Create `.dockerignore`:
```
target/
.git/
.gitignore
.settings/
.classpath
.project
.vscode/
*.md
.metadata/
```

---

### 19. Session Management for Distributed Deployment
**Severity:** LOW (for current single-instance deployment)  
**Location:** Application architecture

**Issue:**
- Using in-memory HTTP sessions
- Not suitable for horizontal scaling
- Session data lost on container restart

**Future Recommendation:**
If scaling beyond single instance:
- Implement Redis session storage
- Use Spring Session
- Or use stateless JWT authentication

---

### 20. Missing Environment-Specific Profiles
**Severity:** LOW  
**Location:** Configuration

**Issue:**
- Single `application.properties` for all environments
- No Spring profiles configured

**Recommended Fix:**
Create profile-specific files:
- `application.properties` (common settings)
- `application-dev.properties`
- `application-prod.properties`

Use environment variable: `SPRING_PROFILES_ACTIVE=prod`

---

## 📋 DEPLOYMENT CHECKLIST

Before going to production, ensure:

- [ ] **Remove hardcoded database credentials**
- [ ] **Rotate database password**
- [ ] **Create `render.yaml` configuration**
- [ ] **Align Java versions (Dockerfile and pom.xml)**
- [ ] **Configure connection pooling (HikariCP)**
- [ ] **Set `hibernate.hbm2ddl.auto=validate`**
- [ ] **Disable SQL logging in production**
- [ ] **Add health check endpoint**
- [ ] **Configure CORS properly**
- [ ] **Update PostgreSQL dialect**
- [ ] **Add custom error pages**
- [ ] **Configure proper logging (Logback)**
- [ ] **Test all endpoints after deployment**
- [ ] **Set up monitoring and alerts in Render**
- [ ] **Configure SSL/HTTPS properly**
- [ ] **Review and test security configuration**

---

## 🔧 RENDER DASHBOARD CONFIGURATION

Ensure these environment variables are set in Render:

```bash
# Database (from Render database service)
DB_DRIVER=org.postgresql.Driver
DB_URL=<from database connection string>
DB_USERNAME=<from database credentials>
DB_PASSWORD=<from database credentials>

# Hibernate
HIBERNATE_DIALECT=org.hibernate.dialect.PostgreSQLDialect
HIBERNATE_DDL_AUTO=validate
HIBERNATE_SHOW_SQL=false
HIBERNATE_FORMAT_SQL=false

# Application
SPRING_PROFILES_ACTIVE=prod
LOG_LEVEL=INFO
PORT=8080

# CORS (if using frontend)
ALLOWED_ORIGINS=https://yourdomain.com,https://www.yourdomain.com
```

---

## 📊 SEVERITY SUMMARY

| Severity | Count | Issues |
|----------|-------|--------|
| 🔴 CRITICAL | 4 | #1, #2, #3, #4 |
| 🟡 HIGH | 4 | #5, #6, #7, #8 |
| 🟢 MEDIUM | 7 | #9, #10, #11, #12, #13, #14, #15 |
| 🔵 LOW | 5 | #16, #17, #18, #19, #20 |
| **TOTAL** | **20** | |

---

## 🎯 IMMEDIATE ACTION ITEMS

1. **Security First:** Remove hardcoded credentials and rotate passwords
2. **Add `render.yaml`:** Version control deployment configuration
3. **Fix Java Version Mismatch:** Align Dockerfile and pom.xml
4. **Add Connection Pooling:** Replace DriverManagerDataSource with HikariCP
5. **Production Settings:** Set proper Hibernate and logging configurations

---

**Document Version:** 1.0  
**Last Updated:** November 30, 2025
