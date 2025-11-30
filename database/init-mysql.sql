-- PlanMate Database Initialization Script for MySQL
-- Run this script to create the database and initial schema

-- Create database
CREATE DATABASE IF NOT EXISTS planmate_dev CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE planmate_dev;

-- Note: Tables will be auto-created by Hibernate with ddl-auto=update
-- This script is for reference and manual setup if needed

-- Optional: Create a dedicated user for the application
-- CREATE USER IF NOT EXISTS 'planmate_user'@'localhost' IDENTIFIED BY 'planmate_password';
-- GRANT ALL PRIVILEGES ON planmate_dev.* TO 'planmate_user'@'localhost';
-- FLUSH PRIVILEGES;

-- Verify database creation
SHOW DATABASES LIKE 'planmate_dev';
