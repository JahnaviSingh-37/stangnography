# Heroku Deployment Guide for PNG Steganography

## Prerequisites
- Heroku account: https://signup.heroku.com
- Heroku CLI installed: https://devcenter.heroku.com/articles/heroku-cli

## Deployment Steps

### 1. Install Heroku CLI
```bash
# macOS
brew tap heroku/brew && brew install heroku

# Verify installation
heroku --version
```

### 2. Login to Heroku
```bash
heroku login
```

### 3. Create Heroku Apps

#### Backend Deployment
```bash
# Navigate to project root
cd /Users/jahnavisingh/stangonograpy

# Create backend app
heroku create png-steganography-backend

# Set buildpack for Java
heroku buildpacks:set heroku/java -a png-steganography-backend

# Set environment variables
heroku config:set SPRING_PROFILES_ACTIVE=production -a png-steganography-backend
heroku config:set MONGODB_URI=mongodb://localhost:27017/steganography -a png-steganography-backend
heroku config:set BLOCKCHAIN_PRIVATE_KEY=dummy_key -a png-steganography-backend
heroku config:set BLOCKCHAIN_RPC_URL=https://rpc-amoy.polygon.technology/ -a png-steganography-backend

# Deploy backend (from backend directory)
git subtree push --prefix=backend heroku-backend main
```

#### Frontend Deployment
```bash
# Create frontend app
heroku create png-steganography-frontend

# Set buildpack for Node.js
heroku buildpacks:set heroku/nodejs -a png-steganography-frontend

# Set environment variables
heroku config:set REACT_APP_API_URL=https://png-steganography-backend.herokuapp.com -a png-steganography-frontend
heroku config:set NODE_ENV=production -a png-steganography-frontend

# Deploy frontend (from frontend directory)
git subtree push --prefix=frontend heroku-frontend main
```

### 4. Set Up Git Remotes
```bash
# Add Heroku remotes
heroku git:remote -a png-steganography-backend -r heroku-backend
heroku git:remote -a png-steganography-frontend -r heroku-frontend
```

### 5. Deploy Both Services
```bash
# Deploy backend
git subtree push --prefix=backend heroku-backend main

# Deploy frontend
git subtree push --prefix=frontend heroku-frontend main
```

## Live URLs
- Backend API: https://png-steganography-backend.herokuapp.com
- Frontend App: https://png-steganography-frontend.herokuapp.com

## Environment Variables

### Backend
- SPRING_PROFILES_ACTIVE=production
- MONGODB_URI=mongodb://localhost:27017/steganography
- BLOCKCHAIN_PRIVATE_KEY=dummy_key
- BLOCKCHAIN_RPC_URL=https://rpc-amoy.polygon.technology/

### Frontend
- REACT_APP_API_URL=https://png-steganography-backend.herokuapp.com
- NODE_ENV=production

## MongoDB Atlas Setup (Optional)
1. Go to https://cloud.mongodb.com
2. Create free cluster
3. Get connection string
4. Update MONGODB_URI: `heroku config:set MONGODB_URI=mongodb+srv://user:pass@cluster.mongodb.net/steganography -a png-steganography-backend`

## Troubleshooting
- Check logs: `heroku logs --tail -a app-name`
- Restart app: `heroku restart -a app-name`
- Check config: `heroku config -a app-name`
