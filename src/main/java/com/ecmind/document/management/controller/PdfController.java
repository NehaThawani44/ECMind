package com.ecmind.document.management.controller;


import com.ecmind.document.management.model.PdfMetadata;
import com.ecmind.document.management.model.StampData;
import com.ecmind.document.management.service.PdfServiceFacade;
import com.ecmind.document.management.service.PdfStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pdfs")
public class PdfController {

    @Autowired
    private PdfServiceFacade pdfServiceFacade;

    @Autowired
    private PdfStorageService pdfStorageService;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadPdf(@RequestParam("file") MultipartFile file) {
        try {
            // Assume your service returns the saved file name or a database ID
            String savedFileName = pdfServiceFacade.uploadPdf(file);

            // Build a JSON response
            Map<String, Object> response = new HashMap<>();
            response.put("filename", savedFileName);
            response.put("message", "File uploaded successfully.");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Error uploading file: " + e.getMessage()));
        }
    }

    @GetMapping
    public List<PdfMetadata> listPdfs() {
        return pdfServiceFacade.listPdfs();
    }

    @GetMapping("/{id}/preview")
    public ResponseEntity<byte[]> getPreview(@PathVariable("id") Long pdfId) {
        byte[] jpegData = pdfServiceFacade.getPreview(pdfId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE)
                .body(jpegData);
    }


    @PostMapping("/{id}/stamp")
    public ResponseEntity<byte[]> stampPdf(@PathVariable("id") Long pdfId, @RequestBody StampData stampData) {
        try {
            byte[] stampedPdf = pdfServiceFacade.stampPdf(pdfId, stampData); // Apply stamp
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                    .body(stampedPdf);  // Return the stamped PDF as response
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(("Error: " + e.getMessage()).getBytes());  // Return error if anything goes wrong
        }
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable("id") String pdfId) {
        byte[] pdfData = pdfServiceFacade.downloadPdf(pdfId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + pdfId + ".pdf\"")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                .body(pdfData);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePdf(@PathVariable("id") Long pdfId) {
        pdfServiceFacade.deletePdf(pdfId);
        return ResponseEntity.ok("PDF deleted successfully.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getPdf(@PathVariable Long id) {
        byte[] pdfData = pdfStorageService.getPdfById(id);  // Now this returns a byte[] not PdfMetadata

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("inline").filename("file.pdf").build());

        return new ResponseEntity<>(pdfData, headers, HttpStatus.OK);
    }
}
