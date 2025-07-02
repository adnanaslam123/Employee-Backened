# Use a base image with Java + Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy the project files
COPY . .

# Build the Spring Boot app
RUN mvn clean package -DskipTests

# Now create the runtime image
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy only the built jar from the previous stage
COPY --from=build /app/target/*.jar app.jar

# Expose port for Render
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
