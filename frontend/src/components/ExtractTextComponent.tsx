import React, { useState } from 'react';
import { steganographyAPI } from '../services/api';

const ExtractTextComponent: React.FC = () => {
  const [imageFile, setImageFile] = useState<File | null>(null);
  const [transactionHash, setTransactionHash] = useState<string>('');
  const [loading, setLoading] = useState<boolean>(false);
  const [extractedText, setExtractedText] = useState<string>('');
  const [verified, setVerified] = useState<boolean | null>(null);
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
    
    if (!imageFile || !transactionHash.trim()) {
      setError('Please provide both an image and transaction hash');
      return;
    }

    setLoading(true);
    setError('');
    setExtractedText('');
    setVerified(null);

    try {
      const response = await steganographyAPI.extractText(imageFile, transactionHash.trim());
      
      if (response.success) {
        setExtractedText(response.extractedText || '');
        setVerified(response.verified || false);
      } else {
        setError(response.message || 'Failed to extract text');
        setVerified(false);
      }
    } catch (error: any) {
      console.error('Error extracting text:', error);
      setError(error.response?.data?.message || 'Failed to extract text from image');
      setVerified(false);
    } finally {
      setLoading(false);
    }
  };

  const copyToClipboard = (text: string) => {
    navigator.clipboard.writeText(text).then(() => {
      alert('Text copied to clipboard!');
    });
  };

  return (
    <div className="component-card">
      <h2>Extract & Verify Text</h2>
      <p>Upload a steganographic PNG image and provide the transaction hash to extract and verify the hidden text.</p>
      
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Steganographic PNG Image</label>
          <div className="file-input-wrapper">
            <input
              type="file"
              id="extract-image-input"
              className="file-input"
              accept="image/png"
              onChange={handleImageChange}
            />
            <label 
              htmlFor="extract-image-input" 
              className={`file-input-label ${imageFile ? 'has-file' : ''}`}
            >
              {imageFile ? `Selected: ${imageFile.name}` : '📁 Click to select PNG image'}
            </label>
          </div>
        </div>

        <div className="form-group">
          <label>Transaction Hash</label>
          <input
            type="text"
            value={transactionHash}
            onChange={(e) => setTransactionHash(e.target.value)}
            placeholder="Enter the blockchain transaction hash (0x...)"
            required
          />
        </div>

        <button type="submit" className="btn" disabled={loading || !imageFile || !transactionHash.trim()}>
          {loading ? 'Extracting & Verifying...' : '🔍 Extract & Verify Text'}
        </button>
      </form>

      {loading && (
        <div className="loading">
          <div className="progress-bar">
            <div className="progress-fill" style={{width: '70%'}}></div>
          </div>
          Extracting text and verifying against blockchain...
        </div>
      )}

      {error && <div className="error">{error}</div>}

      {extractedText && (
        <div className={verified ? 'success' : 'error'}>
          <h3>{verified ? '✅ Text Verified Successfully!' : '❌ Verification Failed!'}</h3>
          <div className="result-card">
            <p><strong>Verification Status:</strong> 
              <span style={{color: verified ? '#51cf66' : '#ff6b6b', marginLeft: '0.5rem'}}>
                {verified ? 'VERIFIED' : 'FAILED'}
              </span>
            </p>
            
            <div style={{marginTop: '1rem'}}>
              <label><strong>Extracted Text:</strong></label>
              <textarea
                value={extractedText}
                readOnly
                style={{
                  marginTop: '0.5rem',
                  backgroundColor: 'rgba(255, 255, 255, 0.1)',
                  border: `1px solid ${verified ? 'rgba(0, 255, 0, 0.3)' : 'rgba(255, 0, 0, 0.3)'}`,
                  minHeight: '120px'
                }}
              />
            </div>
            
            <div style={{marginTop: '1rem'}}>
              <button className="btn btn-secondary" onClick={() => copyToClipboard(extractedText)}>
                📋 Copy Text
              </button>
            </div>
            
            <div style={{marginTop: '1rem', fontSize: '0.9rem', opacity: 0.8}}>
              {verified ? (
                <>
                  💡 <strong>Success:</strong> The text integrity has been verified against the blockchain record.
                </>
              ) : (
                <>
                  ⚠️ <strong>Warning:</strong> The text may have been tampered with or the transaction hash is incorrect.
                </>
              )}
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default ExtractTextComponent;
