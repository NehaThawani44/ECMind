// src/api.js
import axios from 'axios';

const API_BASE = "http://localhost:8080/api/pdfs";

export const listPdfs = async () => {
  const response = await fetch(API_BASE);
  if (!response.ok) throw new Error("Error listing PDFs");
  return response.json();
};

export const uploadPdf = async (file) => {
  const formData = new FormData();
  formData.append("file", file);
  const response = await fetch(`${API_BASE}/upload`, {
    method: "POST",
    body: formData,
  });
  if (!response.ok) throw new Error("Error uploading PDF");
  return response.text(); // Expecting an ID in response
};

export const getPreview = async (id) => {
  const response = await fetch(`${API_BASE}/${id}/preview`);
  if (!response.ok) throw new Error("Error getting preview");
  const blob = await response.blob();
  return URL.createObjectURL(blob);
};

export const stampPdf = async (id, stampData) => {
  const response = await fetch(`${API_BASE}/${id}/stamp`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(stampData),
  });
  if (!response.ok) throw new Error("Error stamping PDF");
  const blob = await response.blob();
  return URL.createObjectURL(blob);
};

export const downloadPdf = async (id) => {
  const response = await fetch(`${API_BASE}/${id}/download`);
  if (!response.ok) throw new Error("Error downloading PDF");
  return response.blob();
};

export const deletePdf = async (id) => {
  const response = await fetch(`${API_BASE}/${id}`, {
    method: "DELETE",
  });
  if (!response.ok) throw new Error("Error deleting PDF");
  return response.text();
};



const api = axios.create({
  baseURL: 'http://localhost:8080/api', // Change the base URL as per your backend
});

export default api;