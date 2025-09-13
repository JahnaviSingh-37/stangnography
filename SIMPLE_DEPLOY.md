# 🚀 Simple Deployment with Vercel

## Why Vercel?
- ✅ **One platform** for both frontend AND backend
- ✅ **Zero configuration** needed
- ✅ **Automatic HTTPS**
- ✅ **Free tier** is generous
- ✅ **Deploy in 2 minutes**

## Step 1: Install Vercel CLI

```bash
npm install -g vercel
```

## Step 2: Deploy Frontend

```bash
cd frontend
vercel
```

Follow the prompts:
- **Set up and deploy?** → Yes
- **Which scope?** → Your account
- **Link to existing project?** → No
- **Project name?** → steganography-frontend
- **Directory?** → ./
- **Override settings?** → No

## Step 3: Deploy Backend

```bash
cd ../backend
vercel
```

Follow the prompts:
- **Set up and deploy?** → Yes
- **Which scope?** → Your account  
- **Link to existing project?** → No
- **Project name?** → steganography-backend
- **Directory?** → ./
- **Override settings?** → No

## Step 4: Set Environment Variables

1. Go to [vercel.com/dashboard](https://vercel.com/dashboard)
2. Click on your backend project
3. Go to Settings → Environment Variables
4. Add:
   - `MONGODB_URI` = your MongoDB connection string
   - `PRIVATE_KEY` = your Ethereum private key
   - `RPC_URL` = `https://rpc-amoy.polygon.technology/`

## Step 5: Update Frontend API URL

1. Go to your frontend project settings on Vercel
2. Add environment variable:
   - `REACT_APP_API_URL` = `https://your-backend-url.vercel.app`

## Step 6: Redeploy

```bash
# Redeploy frontend with new environment variable
cd frontend
vercel --prod

# Redeploy backend with environment variables
cd ../backend  
vercel --prod
```

## 🎉 Done!

Your app will be live at:
- **Frontend**: `https://steganography-frontend.vercel.app`
- **Backend**: `https://steganography-backend.vercel.app`

---

## Alternative: Even Simpler - Netlify Drop

If Vercel doesn't work, try this **drag-and-drop** approach:

### Frontend:
1. Go to [netlify.com](https://netlify.com)
2. Drag your `frontend/build` folder to the deploy area
3. Done! ✅

### Backend:
Use **Railway One-Click**:
1. Go to [railway.app](https://railway.app)
2. Click "Deploy Now" 
3. Connect GitHub → Select repo → Auto-deploy ✅

This is literally the easiest way possible! 🚀
