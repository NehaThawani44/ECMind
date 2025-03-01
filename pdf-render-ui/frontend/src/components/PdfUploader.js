import React, { useState } from 'react';
import axios from 'axios';
import { useHistory } from 'react-router-dom';

function PdfUploader() {
  const [file, setFile] = useState(null);
  const history = useHistory();

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
  };

  const handleUpload = () => {
    const formData = new FormData();
    formData.append("file", file);

    axios.post('http://localhost:8080/api/pdfs/upload', formData)
      .then(response => {
        console.log("File uploaded successfully");
        history.push('/');  // Redirect to file list after upload
      })
      .catch(error => console.log(error));
  };

  return (
    <div>
      <h2>Upload PDF</h2>
      <input type="file" onChange={handleFileChange} />
      <button onClick={handleUpload}>Upload</button>
    </div>
  );
}

export default PdfUploader;
