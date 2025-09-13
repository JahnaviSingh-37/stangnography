import React, { useState, useEffect } from 'react';
import { steganographyAPI, SteganographyRecord } from '../services/api';

const RecordsComponent: React.FC = () => {
  const [records, setRecords] = useState<SteganographyRecord[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string>('');
  const [searchHash, setSearchHash] = useState<string>('');
  const [searchResult, setSearchResult] = useState<SteganographyRecord | null>(null);
  const [searchLoading, setSearchLoading] = useState<boolean>(false);

  useEffect(() => {
    loadAllRecords();
  }, []);

  const loadAllRecords = async () => {
    try {
      setLoading(true);
      const response = await steganographyAPI.getAllRecords();
      
      if (response.success && response.records) {
        setRecords(response.records);
      } else {
        setError(response.message || 'Failed to load records');
      }
    } catch (error: any) {
      console.error('Error loading records:', error);
      setError('Failed to load records');
    } finally {
      setLoading(false);
    }
  };

  const searchByTransactionHash = async () => {
    if (!searchHash.trim()) {
      setError('Please enter a transaction hash');
      return;
    }

    try {
      setSearchLoading(true);
      setError('');
      const response = await steganographyAPI.getRecordByTransactionHash(searchHash.trim());
      
      if (response.success && response.record) {
        setSearchResult(response.record);
      } else {
        setError('No record found for this transaction hash');
        setSearchResult(null);
      }
    } catch (error: any) {
      console.error('Error searching record:', error);
      setError('Failed to search record');
      setSearchResult(null);
    } finally {
      setSearchLoading(false);
    }
  };

  const copyToClipboard = (text: string) => {
    navigator.clipboard.writeText(text).then(() => {
      alert('Copied to clipboard!');
    });
  };

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleString();
  };

  const shortenHash = (hash: string, length: number = 10) => {
    if (hash.length <= length * 2) return hash;
    return `${hash.substring(0, length)}...${hash.substring(hash.length - length)}`;
  };

  return (
    <div className="component-card">
      <h2>Steganography Records</h2>
      <p>View all steganography operations and search by transaction hash.</p>

      {/* Search Section */}
      <div style={{ marginBottom: '2rem', padding: '1rem', background: 'rgba(255, 255, 255, 0.05)', borderRadius: '8px' }}>
        <h3>Search by Transaction Hash</h3>
        <div style={{ display: 'flex', gap: '1rem', alignItems: 'flex-end' }}>
          <div className="form-group" style={{ flex: 1, marginBottom: 0 }}>
            <input
              type="text"
              value={searchHash}
              onChange={(e) => setSearchHash(e.target.value)}
              placeholder="Enter transaction hash (0x...)"
            />
          </div>
          <button 
            className="btn" 
            onClick={searchByTransactionHash}
            disabled={searchLoading || !searchHash.trim()}
          >
            {searchLoading ? 'Searching...' : '🔍 Search'}
          </button>
        </div>

        {searchResult && (
          <div className="result-card" style={{ marginTop: '1rem' }}>
            <h4>Search Result</h4>
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '1rem' }}>
              <div>
                <strong>File Name:</strong><br />
                {searchResult.fileName}
              </div>
              <div>
                <strong>Original File:</strong><br />
                {searchResult.originalFileName}
              </div>
              <div>
                <strong>Text Hash:</strong><br />
                <code onClick={() => copyToClipboard(searchResult.textHash)} style={{ cursor: 'pointer' }}>
                  {shortenHash(searchResult.textHash)}
                </code>
              </div>
              <div>
                <strong>Transaction Hash:</strong><br />
                <code onClick={() => copyToClipboard(searchResult.transactionHash)} style={{ cursor: 'pointer' }}>
                  {shortenHash(searchResult.transactionHash)}
                </code>
              </div>
              <div>
                <strong>Status:</strong><br />
                <span style={{ color: searchResult.status === 'COMPLETED' ? '#51cf66' : '#ff6b6b' }}>
                  {searchResult.status}
                </span>
              </div>
              <div>
                <strong>Created:</strong><br />
                {formatDate(searchResult.createdAt)}
              </div>
            </div>
          </div>
        )}
      </div>

      {/* Refresh Button */}
      <div style={{ marginBottom: '1rem' }}>
        <button className="btn btn-secondary" onClick={loadAllRecords} disabled={loading}>
          {loading ? 'Loading...' : '🔄 Refresh Records'}
        </button>
      </div>

      {error && <div className="error">{error}</div>}

      {loading ? (
        <div className="loading">Loading records...</div>
      ) : records.length === 0 ? (
        <div style={{ textAlign: 'center', padding: '2rem', opacity: 0.7 }}>
          No records found. Start by hiding some text in images!
        </div>
      ) : (
        <>
          <div style={{ marginBottom: '1rem' }}>
            <strong>Total Records: {records.length}</strong>
          </div>
          
          <div style={{ overflowX: 'auto' }}>
            <table className="records-table">
              <thead>
                <tr>
                  <th>File Name</th>
                  <th>Original File</th>
                  <th>Text Hash</th>
                  <th>Transaction Hash</th>
                  <th>Status</th>
                  <th>Created At</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {records.map((record) => (
                  <tr key={record.id}>
                    <td>{record.fileName}</td>
                    <td>{record.originalFileName}</td>
                    <td>
                      <code 
                        onClick={() => copyToClipboard(record.textHash)} 
                        style={{ cursor: 'pointer' }}
                        title="Click to copy"
                      >
                        {shortenHash(record.textHash)}
                      </code>
                    </td>
                    <td>
                      <code 
                        onClick={() => copyToClipboard(record.transactionHash)} 
                        style={{ cursor: 'pointer' }}
                        title="Click to copy"
                      >
                        {shortenHash(record.transactionHash)}
                      </code>
                    </td>
                    <td>
                      <span style={{ 
                        color: record.status === 'COMPLETED' ? '#51cf66' : '#ff6b6b',
                        fontWeight: 'bold'
                      }}>
                        {record.status}
                      </span>
                    </td>
                    <td>{formatDate(record.createdAt)}</td>
                    <td>
                      <button 
                        className="btn btn-secondary" 
                        style={{ fontSize: '0.8rem', padding: '0.3rem 0.6rem' }}
                        onClick={() => {
                          setSearchHash(record.transactionHash);
                          setSearchResult(record);
                        }}
                      >
                        View
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </>
      )}
    </div>
  );
};

export default RecordsComponent;
