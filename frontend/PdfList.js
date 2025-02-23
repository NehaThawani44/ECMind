// src/components/PdfList.js
import React, { useEffect, useState } from "react";
import { listPdfs, uploadPdf, getPreview, deletePdf } from "../api";

const PdfList = () => {
  const [pdfs, setPdfs] = useState([]);
  const [previewUrl, setPreviewUrl] = useState(null);

  useEffect(() => {
    fetchPdfs();
  }, []);

  const fetchPdfs = async () => {
    const data = await listPdfs();
    setPdfs(data);
  };

  const handleFileChange = async (e) => {
    const file = e.target.files[0];
    if (file) {
      await uploadPdf(file);
      fetchPdfs();
    }
  };

  const handlePreview = async (id) => {
    const url = await getPreview(id);
    setPreviewUrl(url);
  };

  const handleDelete = async (id) => {
    await deletePdf(id);
    fetchPdfs();
  };

  return (
    <div>
      <h2>PDF Files</h2>
      <input type="file" onChange={handleFileChange} accept="application/pdf" />
      <ul>
        {pdfs.map((pdf) => (
          <li key={pdf.id}>
            {pdf.fileName} (Uploaded: {new Date(pdf.uploadDate).toLocaleString()})
            <button onClick={() => handlePreview(pdf.id)}>Preview</button>
            <button onClick={() => handleDelete(pdf.id)}>Delete</button>
          </li>
        ))}
      </ul>
      {previewUrl && (
        <div>
          <h3>Preview</h3>
          <img src={previewUrl} alt="PDF Preview" style={{ maxWidth: "500px" }} />
        </div>
      )}
    </div>
  );
};

export default PdfList;
