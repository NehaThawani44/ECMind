package com.ecmind.document.management.service;

import com.ecmind.document.management.model.PdfMetadata;
import com.ecmind.document.management.model.StampData;
import com.ecmind.document.management.repository.PdfRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class PdfServiceFacade {
    @Autowired
    private PdfUploadService pdfUploadService;

    @Autowired
    private PdfStorageService pdfStorageService;

    @Autowired
    private PdfRenderingService pdfRenderingService;

    @Autowired
    private PdfStampService pdfStampService;

    @Autowired
    private PdfRepository pdfRepository;

    public void uploadPdf(MultipartFile file) {
        pdfUploadService.upload(file);
    }

    public List<PdfMetadata> listPdfs() {
        return pdfStorageService.getAllPdfs();
    }

    public byte[] getPdfById(Long pdfId) {
        // Fetch the PDF from the repository or storage
        // If you're using a database, you might have a method like this:
        Optional<PdfMetadata> pdfMetadata = pdfRepository.findById(pdfId);

        // Check if the PDF exists
        if (pdfMetadata.isPresent()) {
            // If found, return the PDF data (stored as a byte array in your repository)
            return pdfMetadata.get().getData(); // Assuming PDF data is stored as byte[]
        } else {
            throw new RuntimeException("PDF not found with id: " + pdfId);
        }
    }


    public byte[] getPreview(Long pdfId) {
        byte[] pdfData = pdfStorageService.getPdfById(pdfId);
        return pdfRenderingService.renderFirstPageAsJpeg(pdfData);
    }

//    public byte[] stampPdf(Long pdfId, StampData stamp) {
//        byte[] pdfData = pdfStorageService.getPdfById(pdfId);
//        byte[] stampedPdf = pdfStampService.applyStamp(pdfData, stamp);
//        pdfStorageService.updatePdf(pdfId, stampedPdf);
//        return pdfRenderingService.renderFirstPageAsJpeg(stampedPdf);
//    }

     //Method to stamp the PDF with text (as a watermark or stamp)
    public byte[] stampPdf(Long pdfId, StampData stampData) throws IOException {
        byte[] pdfData = pdfStorageService.getPdfById(pdfId);  // Get PDF data

        try (PDDocument document = PDDocument.load(pdfData)) {
            PDPage page = document.getPage(0); // Assuming we stamp the first page

            // Prepare the content stream to add text to the PDF
            PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.newLineAtOffset(50, 50);  // Position of the stamp on the page (x, y)
            String stampText = "Date: " + stampData.getDate() + " | Name: " + stampData.getName() + " | Comment: " + stampData.getComment();
            contentStream.showText(stampText);  // Add the stamp text
            contentStream.endText();
            contentStream.close();

            // Save the document to a byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.save(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error applying stamp to PDF", e);
        }
    }

//    @PostMapping("/{id}/stamp")
//    public ResponseEntity<byte[]> stampPdf(@PathVariable("id") Long pdfId, @RequestBody StampData stampData) {
//        try {
//            byte[] stampedPdfImage = pdfServiceFacade.stampPdf(pdfId, stampData);  // Apply stamp and get image
//            return ResponseEntity.ok()
//                    .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE)  // Set content type to image/jpeg
//                    .body(stampedPdfImage);  // Return the stamped PDF as a JPEG image
//        } catch (Exception e) {
//            return ResponseEntity.status(500)
//                    .body(("Error: " + e.getMessage()).getBytes());  // Return error message if something goes wrong
//        }
//    }

    public byte[] downloadPdf(String pdfId) {
        return pdfStorageService.getPdfById(Long.parseLong(pdfId));
    }

    public void deletePdf(Long pdfId) {
        pdfStorageService.deletePdf(pdfId);
    }

}
