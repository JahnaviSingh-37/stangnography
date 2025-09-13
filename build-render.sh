#!/bin/bash

echo "🔨 Building PNG Steganography Application for Render..."

# Set permissions for Maven wrapper
chmod +x backend/mvnw

# Build backend
echo "📦 Building Spring Boot backend..."
cd backend
./mvnw clean package -DskipTests
if [ $? -ne 0 ]; then
    echo "❌ Backend build failed"
    exit 1
fi

echo "✅ Backend build completed successfully"
echo "📁 JAR file created: target/png-steganography-1.0.0.jar"

cd ..

# Build frontend
echo "📦 Building React frontend..."
cd frontend
npm install
if [ $? -ne 0 ]; then
    echo "❌ Frontend dependencies installation failed"
    exit 1
fi

npm run build
if [ $? -ne 0 ]; then
    echo "❌ Frontend build failed"
    exit 1
fi

echo "✅ Frontend build completed successfully"
echo "📁 Build files created in: build/"

cd ..

echo "🎉 All builds completed successfully!"
echo "🚀 Ready for deployment to Render!"
