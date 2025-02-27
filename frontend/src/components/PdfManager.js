import React, { useState, useEffect } from "react";
import {
  listPdfs,
  uploadPdf,
  getPreview,
  stampPdf,
  downloadPdf,
  deletePdf,
} from "../api";

const PdfManager = () => {
  const [pdfs, setPdfs] = useState([]);
  const [selectedPreview, setSelectedPreview] = useState(null);
  const [selectedPdfId, setSelectedPdfId] = useState(null);
  const [stampData, setStampData] = useState({
    date: "",
    name: "",
    comment: "",
  });
  const [uploading, setUploading] = useState(false);
  const [error, setError] = useState("");

  // Load the list on mount
  useEffect(() => {
    fetchPdfs();
  }, []);

  const fetchPdfs = async () => {
    try {
      const data = await listPdfs();
      setPdfs(data);
    } catch (err) {
      setError(err.message);
    }
  };

  const handleFileChange = async (e) => {
    const file = e.target.files[0];
    if (file) {
      setUploading(true);
      try {
        const id = await uploadPdf(file);
        console.log("Uploaded PDF with id:", id);
        await fetchPdfs();
      } catch (err) {
        setError(err.message);
      } finally {
        setUploading(false);
      }
    }
  };

  const handlePreview = async (id) => {
    try {
      const previewUrl = await getPreview(id);
      setSelectedPreview(previewUrl);
      setSelectedPdfId(id);
    } catch (err) {
      setError(err.message);
    }
  };

  const handleStamp = async () => {
    if (!selectedPdfId) return;
    try {
      const stampedPreview = await stampPdf(selectedPdfId, stampData);
      setSelectedPreview(stampedPreview);
      // Optionally, refresh the list if your metadata changes.
    } catch (err) {
      setError(err.message);
    }
  };

  const handleDownload = async (id) => {
    try {
      const blob = await downloadPdf(id);
      const url = URL.createObjectURL(blob);
      const a = document.createElement("a");
      a.href = url;
      a.download = "document.pdf";
      a.click();
    } catch (err) {
      setError(err.message);
    }
  };

  const handleDelete = async (id) => {
    try {
      await deletePdf(id);
      await fetchPdfs();
      if (selectedPdfId === id) {
        setSelectedPreview(null);
        setSelectedPdfId(null);
      }
    } catch (err) {
      setError(err.message);
    }
  };

  const handleStampChange = (e) => {
    setStampData({ ...stampData, [e.target.name]: e.target.value });
  };

  return (
    <div style={{ padding: "1rem" }}>
      <h1>PDF Manager</h1>
      {error && <p style={{ color: "red" }}>{error}</p>}
      <div>
        <input type="file" accept="application/pdf" onChange={handleFileChange} />
        {uploading && <p>Uploading...</p>}
      </div>
      <h2>Uploaded PDFs</h2>
      <ul>
        {pdfs.map((pdf) => (
          <li key={pdf.id}>
            {pdf.fileName} (Uploaded: {new Date(pdf.uploadDate).toLocaleString()})
            <button onClick={() => handlePreview(pdf.id)}>Preview</button>
            <button onClick={() => handleDownload(pdf.id)}>Download</button>
            <button onClick={() => handleDelete(pdf.id)}>Delete</button>
          </li>
        ))}
      </ul>
      {selectedPreview && (
        <div>
          <h2>Preview</h2>
          <img
            src={selectedPreview}
            alt="PDF preview"
            style={{ maxWidth: "100%", border: "1px solid #ccc" }}
          />
          <h3>Stamp PDF</h3>
          <div>
            <label>
              Date:{" "}
              <input
                type="text"
                name="date"
                value={stampData.date}
                onChange={handleStampChange}
                placeholder="YYYY-MM-DD"
              />
            </label>
            <br />
            <label>
              Name:{" "}
              <input
                type="text"
                name="name"
                value={stampData.name}
                onChange={handleStampChange}
              />
            </label>
            <br />
            <label>
              Comment:{" "}
              <input
                type="text"
                name="comment"
                value={stampData.comment}
                onChange={handleStampChange}
              />
            </label>
            <br />
            <button onClick={handleStamp}>Apply Stamp</button>
          </div>
        </div>
      )}
    </div>
  );
};

export default PdfManager;