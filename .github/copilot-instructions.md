# PNG Steganography with Blockchain Verification

Full-stack application for hiding text in PNG images and verifying integrity using Polygon testnet.

## Architecture
- **Backend**: Java Spring Boot with steganography and blockchain integration
- **Frontend**: React TypeScript with modern UI
- **Blockchain**: Polygon Mumbai testnet for hash storage
- **Testing**: Postman collections for API testing

## Features
- Hide text messages in PNG images using LSB steganography
- Extract hidden text from steganographic images
- Store SHA-256 hash on Polygon Mumbai testnet
- Verify message integrity against blockchain
- RESTful API with comprehensive endpoints
- React frontend with file upload/download
- MongoDB for metadata storage

## Development Guidelines
- Use Spring Boot best practices
- Implement proper error handling
- Add comprehensive logging
- Follow REST API conventions
- Include proper input validation
- Use environment variables for sensitive data

## Progress Tracking
- [x] Verify copilot-instructions.md created
- [x] Clarify Project Requirements  
- [x] Scaffold the Project
- [x] Customize the Project
- [ ] Install Required Extensions
- [ ] Compile the Project
- [ ] Create and Run Task
- [ ] Launch the Project
- [x] Ensure Documentation is Complete

## Project Structure Created
✅ Backend (Spring Boot):
- Main application class
- REST Controller with all endpoints
- Service layer for business logic
- MongoDB model for data persistence
- Blockchain service for Polygon integration
- Steganography utility for image processing
- CORS configuration
- Application configuration

✅ Frontend (React TypeScript):
- Main App component with navigation
- Hide text component
- Extract text component
- Records management component
- API service layer
- Modern glassmorphism UI design
- Responsive layout

✅ Documentation:
- Comprehensive README
- API documentation
- Postman collection for testing
- Setup instructions

## Next Steps
1. Install MongoDB and start service
2. Configure Polygon Mumbai private key
3. Install backend dependencies: `mvn clean install`
4. Install frontend dependencies: `npm install`
5. Start backend: `mvn spring-boot:run`
6. Start frontend: `npm start`
7. Test with Postman collection
