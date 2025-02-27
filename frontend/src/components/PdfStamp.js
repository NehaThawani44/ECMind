import React, { useState } from "react";
import axios from "axios";

function PdfStamp({ pdfId }) {
  const [date, setDate] = useState("");
  const [name, setName] = useState("");
  const [comment, setComment] = useState("");

  const handleStamp = () => {
    const stampData = { date, name, comment };

    axios.post(`http://localhost:8080/api/pdfs/${pdfId}/stamp`, stampData)
      .then(response => {
        alert("PDF stamped successfully!");
      })
      .catch(error => {
        alert("Failed to stamp PDF!");
      });
  };

  return (
    <div>
      <input
        type="text"
        placeholder="Date"
        value={date}
        onChange={(e) => setDate(e.target.value)}
      />
      <input
        type="text"
        placeholder="Name"
        value={name}
        onChange={(e) => setName(e.target.value)}
      />
      <input
        type="text"
        placeholder="Comment"
        value={comment}
        onChange={(e) => setComment(e.target.value)}
      />
      <button onClick={handleStamp}>Stamp PDF</button>
    </div>
  );
}

export default PdfStamp;