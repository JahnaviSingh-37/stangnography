const express = require('express');
const path = require('path');
const app = express();

const PORT = process.env.PORT || 3000;

// Serve static files from the React app build directory
app.use(express.static(path.join(__dirname, 'build')));

// API routes should be handled by the backend
app.get('/api/*', (req, res) => {
  res.status(404).json({ error: 'API endpoint not found. Use backend service.' });
});

// Serve the React app for all other routes
app.get('*', (req, res) => {
  res.sendFile(path.join(__dirname, 'build', 'index.html'));
});

app.listen(PORT, () => {
  console.log(`🚀 Frontend server running on port ${PORT}`);
  console.log(`📱 Environment: ${process.env.NODE_ENV || 'development'}`);
  console.log(`🔗 API URL: ${process.env.REACT_APP_API_URL || 'http://localhost:8080'}`);
});
