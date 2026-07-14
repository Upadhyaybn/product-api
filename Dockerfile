# ---------- Build Stage ----------

# Java 21 और Maven वाली image से application build करें.
FROM maven:3.9-eclipse-temurin-21 AS build

# Container के अंदर working folder.
WORKDIR /app

# Maven configuration पहले copy करें.
COPY pom.xml .

# Maven wrapper files copy करें.
COPY mvnw .
COPY .mvn .mvn

# Linux container में wrapper executable बनाएँ.
RUN chmod +x mvnw

# Source code copy करें.
COPY src ./src

# Tests चलाकर executable Spring Boot JAR बनाएँ.
RUN ./mvnw clean package


# ---------- Runtime Stage ----------

# Final image में केवल lightweight Java runtime रखें.
FROM eclipse-temurin:21-jre

# Runtime working directory.
WORKDIR /app

# Build stage से generated JAR copy करें.
COPY --from=build /app/target/*.jar app.jar

# Application port documentation.
EXPOSE 8080

# Container start होते ही Spring Boot application चलेगी.
ENTRYPOINT ["java", "-jar", "app.jar"]