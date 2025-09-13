FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy all files
COPY . .

# Make mvnw executable and build
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Expose port
EXPOSE 8080

# Run the application
CMD ["java", "-Dserver.port=${PORT:-8080}", "-Dspring.profiles.active=production", "-jar", "target/png-steganography-1.0.0.jar"]
