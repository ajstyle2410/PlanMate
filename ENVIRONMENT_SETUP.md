# Environment Setup Instructions

## Required Environment Variables for Render

After fixing the critical issues, you need to configure these environment variables in Render Dashboard:

### Database Configuration
These are automatically populated when you link the Render PostgreSQL database:
- `DB_URL` - Auto-filled from database connection string
- `DB_USERNAME` - Auto-filled from database user  
- `DB_PASSWORD` - Auto-filled from database password

### Application Configuration
Set these manually in Render Dashboard → Environment:

```bash
# Hibernate Settings (Production)
HIBERNATE_DDL_AUTO=validate
HIBERNATE_SHOW_SQL=false
HIBERNATE_FORMAT_SQL=false

# Application Profile
SPRING_PROFILES_ACTIVE=prod

# Logging
LOG_LEVEL=INFO

# CORS (Update with your actual frontend domains)
ALLOWED_ORIGINS=https://planmates.dpdns.org,https://your-frontend-domain.com
```

### Optional (Render sets defaults)
- `PORT=8080` - Usually auto-configured by Render

---

## Local Development Setup

1. **Copy the example file:**
   ```bash
   cd PlanMate/src/main/resources
   cp application-dev.properties.example application-dev.properties
   ```

2. **Edit `application-dev.properties` with your local database:**
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/planmate_dev
   spring.datasource.username=your_local_username
   spring.datasource.password=your_local_password
   ```

3. **Run with development profile:**
   ```bash
   mvn spring-boot:run -Dspring.profiles.active=dev
   ```

---

## Security Notes

⚠️ **IMPORTANT:** Never commit actual credentials to git!

- The old database password `zEznbKzx37qpsv2DLwFrZs9urig2IXqT` was exposed in git history
- **Rotate the database password immediately** in Render PostgreSQL dashboard
- Update the `DB_PASSWORD` environment variable with the new password
- Consider rotating credentials periodically

---

## Health Check Configuration

In Render Dashboard:
- **Health Check Path:** `/health`
- **Health Check Interval:** 30 seconds (default)

The health endpoint will return:
- `200 OK` if application and database are healthy
- `503 Service Unavailable` if database connection fails
