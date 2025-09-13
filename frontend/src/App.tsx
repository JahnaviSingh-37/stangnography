import React, { useState } from 'react';
import './App.css';
import HideTextComponent from './components/HideTextComponent';
import ExtractTextComponent from './components/ExtractTextComponent';
import RecordsComponent from './components/RecordsComponent';

const App: React.FC = () => {
  const [activeTab, setActiveTab] = useState<'hide' | 'extract' | 'records'>('hide');

  return (
    <div className="app">
      <header className="app-header">
        <h1>🖼️ PNG Steganography Tool</h1>
        <p>Hide text in images • Verify integrity with blockchain</p>
      </header>

      <nav className="nav-tabs">
        <button 
          className={`tab ${activeTab === 'hide' ? 'active' : ''}`}
          onClick={() => setActiveTab('hide')}
        >
          📝 Hide Text
        </button>
        <button 
          className={`tab ${activeTab === 'extract' ? 'active' : ''}`}
          onClick={() => setActiveTab('extract')}
        >
          🔍 Extract Text
        </button>
        <button 
          className={`tab ${activeTab === 'records' ? 'active' : ''}`}
          onClick={() => setActiveTab('records')}
        >
          📊 Records
        </button>
      </nav>

      <main className="main-content">
        {activeTab === 'hide' && <HideTextComponent />}
        {activeTab === 'extract' && <ExtractTextComponent />}
        {activeTab === 'records' && <RecordsComponent />}
      </main>

      <footer className="app-footer">
        <p>Powered by Spring Boot + React + Polygon Mumbai Testnet</p>
      </footer>
    </div>
  );
};

export default App;
