import React, { useState, useEffect, useRef } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import { PDFDocument, rgb, StandardFonts } from 'pdf-lib';

function PdfManager() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [pdfFile, setPdfFile] = useState(null);
  const [fileName, setFileName] = useState('');
  const [stampText, setStampText] = useState('');
  const [pdfUrl, setPdfUrl] = useState('');
  const [isLoading, setIsLoading] = useState(true);
  const canvasRef = useRef(null);
  
  // Fetch PDF data from backend
  useEffect(() => {
    const fetchPdf = async () => {
      try {
        setIsLoading(true);
        
        // Get PDF metadata
        const metadataResponse = await axios.get(`http://localhost:8080/api/pdfs/${id}`);
        setFileName(metadataResponse.data.fileName);
        
        // Get the actual PDF file
        const pdfResponse = await axios.get(`http://localhost:8080/api/pdfs/${id}/download`, {
          responseType: 'arraybuffer'
        });
        
        const pdfBlob = new Blob([pdfResponse.data], { type: 'application/pdf' });
        setPdfFile(pdfResponse.data);
        setPdfUrl(URL.createObjectURL(pdfBlob));
        setIsLoading(false);
      } catch (error) {
        console.error('Error fetching PDF:', error);
        setIsLoading(false);
      }
    };
    
    fetchPdf();
  }, [id]);

  // Apply stamp to PDF directly in the browser
  const applyStamp = async () => {
    try {
      if (!pdfFile || !stampText) return;
      
      // Load the PDF
      const pdfDoc = await PDFDocument.load(pdfFile);
      const pages = pdfDoc.getPages();
      const lastPage = pages[pages.length - 1];
      
      // Get dimensions
      const { width, height } = lastPage.getSize();
      
      // Embed font
      const helveticaFont = await pdfDoc.embedFont(StandardFonts.Helvetica);
      
      // Add stamp text at the bottom of the page
      lastPage.drawText(stampText, {
        x: 50,
        y: 50, // Position at bottom of page
        size: 12,
        font: helveticaFont,
        color: rgb(0.95, 0.1, 0.1), // Red color for stamp
      });
      
      // Save the modified PDF
      const modifiedPdfBytes = await pdfDoc.save();
      
      // Create a new blob with the modified PDF
      const modifiedPdfBlob = new Blob([modifiedPdfBytes], { type: 'application/pdf' });
      
      // Update the PDF URL to show the stamped version
      URL.revokeObjectURL(pdfUrl); // Clean up old URL
      setPdfUrl(URL.createObjectURL(modifiedPdfBlob));
      setPdfFile(modifiedPdfBytes);
      
      // Optional: If you want to also save to backend
      // saveStampedPdfToBackend(modifiedPdfBytes);
      
    } catch (error) {
      console.error('Error applying stamp:', error);
    }
  };
  
  // Optional: Save stamped PDF to backend
  const saveStampedPdfToBackend = async (pdfBytes) => {
    try {
      const formData = new FormData();
      const blob = new Blob([pdfBytes], { type: 'application/pdf' });
      formData.append('file', blob, fileName);
      
      await axios.post(`http://localhost:8080/api/pdfs/${id}/update`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      });
      
      console.log('Stamped PDF saved to backend');
    } catch (error) {
      console.error('Error saving stamped PDF to backend:', error);
    }
  };
  
  // Download the current PDF (original or stamped)
  const downloadPdf = () => {
    if (!pdfUrl) return;
    
    const link = document.createElement('a');
    link.href = pdfUrl;
    link.download = fileName || `stamped-pdf-${id}.pdf`;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  };

  return (
    <div style={{ padding: '20px', maxWidth: '1200px', margin: '0 auto' }}>
      <h1>{fileName || `PDF ${id}`}</h1>
      
      <div style={{ marginBottom: '20px' }}>
        <div style={{ marginBottom: '10px' }}>
          <label style={{ display: 'block', marginBottom: '5px' }}>Stempeltext:</label>
          <input
            type="text"
            value={stampText}
            onChange={(e) => setStampText(e.target.value)}
            placeholder="Enter stamp text"
            style={{ width: '100%', padding: '8px', fontSize: '16px' }}
          />
        </div>
        
        <div style={{ display: 'flex', gap: '10px' }}>
          <button 
            onClick={applyStamp}
            style={{
              padding: '8px 16px',
              backgroundColor: '#4CAF50',
              color: 'white',
              border: 'none',
              borderRadius: '4px',
              cursor: 'pointer'
            }}
          >
            Aktualisieren
          </button>
          
          <button 
            onClick={downloadPdf}
            style={{
              padding: '8px 16px',
              backgroundColor: '#2196F3',
              color: 'white',
              border: 'none',
              borderRadius: '4px',
              cursor: 'pointer'
            }}
          >
            Herunterladen
          </button>
          
          <button 
            onClick={() => navigate('/')}
            style={{
              padding: '8px 16px',
              backgroundColor: '#f1f1f1',
              border: 'none',
              borderRadius: '4px',
              cursor: 'pointer'
            }}
          >
            Zur Liste
          </button>
        </div>
      </div>
      
      {isLoading ? (
        <div>Loading PDF...</div>
      ) : (
        <div style={{ border: '1px solid #ddd', borderRadius: '4px', height: '600px' }}>
          <iframe
            src={pdfUrl}
            title="PDF Viewer"
            style={{ width: '100%', height: '100%', border: 'none' }}
          />
        </div>
      )}
    </div>
  );
}

export default PdfManager;