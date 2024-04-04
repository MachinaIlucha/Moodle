# Use Maven with JDK 17 as the base image for building the application
FROM maven:3.8.4-openjdk-17 as builder

# Copy the project files into the Docker image
# The build context must be set to the root of the Maven project where the pom.xml file is located
COPY . /app

# Set the working directory to the app directory
WORKDIR /app

# Build the application using Maven
RUN mvn clean package -B -DskipTests -pl '!e2e-test'

# Use JDK 17 slim image for the final image to reduce size
FROM openjdk:17-slim

# Copy the built jar file from the builder stage to the final image
COPY --from=builder /app/api-core/target/api-moodle-0.0.1.jar /app/api-moodle-0.0.1.jar

# Set the working directory in the container to /app
WORKDIR /app

# Command to run the application
CMD ["java", "-jar", "api-moodle-0.0.1.jar"]
