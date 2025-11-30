# QUICK FIX: Eclipse Missing Maven Dependencies

## The Problem
Eclipse is NOT copying Maven dependencies (Spring JARs) to Tomcat's deployment folder.

## The Solution (5 Minutes)

### ⚠️ CRITICAL STEP: Add Maven Dependencies to Deployment

1. **Right-click** `PlanMate` project
2. Click **Properties**
3. Click **Deployment Assembly** (left sidebar)
4. Look for an entry called **"Maven Dependencies"**

**If you DON'T see "Maven Dependencies":**

5. Click **Add...** button
6. Select **Java Build Path Entries**
7. Click **Next >**
8. Check **Maven Dependencies**
9. Click **Finish**
10. Click **Apply and Close**

**Expected result:**
```
Source                    Deploy Path
/src/main/java           WEB-INF/classes
/src/main/resources      WEB-INF/classes
/src/main/webapp         /
Maven Dependencies       WEB-INF/lib    ← THIS MUST BE HERE!
```

---

### Clean and Redeploy

11. **Project** → **Clean** → Select PlanMate → **Clean**
12. **Servers** view → Right-click Tomcat → **Clean...**
13. **Servers** view → Right-click Tomcat → **Stop** (if running)
14. **Servers** view → Right-click Tomcat → **Start**

---

### Test

15. Open browser: **http://localhost:3232/PlanMate/diagnostic**

**Expected:** JSON response with "Spring MVC is working!"

---

## If Still Not Working

### Verify Deployment

Navigate to:
```
C:\Users\ajdes\Downloads\Angular\Stock\StockAdvarsaryAPI\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\PlanMate\WEB-INF\lib
```

**Check:** Do you see `spring-webmvc-5.3.32.jar` and other Spring JARs?

- ✅ **YES:** Restart Tomcat and try again
- ❌ **NO:** Maven Dependencies is still not configured correctly

### Nuclear Option: Reimport Project

1. Close Eclipse
2. Navigate to project folder
3. Delete these files/folders:
   - `.settings/`
   - `.project`
   - `.classpath`
4. Open Eclipse
5. **File** → **Import** → **Existing Maven Projects**
6. Browse to `PlanMate` folder
7. **Finish**
8. Repeat "Add Maven Dependencies" steps above
9. Add project to Tomcat server
10. Start server

---

## Your Port Number

Your Tomcat is on port **3232** (not 4353)

All URLs should use: `http://localhost:3232/PlanMate/...`

---

## Success Indicators

✅ Console shows: `INFO: Initializing Spring DispatcherServlet 'spring'`
✅ No `ClassNotFoundException` errors
✅ `/diagnostic` endpoint returns JSON
✅ `WEB-INF/lib` contains Spring JARs

---

**The fix is simple: Eclipse needs to know to copy Maven Dependencies to WEB-INF/lib!**
