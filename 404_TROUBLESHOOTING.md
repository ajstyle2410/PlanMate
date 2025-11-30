# 404 Error Troubleshooting Guide

## Current Status
- ✅ Tomcat 9.0.106 is running (correct version)
- ❌ Getting HTTP 404 error when accessing http://localhost:4353/PlanMate/

## Root Cause Analysis

The 404 error typically means one of these issues:

1. **Spring context failed to load** - Application didn't start properly
2. **WAR file not deployed correctly** - Old or incomplete deployment
3. **Database connection failure** - Spring can't initialize beans

---

## Solution Steps

### Step 1: Check Tomcat Logs

**Location:** Your Tomcat installation folder → `logs/catalina.out` or `logs/catalina.YYYY-MM-DD.log`

**Look for:**
- `INFO: Deploying web application directory [PlanMate]` or `INFO: Deployment of web application directory [PlanMate] has finished`
- Any `ERROR` or `SEVERE` messages
- `ClassNotFoundException` errors
- Database connection errors

**Common errors to look for:**
```
SEVERE: Exception starting filter
SEVERE: Context initialization failed
ERROR: Failed to load ApplicationContext
```

### Step 2: Rebuild and Redeploy

Since I've updated the configuration to use H2 in-memory database by default, you need to rebuild:

#### Using IDE (Recommended):

**Eclipse:**
1. Right-click project → Maven → Update Project → Force Update → OK
2. Project → Clean → Clean all projects
3. Right-click project → Run As → Maven build
   - Goals: `clean package`
   - Click Run
4. Wait for BUILD SUCCESS
5. Stop Tomcat server
6. Delete deployment:
   - In Servers view, right-click Tomcat → Clean
   - Or manually delete `<tomcat>/webapps/PlanMate` folder
7. Copy `target/PlanMate.war` to `<tomcat>/webapps/`
8. Start Tomcat

**IntelliJ IDEA:**
1. Right-click `pom.xml` → Maven → Reload Project
2. Build → Rebuild Project
3. Maven tool window → Lifecycle → clean → package
4. Stop Tomcat
5. Delete `<tomcat>/webapps/PlanMate` folder
6. Run/Debug → Deploy artifact
7. Start Tomcat

**VS Code:**
1. Maven extension → Reload Projects
2. Run Maven goal: `clean package`
3. Stop Tomcat
4. Copy `target/PlanMate.war` to Tomcat webapps
5. Start Tomcat

### Step 3: Test Diagnostic Endpoints

After redeploying, test these URLs in your browser:

1. **Diagnostic endpoint:**
   ```
   http://localhost:4353/PlanMate/diagnostic
   ```
   **Expected:** JSON response with "Spring MVC is working!"

2. **Environment check:**
   ```
   http://localhost:4353/PlanMate/diagnostic/env
   ```
   **Expected:** Shows which environment variables are set

3. **Health check:**
   ```
   http://localhost:4353/PlanMate/health
   ```
   **Expected:** JSON with database status

4. **Root URL:**
   ```
   http://localhost:4353/PlanMate/
   ```
   **Expected:** Redirects to trips page

### Step 4: Verify WAR Contents

Check that the WAR file contains all dependencies:

**Windows PowerShell:**
```powershell
cd c:\Users\ajdes\Downloads\PROJECTS\PlanMate\PlanMate\target
jar -tf PlanMate.war | Select-String "spring-webmvc"
```

**Expected output:**
```
WEB-INF/lib/spring-webmvc-5.3.32.jar
```

### Step 5: Check Deployment Directory

Verify Tomcat extracted the WAR:

1. Navigate to: `<tomcat-installation>/webapps/PlanMate/`
2. Check that `WEB-INF/lib/` exists and contains Spring JARs
3. Check that `WEB-INF/classes/` contains your compiled classes

---

## What I Fixed

### 1. Created DiagnosticController
- Simple endpoint to test if Spring MVC loads
- No database dependency
- URL: `/diagnostic`

### 2. Updated Database Configuration
- **Before:** Required PostgreSQL environment variables (would fail if not set)
- **After:** Uses H2 in-memory database by default for local development
- **Benefit:** Application starts without external database setup

### 3. Created Local Development Config
- File: `application-local.properties`
- Contains H2, MySQL, and PostgreSQL examples
- Easy to switch between databases

---

## Quick Test

After rebuilding and redeploying, try this in your browser:

```
http://localhost:4353/PlanMate/diagnostic
```

**If you see JSON response:** ✅ Spring MVC is working!
**If you still get 404:** ❌ Check Tomcat logs (Step 1)

---

## Common Issues and Solutions

### Issue: "Failed to load ApplicationContext"
**Solution:** Database connection problem. The H2 fallback should fix this.

### Issue: "ClassNotFoundException: DispatcherServlet"
**Solution:** 
- Verify you're using Tomcat 9.x (not 10.x) ✅ You are!
- Rebuild with Maven to package dependencies

### Issue: "404 on all URLs"
**Solution:**
- Application didn't deploy
- Check `<tomcat>/webapps/` for PlanMate folder
- Check Tomcat logs for deployment errors

### Issue: WAR file not updating
**Solution:**
1. Stop Tomcat
2. Delete `<tomcat>/webapps/PlanMate` folder AND `PlanMate.war`
3. Delete `<tomcat>/work/Catalina/localhost/PlanMate`
4. Copy fresh WAR from `target/`
5. Start Tomcat

---

## Port Configuration

Your Tomcat is running on port **4353** (not the default 8080).

To verify/change:
1. Open `<tomcat>/conf/server.xml`
2. Find: `<Connector port="4353"`
3. This is correct for your setup

---

## Next Steps

1. **Rebuild** your project (Step 2)
2. **Redeploy** to Tomcat
3. **Test** http://localhost:4353/PlanMate/diagnostic
4. **Check** Tomcat logs if still failing
5. **Report** any errors you see in the logs

---

## Database Notes

### Current Setup (After My Changes):
- **Local Development:** H2 in-memory database (no setup needed)
- **Production:** Set environment variables for PostgreSQL/MySQL

### To Use MySQL Locally:
Set these environment variables before starting Tomcat:
```
DB_DRIVER=com.mysql.cj.jdbc.Driver
DB_URL=jdbc:mysql://localhost:3306/planmate?createDatabaseIfNotExist=true
DB_USERNAME=root
DB_PASSWORD=yourpassword
HIBERNATE_DIALECT=org.hibernate.dialect.MySQL8Dialect
```

### To Use PostgreSQL Locally:
Set these environment variables:
```
DB_DRIVER=org.postgresql.Driver
DB_URL=jdbc:postgresql://localhost:5432/planmate
DB_USERNAME=postgres
DB_PASSWORD=yourpassword
HIBERNATE_DIALECT=org.hibernate.dialect.PostgreSQLDialect
```

---

## Success Indicators

✅ Tomcat logs show: `INFO: Deployment of web application directory [PlanMate] has finished`
✅ No ERROR or SEVERE messages in logs
✅ `/diagnostic` endpoint returns JSON
✅ `/health` endpoint returns database status
✅ Root URL redirects properly

---

**Status:** Please rebuild and redeploy, then test the diagnostic endpoint!
