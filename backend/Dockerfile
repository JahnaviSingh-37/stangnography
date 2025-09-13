FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY mvnw pom.xml ./
COPY .mvn .mvn

# Copy source code
COPY src src

# Build the application
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# Expose port
EXPOSE 8080

# Set default port
ENV PORT=8080

# Run the application
CMD ["sh", "-c", "java -Dserver.port=${PORT} -Dspring.profiles.active=production -jar target/png-steganography-1.0.0.jar"]
