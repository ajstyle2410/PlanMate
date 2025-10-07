# syntax=docker/dockerfile:1

# Stage 1: Build the WAR using Maven and Temurin JDK 17
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /workspace

# Cache dependencies
COPY pom.xml ./
RUN mvn -B dependency:go-offline

# Copy source and build the project
COPY src ./src
RUN mvn -B clean package -DskipTests

# Stage 2: Run on Tomcat (suitable for Render Docker deployment)
FROM tomcat:9.0-jdk17-temurin
ENV CATALINA_OPTS="-Djava.security.egd=file:/dev/./urandom"
WORKDIR /usr/local/tomcat

# Remove default webapps and deploy our WAR as ROOT
RUN rm -rf webapps/*
COPY --from=builder /workspace/target/PlanMate.war webapps/ROOT.war

# Render exposes the service on port 8080 by default
EXPOSE 8000

CMD ["catalina.sh", "run"]
