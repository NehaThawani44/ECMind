package com.ecmind.document.management.controller;


import com.ecmind.document.management.model.PdfMetadata;
import com.ecmind.document.management.model.StampData;
import com.ecmind.document.management.service.PdfServiceFacade;
import com.ecmind.document.management.service.PdfStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/pdfs")
public class PdfController {

    @Autowired
    private PdfServiceFacade pdfServiceFacade;

    @Autowired
    private PdfStorageService pdfStorageService;


    @PostMapping("/upload")
    public ResponseEntity<String> uploadPdf(@RequestParam("file") MultipartFile file) {
        try {
            pdfServiceFacade.uploadPdf(file);
            return ResponseEntity.ok("File uploaded successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error uploading file: " + e.getMessage());
        }
    }

    @GetMapping
    public List<PdfMetadata> listPdfs() {
        return pdfServiceFacade.listPdfs();
    }

    // Render the first page as a JPEG preview
    @GetMapping("/{id}/preview")
    public ResponseEntity<byte[]> getPreview(@PathVariable("id") String pdfId) {
        byte[] jpegData = pdfServiceFacade.getPreview(pdfId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE)
                .body(jpegData);
    }

    // Stamp the PDF and return a new JPEG preview with the stamp applied
    @PostMapping("/{id}/stamp")
    public ResponseEntity<byte[]> stampPdf(@PathVariable("id") String pdfId, @RequestBody StampData stampData) {
        byte[] jpegData = pdfServiceFacade.stampPdf(pdfId, stampData);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE)
                .body(jpegData);
    }

    // Download the PDF file
    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable("id") String pdfId) {
        byte[] pdfData = pdfServiceFacade.downloadPdf(pdfId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + pdfId + ".pdf\"")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                .body(pdfData);
    }

    // Delete the PDF file
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePdf(@PathVariable("id") String pdfId) {
        pdfServiceFacade.deletePdf(pdfId);
        return ResponseEntity.ok("PDF deleted successfully.");
    }
}
