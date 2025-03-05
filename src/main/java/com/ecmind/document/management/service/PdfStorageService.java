package com.ecmind.document.management.service;

import com.ecmind.document.management.exception.PdfNotFoundException;
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

        return pdfRepository.findAll();
    }


    public byte[] getPdfById(Long pdfId) {
        try {

            PdfMetadata entity = pdfRepository.findById(pdfId)
                    .orElseThrow(() -> new PdfNotFoundException("PDF not found with id: " + pdfId));
            return entity.getData();
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid ID format: " + pdfId, e);  // Add more context
        }
    }

    public void updatePdf(Long pdfId, byte[] newData) {
        if (!pdfStorage.containsKey(pdfId)) {
            throw new RuntimeException("PDF not found with id: " + pdfId);
        }
        pdfStorage.put(pdfId.toString(), newData);
    }

    public void deletePdf(Long pdfId) {
        pdfStorage.remove(pdfId);
        metadataStorage.remove(pdfId);
    }
}
