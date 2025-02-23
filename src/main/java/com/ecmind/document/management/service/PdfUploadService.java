package com.ecmind.document.management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PdfUploadService {

    @Autowired
    private PdfStorageService pdfStorageService;

    public void upload(MultipartFile file) {
        pdfStorageService.store(file);
    }
}