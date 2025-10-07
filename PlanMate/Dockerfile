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

# Stage 2: Run on Tomcat (Render compatible)
FROM tomcat:9.0-jdk17-temurin
ENV CATALINA_OPTS="-Djava.security.egd=file:/dev/./urandom"
WORKDIR /usr/local/tomcat

# Remove default webapps and deploy our WAR as ROOT
RUN rm -rf webapps/*

# Copy the built WAR (using wildcard to avoid naming mismatch)
COPY --from=builder /workspace/target/*.war webapps/ROOT.war

# Expose correct port for Render
EXPOSE 8080

CMD ["catalina.sh", "run"]
