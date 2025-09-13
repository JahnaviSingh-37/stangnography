#!/bin/bash
echo "🚀 Starting PNG Steganography Backend..."
cd backend
mvn clean package -DskipTests
echo "✅ Build complete! Starting server..."
java -Dserver.port=8080 -jar target/png-steganography-1.0.0.jar
