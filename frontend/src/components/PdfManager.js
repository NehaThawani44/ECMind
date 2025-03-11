import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function PdfManager() {
  const navigate = useNavigate();
  const [pdfs, setPdfs] = useState([]);
  const [selectedPdf, setSelectedPdf] = useState(null);
  const [stampDate, setStampDate] = useState('');
  const [stampName, setStampName] = useState('');
  const [stampComment, setStampComment] = useState('');

  // Navigate back to the home page
  const goBack = () => {
    navigate('/');
  };

  // Fetch PDF list from the backend on component mount
  useEffect(() => {
    axios.get('http://localhost:8080/api/pdfs')
      .then(response => {
        setPdfs(response.data);
      })
      .catch(error => {
        console.error('Error fetching PDFs:', error);
      });
  }, []);

  // Handle selecting a PDF for stamping
  const handleSelectPdf = (pdf) => {
    setSelectedPdf(pdf);
    // Reset the stamp fields
    setStampDate('');
    setStampName('');
    setStampComment('');
  };

  // Handle the stamp submit which calls the backend endpoint
  const handleStampSubmit = () => {
    if (!selectedPdf) return;

    // Make the POST call to the endpoint /api/pdfs/{id}/stamp
    axios.post(`http://localhost:8080/api/pdfs/${selectedPdf.id}/stamp`, {
      date: stampDate,
      name: stampName,
      comment: stampComment,
    }, {
      headers: {
        'Content-Type': 'application/json',
      }
    })
      .then(response => {
        console.log('Stamp applied:', response.data);
        // Optionally, refresh the PDF list or update the UI as needed.
        setSelectedPdf(null);
        setStampDate('');
        setStampName('');
        setStampComment('');
      })
      .catch(error => {
        console.error('Error stamping PDF:', error);
      });
  };

  return (
    <div style={{ padding: '1rem' }}>
      <h1>PDF Manager</h1>
      <button onClick={goBack}>Go Back</button>

      <h2>Uploaded PDFs</h2>
      <ul>
        {pdfs.length === 0 ? (
          <li>No PDFs uploaded yet.</li>
        ) : (
          pdfs.map(pdf => (
            <li key={pdf.id} style={{ marginBottom: '0.5rem' }}>
              {pdf.name}
              <button
                style={{ marginLeft: '1rem' }}
                onClick={() => handleSelectPdf(pdf)}
              >
                Stamp PDF
              </button>
            </li>
          ))
        )}
      </ul>

      {selectedPdf && (
        <div style={{ marginTop: '2rem', padding: '1rem', border: '1px solid #ccc' }}>
          <h2>Stamp PDF: {selectedPdf.name}</h2>
          <div style={{ marginBottom: '1rem' }}>
            <label>
              Date:&nbsp;
              <input
                type="date"
                value={stampDate}
                onChange={(e) => setStampDate(e.target.value)}
              />
            </label>
          </div>
          <div style={{ marginBottom: '1rem' }}>
            <label>
              Name:&nbsp;
              <input
                type="text"
                value={stampName}
                onChange={(e) => setStampName(e.target.value)}
                placeholder="John Doe"
              />
            </label>
          </div>
          <div style={{ marginBottom: '1rem' }}>
            <label>
              Comment:&nbsp;
              <input
                type="text"
                value={stampComment}
                onChange={(e) => setStampComment(e.target.value)}
                placeholder="Approved"
              />
            </label>
          </div>
          <button onClick={handleStampSubmit}>Apply Stamp</button>
          <button
            onClick={() => setSelectedPdf(null)}
            style={{ marginLeft: '0.5rem' }}
          >
            Cancel
          </button>
        </div>
      )}
    </div>
  );
}

export default PdfManager;
