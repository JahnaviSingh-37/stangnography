# 🚀 Quick Deployment Checklist

## Before You Deploy

### ✅ Prerequisites Setup
- [ ] Create GitHub repository and push your code
- [ ] Sign up for Render account (free): https://render.com
- [ ] Sign up for MongoDB Atlas (free): https://cloud.mongodb.com
- [ ] Get a Polygon testnet private key (for blockchain features)

### ✅ MongoDB Atlas Setup (5 minutes)
1. [ ] Create free cluster at cloud.mongodb.com
2. [ ] Create database user with username/password
3. [ ] Whitelist IP addresses (0.0.0.0/0 for all)
4. [ ] Copy connection string: `mongodb+srv://username:password@cluster.mongodb.net/steganography`

### ✅ Deploy to Render

#### Backend Deployment:
1. [ ] Go to render.com → "New +" → "Web Service"
2. [ ] Connect GitHub repo: `stangonograpy`
3. [ ] Settings:
   - **Name**: `png-steganography-backend`
   - **Runtime**: Java
   - **Build Command**: `cd backend && chmod +x mvnw && ./mvnw clean package -DskipTests`
   - **Start Command**: `cd backend && java -Dserver.port=$PORT -jar target/png-steganography-1.0.0.jar`
   - **Plan**: Free

4. [ ] Environment Variables:
   ```
   MONGODB_URI = mongodb+srv://username:password@cluster.mongodb.net/steganography
   BLOCKCHAIN_PRIVATE_KEY = your_private_key_without_0x
   SPRING_PROFILES_ACTIVE = production
   ```

#### Frontend Deployment:
1. [ ] "New +" → "Web Service" (same GitHub repo)
2. [ ] Settings:
   - **Name**: `png-steganography-frontend`  
   - **Runtime**: Node
   - **Build Command**: `cd frontend && npm install && npm run build`
   - **Start Command**: `cd frontend && npm run serve`
   - **Plan**: Free

3. [ ] Environment Variables:
   ```
   REACT_APP_API_URL = https://png-steganography-backend.onrender.com/api/steganography
   NODE_ENV = production
   ```

### ✅ Final Steps
- [ ] Wait for both services to build (5-10 minutes)
- [ ] Test frontend URL: `https://png-steganography-frontend.onrender.com`
- [ ] Test hiding text in a PNG image
- [ ] Test extracting text from steganographic image
- [ ] Share your live URL with others! 🎉

## Your Live URLs
- **Frontend**: `https://png-steganography-frontend.onrender.com`
- **Backend API**: `https://png-steganography-backend.onrender.com`

## Need Help?
- Check Render dashboard logs if builds fail
- Verify MongoDB connection in backend logs  
- Ensure environment variables are set correctly
- Test API endpoints directly first

**Total Setup Time**: ~15-20 minutes
**Cost**: $0.00 (completely free!)

## What Users Can Do
- Upload PNG images and hide secret text
- Download steganographic images
- Extract hidden messages with integrity verification
- View all steganography operations
- Works on any device with a web browser

Your app will be accessible to anyone worldwide! 🌍
