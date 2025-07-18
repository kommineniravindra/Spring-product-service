# Use an official JDK base image
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from host into the container
COPY target/*.jar app.jar

# Expose port (Render sets PORT env automatically)
EXPOSE 8082

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
