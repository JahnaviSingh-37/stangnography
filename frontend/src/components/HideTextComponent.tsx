import React, { useState } from 'react';
import { steganographyAPI, SteganographyRecord } from '../services/api';

const HideTextComponent: React.FC = () => {
  const [imageFile, setImageFile] = useState<File | null>(null);
  const [text, setText] = useState<string>('');
  const [loading, setLoading] = useState<boolean>(false);
  const [result, setResult] = useState<SteganographyRecord | null>(null);
  const [error, setError] = useState<string>('');

  const handleImageChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];
    if (file) {
      if (file.type !== 'image/png') {
        setError('Please select a PNG image file');
        return;
      }
      setImageFile(file);
      setError('');
    }
  };

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    
    if (!imageFile || !text.trim()) {
      setError('Please provide both an image and text to hide');
      return;
    }

    setLoading(true);
    setError('');
    setResult(null);

    try {
      const response = await steganographyAPI.hideText(imageFile, text.trim());
      
      if (response.success && response.record) {
        setResult(response.record);
        setText('');
        setImageFile(null);
      } else {
        setError(response.message || 'Failed to hide text');
      }
    } catch (error: any) {
      console.error('Error hiding text:', error);
      setError(error.response?.data?.message || 'Failed to hide text in image');
    } finally {
      setLoading(false);
    }
  };

  const downloadStegoImage = () => {
    if (result) {
      // In a real implementation, you would get the actual image bytes from the server
      alert('Download functionality would be implemented to get the steganographic image from the server');
    }
  };

  return (
    <div className="component-card">
      <h2>Hide Text in PNG Image</h2>
      <p>Upload a PNG image and enter text to hide. The text hash will be stored on Polygon Mumbai testnet.</p>
      
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>PNG Image</label>
          <div className="file-input-wrapper">
            <input
              type="file"
              id="image-input"
              className="file-input"
              accept="image/png"
              onChange={handleImageChange}
            />
            <label 
              htmlFor="image-input" 
              className={`file-input-label ${imageFile ? 'has-file' : ''}`}
            >
              {imageFile ? `Selected: ${imageFile.name}` : '📁 Click to select PNG image'}
            </label>
          </div>
        </div>

        <div className="form-group">
          <label>Text to Hide</label>
          <textarea
            value={text}
            onChange={(e) => setText(e.target.value)}
            placeholder="Enter the secret text you want to hide in the image..."
            required
          />
        </div>

        <button type="submit" className="btn" disabled={loading || !imageFile || !text.trim()}>
          {loading ? 'Hiding Text & Storing Hash...' : '🔒 Hide Text'}
        </button>
      </form>

      {loading && (
        <div className="loading">
          <div className="progress-bar">
            <div className="progress-fill" style={{width: '50%'}}></div>
          </div>
          Processing image and storing hash on blockchain...
        </div>
      )}

      {error && <div className="error">{error}</div>}

      {result && (
        <div className="success">
          <h3>✅ Text Hidden Successfully!</h3>
          <div className="result-card">
            <p><strong>File Name:</strong> {result.fileName}</p>
            <p><strong>SHA-256 Hash:</strong> <code>{result.textHash}</code></p>
            <p><strong>Transaction Hash:</strong> <code>{result.transactionHash}</code></p>
            <p><strong>Blockchain Address:</strong> <code>{result.blockchainAddress}</code></p>
            <p><strong>Status:</strong> {result.status}</p>
            <p><strong>Created At:</strong> {new Date(result.createdAt).toLocaleString()}</p>
            
            <div style={{marginTop: '1rem'}}>
              <button className="btn btn-secondary" onClick={downloadStegoImage}>
                📥 Download Steganographic Image
              </button>
            </div>
            
            <div style={{marginTop: '1rem', fontSize: '0.9rem', opacity: 0.8}}>
              💡 <strong>Important:</strong> Save the transaction hash to extract and verify the text later!
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default HideTextComponent;
