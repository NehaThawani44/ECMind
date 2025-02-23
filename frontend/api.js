// src/api.js
const API_BASE = "http://localhost:8080/api/pdfs";

export const uploadPdf = async (file) => {
  const formData = new FormData();
  formData.append("file", file);
  const response = await fetch(`${API_BASE}/upload`, {
    method: "POST",
    body: formData,
  });
  return response.text();
};

export const listPdfs = async () => {
  const response = await fetch(API_BASE);
  return response.json();
};

export const getPreview = async (id) => {
  const response = await fetch(`${API_BASE}/${id}/preview`);
  const blob = await response.blob();
  return URL.createObjectURL(blob);
};

export const stampPdf = async (id, stampData) => {
  const response = await fetch(`${API_BASE}/${id}/stamp`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(stampData),
  });
  const blob = await response.blob();
  return URL.createObjectURL(blob);
};

export const downloadPdf = async (id) => {
  const response = await fetch(`${API_BASE}/${id}/download`);
  return response.blob();
};

export const deletePdf = async (id) => {
  const response = await fetch(`${API_BASE}/${id}`, {
    method: "DELETE",
  });
  return response.text();
};
