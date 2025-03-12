import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import PdfList from './components/PdfList';
import PdfManager from './components/PdfManager';
import './App.css';

function App() {
  return (
    <Router>
      <div className="App">
        <header className="App-header" style={{ backgroundColor: '#f8f9fa', padding: '10px 0', marginBottom: '20px' }}>
          <h1 style={{ margin: 0, color: '#333', textAlign: 'center' }}>PDF Stamper</h1>
        </header>
        <Routes>
          <Route path="/" element={<PdfList />} />
          <Route path="/pdfs/:id" element={<PdfManager />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;