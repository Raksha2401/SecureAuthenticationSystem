FROM openjdk:21-jdk-slim
WORKDIR /app
COPY app/build/libs/SecureAuthenticationSystem-plain.jar /app/SecureAuthenticationSystem.jar
CMD ["java", "-jar", "SecureAuthenticationSystem.jar"]