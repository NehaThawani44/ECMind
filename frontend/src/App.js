import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import PdfList from './components/PdfList'; // Example component
import PdfManager from './components/PdfManager'; // Example component

function App() {
  return (
    <Router>
      <div className="App">
        {/* Use Routes instead of Switch */}
        <Routes>
          <Route path="/" element={<PdfList />} />
          <Route path="/pdfs/:id" element={<PdfManager />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;