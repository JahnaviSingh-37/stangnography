# 🖼️ PNG Steganography with Blockchain Verification

A full-stack application that hides text inside PNG images and verifies integrity using Polygon Mumbai testnet blockchain.

## 🚀 Features

- **🔒 Text Hiding**: Hide secret text messages in PNG images using LSB (Least Significant Bit) steganography
- **🔍 Text Extraction**: Extract hidden text from steganographic images
- **⛓️ Blockchain Verification**: Store SHA-256 hash on Polygon Mumbai testnet for integrity verification
- **📊 Record Management**: View all steganography operations and search by transaction hash
- **🎨 Modern UI**: Beautiful React frontend with responsive design
- **🔧 RESTful API**: Comprehensive Spring Boot backend with proper error handling

## 🏗️ Architecture

### Backend (Java Spring Boot)
- **Framework**: Spring Boot 3.1.2 with Java 17
- **Database**: MongoDB for metadata storage
- **Blockchain**: Web3j for Polygon Mumbai testnet integration
- **Image Processing**: Java BufferedImage for steganography operations
- **Security**: SHA-256 hashing for text integrity

### Frontend (React TypeScript)
- **Framework**: React 18 with TypeScript
- **Styling**: Custom CSS with glassmorphism design
- **API Client**: Axios for HTTP requests
- **UI Components**: Custom components for each functionality

### Blockchain
- **Network**: Polygon Mumbai Testnet
- **Storage**: Transaction data field for hash storage
- **Verification**: On-chain hash comparison for integrity

## 📁 Project Structure

```
stangonograpy/
├── backend/                          # Spring Boot backend
│   ├── src/main/java/com/steganography/
│   │   ├── SteganographyApplication.java
│   │   ├── controller/
│   │   │   └── SteganographyController.java
│   │   ├── service/
│   │   │   ├── SteganographyService.java
│   │   │   └── BlockchainService.java
│   │   ├── model/
│   │   │   └── SteganographyRecord.java
│   │   ├── util/
│   │   │   └── SteganographyUtil.java
│   │   └── config/
│   ├── src/main/resources/
│   │   └── application.yml
│   └── pom.xml
├── frontend/                         # React TypeScript frontend
│   ├── src/
│   │   ├── components/
│   │   │   ├── HideTextComponent.tsx
│   │   │   ├── ExtractTextComponent.tsx
│   │   │   └── RecordsComponent.tsx
│   │   ├── services/
│   │   │   └── api.ts
│   │   ├── App.tsx
│   │   ├── App.css
│   │   └── index.tsx
│   ├── public/
│   │   └── index.html
│   ├── package.json
│   └── tsconfig.json
├── postman/                          # Postman collections
│   └── steganography-api.json
├── docs/                             # Documentation
│   ├── api-documentation.md
│   └── setup-guide.md
└── README.md
```

## 🛠️ Setup & Installation

### Prerequisites

- **Java 17+**
- **Node.js 16+**
- **MongoDB 4.4+**
- **Maven 3.6+**
- **Polygon Mumbai testnet account with MATIC tokens**

### Backend Setup

1. **Clone the repository**
```bash
git clone <repository-url>
cd stangonograpy/backend
```

2. **Configure MongoDB**
```bash
# Start MongoDB (macOS with Homebrew)
brew services start mongodb-community

# Or start manually
mongod --config /usr/local/etc/mongod.conf
```

3. **Configure Blockchain Settings**
```bash
# Edit backend/src/main/resources/application.yml
# Update the following values:
blockchain:
  private:
    key: "your-polygon-mumbai-private-key"
```

4. **Build and Run Backend**
```bash
mvn clean install
mvn spring-boot:run
```

Backend will start on `http://localhost:8080`

### Frontend Setup

1. **Navigate to frontend directory**
```bash
cd ../frontend
```

2. **Install dependencies**
```bash
npm install
```

3. **Start development server**
```bash
npm start
```

Frontend will start on `http://localhost:3000`

## 🔌 API Endpoints

### Hide Text
```http
POST /api/steganography/hide
Content-Type: multipart/form-data

Parameters:
- image: PNG image file
- text: Text to hide
```

### Extract Text
```http
POST /api/steganography/extract
Content-Type: multipart/form-data

Parameters:
- image: Steganographic PNG image
- transactionHash: Blockchain transaction hash
```

### Verify Text
```http
POST /api/steganography/verify
Content-Type: multipart/form-data

Parameters:
- text: Text to verify
- transactionHash: Blockchain transaction hash
```

### Get All Records
```http
GET /api/steganography/records
```

### Get Record by Transaction Hash
```http
GET /api/steganography/records/transaction/{transactionHash}
```

### Generate Hash
```http
POST /api/steganography/hash
Content-Type: multipart/form-data

Parameters:
- text: Text to hash
```

### Health Check
```http
GET /api/steganography/health
```

## 🧪 Testing with Postman

Import the Postman collection from `postman/steganography-api.json` to test all API endpoints.

### Test Scenarios

1. **Hide Text Flow**
   - Upload a PNG image
   - Enter secret text
   - Verify transaction hash is returned
   - Check MongoDB for record creation

2. **Extract & Verify Flow**
   - Upload steganographic image
   - Provide transaction hash
   - Verify text extraction and blockchain verification

3. **Records Management**
   - List all records
   - Search by transaction hash
   - View individual record details

## 🔐 Security Considerations

- **Private Key Management**: Store blockchain private keys securely using environment variables
- **Input Validation**: All endpoints validate input data and file types
- **Error Handling**: Comprehensive error handling with proper HTTP status codes
- **CORS Configuration**: Properly configured for frontend-backend communication

## 🌐 Blockchain Integration

### Polygon Mumbai Testnet

- **RPC URL**: `https://rpc-mumbai.maticvigil.com/`
- **Chain ID**: 80001
- **Currency**: MATIC (testnet)
- **Explorer**: https://mumbai.polygonscan.com/

### Getting Test MATIC

1. Visit [Polygon Faucet](https://faucet.polygon.technology/)
2. Enter your wallet address
3. Select Mumbai testnet
4. Claim free MATIC tokens

## 📚 How It Works

### Steganography Process

1. **Text Hiding**:
   - Load PNG image into BufferedImage
   - Convert text to bytes with delimiter
   - Modify LSB of RGB channels to embed text bits
   - Generate SHA-256 hash of original text
   - Store hash on Polygon blockchain
   - Save steganographic image

2. **Text Extraction**:
   - Load steganographic image
   - Extract LSB from RGB channels
   - Reconstruct text using delimiter
   - Generate SHA-256 hash of extracted text
   - Compare with blockchain-stored hash
   - Verify integrity

### Blockchain Verification

1. **Storage**: SHA-256 hash stored in transaction data field
2. **Verification**: Compare extracted text hash with blockchain record
3. **Integrity**: Detect any tampering or corruption

## 🚀 Deployment

### Backend Deployment

```bash
# Build JAR file
mvn clean package

# Run with production profile
java -jar target/png-steganography-1.0.0.jar --spring.profiles.active=prod
```

### Frontend Deployment

```bash
# Build for production
npm run build

# Serve static files (use nginx, apache, or any static file server)
```

## 🐛 Troubleshooting

### Common Issues

1. **MongoDB Connection Failed**
   - Ensure MongoDB is running: `brew services start mongodb-community`
   - Check connection string in `application.yml`

2. **Blockchain Transaction Failed**
   - Verify private key is correct
   - Ensure sufficient MATIC balance
   - Check Mumbai testnet connectivity

3. **Image Processing Error**
   - Only PNG images are supported
   - Ensure image has sufficient capacity for text
   - Check file size limits (10MB max)

4. **CORS Issues**
   - Backend allows all origins in development
   - Configure proper CORS for production

## 🤝 Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **Polygon Team** for providing excellent testnet infrastructure
- **Spring Boot Community** for comprehensive documentation
- **React Team** for the amazing frontend framework
- **Web3j Team** for seamless blockchain integration

## 📞 Support

For support and questions:
- Create an issue in this repository
- Contact the development team

---

**Built with ❤️ using Java Spring Boot, React TypeScript, and Polygon Blockchain**
