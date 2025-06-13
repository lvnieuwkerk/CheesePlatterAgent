# Use Java 21 as the base image
FROM eclipse-temurin:21-jdk-jammy

# Set working directory in the container
WORKDIR /app

# Copy the Maven wrapper files
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Copy the source code
COPY src ./src

# Build the application
RUN ./mvnw package -DskipTests

# Expose the port your application runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "target/cheesePlatterAgent-0.0.1-SNAPSHOT.jar"]
