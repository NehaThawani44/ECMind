import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

function PdfManager() {
  const navigate = useNavigate();
  const [pdfData, setPdfData] = useState(null);

  // Use navigate instead of history.push
  const goBack = () => {
    navigate('/');
  };

  useEffect(() => {
    // Your logic to fetch PDF data
  }, []);

  return (
    <div>
      <h1>Pdf Manager</h1>
      <button onClick={goBack}>Go Back</button>
      {/* Render PDF data */}
    </div>
  );
}

export default PdfManager;
