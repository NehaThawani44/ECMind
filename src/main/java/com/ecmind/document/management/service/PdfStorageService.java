package com.ecmind.document.management.service;

import com.ecmind.document.management.model.PdfMetadata;
import com.ecmind.document.management.repository.PdfRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PdfStorageService {

    @Autowired
    private PdfRepository pdfRepository;


    // In‑memory storage for demo purposes
    private Map<String, byte[]> pdfStorage = new ConcurrentHashMap<>();
    private Map<String, PdfMetadata> metadataStorage = new ConcurrentHashMap<>();

    // Store the PDF file and generate an ID
    public PdfMetadata store(MultipartFile file) {
        try {
            PdfMetadata entity = new PdfMetadata();

            entity.setFileName(file.getOriginalFilename());
            entity.setUploadDate(new Date());
            entity.setData(file.getBytes());
            return pdfRepository.save(entity);
        } catch (IOException e) {
            throw new RuntimeException("Error storing file", e);
        }
    }

    public List<PdfMetadata> getAllPdfs() {
        return new ArrayList<>(metadataStorage.values());
    }

    public byte[] getPdfById(String pdfId) {
        Long id = Long.parseLong(pdfId);
        PdfMetadata entity = pdfRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PDF not found with id: " + pdfId));
        return entity.getData();
    }

    public void updatePdf(String pdfId, byte[] newData) {
        if (!pdfStorage.containsKey(pdfId)) {
            throw new RuntimeException("PDF not found with id: " + pdfId);
        }
        pdfStorage.put(pdfId, newData);
    }

    public void deletePdf(String pdfId) {
        pdfStorage.remove(pdfId);
        metadataStorage.remove(pdfId);
    }
}
