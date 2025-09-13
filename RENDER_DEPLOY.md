# 🚀 Deploy PNG Steganography to Render

## Prerequisites
1. GitHub account
2. Render account (free at render.com)
3. MongoDB Atlas account (free at cloud.mongodb.com)

## Step-by-Step Deployment

### 1. Prepare Your Repository
```bash
# Make build script executable
chmod +x build-render.sh

# Test build locally (optional)
./build-render.sh

# Commit all changes
git add .
git commit -m "Prepare for Render deployment"
git push origin main
```

### 2. Set Up MongoDB Atlas (Free Database)
1. Go to https://cloud.mongodb.com
2. Create a free account and cluster
3. Create a database user
4. Get your connection string (replace <password> with actual password)
   ```
   mongodb+srv://username:password@cluster.mongodb.net/steganography
   ```

### 3. Deploy Backend to Render
1. Go to https://render.com and login
2. Click "New +" → "Web Service"
3. Connect your GitHub repository
4. Configure:
   - **Name**: `png-steganography-backend`
   - **Runtime**: `Java`
   - **Build Command**: 
     ```bash
     cd backend && chmod +x mvnw && ./mvnw clean package -DskipTests
     ```
   - **Start Command**: 
     ```bash
     cd backend && java -Dserver.port=$PORT -Dspring.profiles.active=production -jar target/png-steganography-1.0.0.jar
     ```
   - **Plan**: Free
   
5. Add Environment Variables:
   - `SPRING_PROFILES_ACTIVE` = `production`
   - `MONGODB_URI` = `your_mongodb_atlas_connection_string`
   - `BLOCKCHAIN_PRIVATE_KEY` = `your_polygon_private_key`
   - `BLOCKCHAIN_RPC_URL` = `https://rpc-amoy.polygon.technology/`

6. Click "Create Web Service"

### 4. Deploy Frontend to Render
1. Click "New +" → "Web Service" 
2. Connect your GitHub repository again
3. Configure:
   - **Name**: `png-steganography-frontend`
   - **Runtime**: `Node`
   - **Build Command**: 
     ```bash
     cd frontend && npm install && npm run build
     ```
   - **Start Command**: 
     ```bash
     cd frontend && npm run serve
     ```
   - **Plan**: Free

4. Add Environment Variables:
   - `NODE_ENV` = `production`
   - `REACT_APP_API_URL` = `https://png-steganography-backend.onrender.com/api/steganography`

5. Click "Create Web Service"

### 5. Update CORS Configuration
After both services are deployed, update your backend's CORS configuration:

1. Note your frontend URL (e.g., `https://png-steganography-frontend.onrender.com`)
2. Add it to backend environment variables:
   - `FRONTEND_URL` = `https://png-steganography-frontend.onrender.com`

### 6. Test Your Deployment
1. Visit your frontend URL
2. Try hiding text in a PNG image
3. Try extracting text from a steganographic image
4. Check the records page

## Important Notes

### Free Tier Limitations
- Services sleep after 15 minutes of inactivity
- First request after sleep may be slow (cold start)
- 750 hours/month total across all services

### MongoDB Atlas Free Tier
- 512MB storage
- Shared cluster
- No credit card required

### Security Considerations
- Never commit real private keys to Git
- Use Render's environment variables for secrets
- MongoDB connection string should include authentication

## Troubleshooting

### Backend Issues
- Check Render logs for Spring Boot startup errors
- Verify MongoDB connection string
- Ensure Java 17+ is being used

### Frontend Issues
- Check console for API connection errors
- Verify API URL environment variable
- Ensure CORS is properly configured

### Build Issues
- Verify Maven wrapper permissions: `chmod +x backend/mvnw`
- Check Node.js version compatibility
- Ensure all dependencies are in package.json

## URLs
- **Frontend**: https://png-steganography-frontend.onrender.com
- **Backend**: https://png-steganography-backend.onrender.com
- **API Health**: https://png-steganography-backend.onrender.com/api/steganography/records

## Support
If you encounter issues:
1. Check Render dashboard logs
2. Verify environment variables
3. Test API endpoints directly
4. Check MongoDB Atlas connection
