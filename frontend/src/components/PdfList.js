import React, { useEffect, useState } from "react";
import axios from "axios";

function PdfList() {
  const [pdfs, setPdfs] = useState([]);

  useEffect(() => {
    axios.get("http://localhost:8080/api/pdfs")
      .then((response) => {
        setPdfs(response.data);
      });
  }, []);

  return (
    <div>
      <h2>Uploaded PDFs</h2>
      <ul>
        {pdfs.map((pdf, index) => (
          <li key={index}>
            {pdf.fileName} - {pdf.uploadDate}
          </li>
        ))}
      </ul>
    </div>
  );
}

export default PdfList;
