# Railway Deployment Guide for PNG Steganography

## Prerequisites
- GitHub account with your code repository
- Railway account: https://railway.app

## Deployment Steps

### 1. Sign Up for Railway
1. Go to https://railway.app
2. Sign up with your GitHub account
3. No credit card required

### 2. Deploy Backend
1. Click "New Project"
2. Select "Deploy from GitHub repo"
3. Choose your repository: `JahnaviSingh-37/stangnography`
4. Select "backend" folder as root directory
5. Railway will auto-detect Java and deploy

### 3. Deploy Frontend
1. Click "New Project" again
2. Select "Deploy from GitHub repo"
3. Choose your repository: `JahnaviSingh-37/stangnography`
4. Select "frontend" folder as root directory
5. Railway will auto-detect Node.js and deploy

### 4. Environment Variables

#### Backend Service
- SPRING_PROFILES_ACTIVE=production
- MONGODB_URI=mongodb://localhost:27017/steganography
- BLOCKCHAIN_PRIVATE_KEY=dummy_key
- BLOCKCHAIN_RPC_URL=https://rpc-amoy.polygon.technology/

#### Frontend Service
- REACT_APP_API_URL=https://[backend-url].railway.app
- NODE_ENV=production

### 5. Custom Domains
Railway provides custom domains like:
- Backend: https://png-steganography-backend-production.up.railway.app
- Frontend: https://png-steganography-frontend-production.up.railway.app

## Features
✅ Free tier (500 hours/month)
✅ Automatic HTTPS
✅ GitHub integration
✅ Environment variables
✅ Custom domains
✅ PostgreSQL database (optional)

## No Credit Card Required!
