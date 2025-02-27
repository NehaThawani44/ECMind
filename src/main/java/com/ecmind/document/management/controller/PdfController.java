package com.ecmind.document.management.controller;


import com.ecmind.document.management.model.PdfMetadata;
import com.ecmind.document.management.model.StampData;
import com.ecmind.document.management.service.PdfServiceFacade;
import com.ecmind.document.management.service.PdfStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
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



    // Upload a PDF file
    @PostMapping("/upload")
    public ResponseEntity<String> uploadPdf(@RequestParam("file") MultipartFile file) {
        try {
            pdfServiceFacade.uploadPdf(file);
            return ResponseEntity.ok("File uploaded successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error uploading file: " + e.getMessage());
        }
    }

    // List all uploaded PDFs
    @GetMapping
    public List<PdfMetadata> listPdfs() {
        return pdfServiceFacade.listPdfs();
    }

    // Render the first page as a JPEG preview
    @GetMapping("/{id}/preview")
    public ResponseEntity<byte[]> getPreview(@PathVariable("id") Long pdfId) {
        byte[] jpegData = pdfServiceFacade.getPreview(pdfId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE)
                .body(jpegData);
    }

    // Stamp the PDF and return a new JPEG preview with the stamp applied
//    @PostMapping("/{id}/stamp")
//    public ResponseEntity<byte[]> stampPdf(@PathVariable("id") Long pdfId, @RequestBody StampData stampData) {
//        byte[] jpegData = pdfServiceFacade.stampPdf(pdfId, stampData);
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE)
//                .body(jpegData);
//    }

    // Endpoint to stamp a PDF with a watermark
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
    public ResponseEntity<String> deletePdf(@PathVariable("id") Long pdfId) {
        pdfServiceFacade.deletePdf(pdfId);
        return ResponseEntity.ok("PDF deleted successfully.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getPdf(@PathVariable Long id) {
        // Fetch the PDF file by id
        byte[] pdfData = pdfStorageService.getPdfById(id);  // Now this returns a byte[] not PdfMetadata

        // Create the response
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("inline").filename("file.pdf").build());

        return new ResponseEntity<>(pdfData, headers, HttpStatus.OK);
    }
}
