# Use a recent JDK
FROM openjdk:21-jdk-slim

WORKDIR /app

# Copy the bootable Spring Boot JAR
COPY app/build/libs/app.jar /app/SecureAuthenticationSystem.jar

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "SecureAuthenticationSystem.jar"]