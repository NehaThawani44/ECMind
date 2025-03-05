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

        Optional<PdfMetadata> pdfMetadata = pdfRepository.findById(pdfId);

        if (pdfMetadata.isPresent()) {
            return pdfMetadata.get().getData();
        } else {
            throw new RuntimeException("PDF not found with id: " + pdfId);
        }
    }

    public byte[] getPreview(Long pdfId) {
        byte[] pdfData = pdfStorageService.getPdfById(pdfId);
        return pdfRenderingService.renderFirstPageAsJpeg(pdfData);
    }

    public byte[] stampPdf(Long pdfId, StampData stampData) throws IOException {
        byte[] pdfData = pdfStorageService.getPdfById(pdfId);  // Get PDF data

        try (PDDocument document = PDDocument.load(pdfData)) {
            int pageCount = document.getNumberOfPages();
            PDPage lastPage = document.getPage(pageCount - 1); // Get last page

            try (PDPageContentStream contentStream = new PDPageContentStream(document, lastPage, PDPageContentStream.AppendMode.APPEND, true, true)) {
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.newLineAtOffset(50, 30);
                String stampText = "Date: " + stampData.getDate() + " | Name: " + stampData.getName() + " | Comment: " + stampData.getComment();
                contentStream.showText(stampText);
                contentStream.endText();
            }

            String fileName = "stamped_pdf_" + pdfId + "_" + System.currentTimeMillis() + ".pdf";
            String saveDirectory = System.getProperty("user.home") + "/Desktop/";
            String filePath = saveDirectory + fileName;
            document.save(filePath);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.save(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error applying stamp to PDF", e);
        }
    }


    public byte[] downloadPdf(String pdfId) {
        return pdfStorageService.getPdfById(Long.parseLong(pdfId));
    }

    public void deletePdf(Long pdfId) {
        pdfStorageService.deletePdf(pdfId);
    }

}


