import React, { useEffect, useState } from 'react';
import axios from 'axios';

const PdfList = () => {
  const [pdfs, setPdfs] = useState([]);

  useEffect(() => {
    // Fetch list of PDFs from the backend API
    axios.get('http://localhost:8080/api/pdfs')
      .then(response => {
        setPdfs(response.data); // Set the PDFs to state
      })
      .catch(error => {
        console.error('Error fetching PDFs:', error);
      });
  }, []);

  return (
    <div>
      <h1>Uploaded PDFs</h1>
      <button onClick={() => console.log("Open file upload dialog")}>Add New File</button>
      <ul>
        {pdfs.length === 0 ? (
          <li>No PDFs uploaded yet.</li>
        ) : (
          pdfs.map((pdf) => (
            <li key={pdf.id}>
              {pdf.name} {/* Assuming pdf has 'id' and 'name' properties */}
            </li>
          ))
        )}
      </ul>
    </div>
  );
};

export default PdfList;