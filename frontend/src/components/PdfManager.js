import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';

function PdfManager() {
  const { id } = useParams(); // Extract PDF ID from URL (e.g., "1")
  const navigate = useNavigate();

  // State for stamp fields
  const [stampDate, setStampDate] = useState('');
  const [stampName, setStampName] = useState('');
  const [stampComment, setStampComment] = useState('');

  // Handle the stamp submit which calls the backend endpoint
  const handleStampSubmit = () => {
    axios.post(`http://localhost:8080/api/pdfs/${id}/stamp`, {
      date: stampDate,
      name: stampName,
      comment: stampComment
    }, {
      headers: {
        'Content-Type': 'application/json',
      }
    })
      .then(response => {
        console.log('Stamp applied:', response.data);
        // Optionally navigate back to the list or display a success message
        navigate('/');
      })
      .catch(error => {
        console.error('Error stamping PDF:', error);
      });
  };

  return (
    <div style={{ padding: '1rem' }}>
      <h1>Stamp PDF (ID: {id})</h1>
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
      <button onClick={() => navigate('/')} style={{ marginLeft: '1rem' }}>
        Cancel
      </button>
    </div>
  );
}

export default PdfManager;
