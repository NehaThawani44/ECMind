import React, { useEffect, useState, useRef } from 'react';
import axios from 'axios';

const PdfList = () => {
  const [pdfs, setPdfs] = useState([]);
  const fileInputRef = useRef(null);

  useEffect(() => {
    fetchPdfs();
  }, []);

  const fetchPdfs = () => {
    axios.get('http://localhost:8080/api/pdfs')
      .then(response => {
        console.log('PDF data from backend:', response.data);
        setPdfs(response.data);
      })
      .catch(error => {
        console.error('Error fetching PDFs:', error);
      });
  };

  const openFileDialog = () => {
    fileInputRef.current.click();
  };

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    if (!file) return;

    console.log("File selected:", file);

    const formData = new FormData();
    formData.append("file", file);

    axios.post('http://localhost:8080/api/pdfs/upload', formData)
      .then(response => {
        console.log("File uploaded successfully:", response.data);
        // Re-fetch the PDFs
        fetchPdfs();
      })
      .catch(error => {
        console.error("Error uploading file:", error);
      });
  };

  return (
    <div>
      <h1>Uploaded PDFs</h1>
      <button onClick={openFileDialog}>Add New File</button>
      <input
        type="file"
        ref={fileInputRef}
        style={{ display: 'none' }}
        onChange={handleFileChange}
      />

     <ul>
       {pdfs.length === 0 ? (
         <li>No PDFs uploaded yet.</li>
       ) : (
         pdfs.map((pdf) => ( 
          <li key={pdf.id}>{pdf.fileName}</li>
         ))
      
       )}
     </ul>
    </div>
  );
};

export default PdfList;