package com.ecmind.document.management.service;


import com.ecmind.document.management.model.StampData;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
    public class PdfStampService {
    private static final Logger logger = LoggerFactory.getLogger(PdfStampService.class);

//        // Dummy implementation: Replace with logic using PDFBox or iText to apply the stamp
//        public byte[] applyStamp(byte[] pdfData, StampData stamp) {
//            // For demonstration, simulate stamping by appending stamp information to the byte array.
//            String stampText = "\nStamp - Date: " + stamp.getDate() +
//                    ", Name: " + stamp.getName() +
//                    ", Comment: " + stamp.getComment();
//            byte[] stampBytes = stampText.getBytes();
//            byte[] stampedData = new byte[pdfData.length + stampBytes.length];
//            System.arraycopy(pdfData, 0, stampedData, 0, pdfData.length);
//            System.arraycopy(stampBytes, 0, stampedData, pdfData.length, stampBytes.length);
//            return stampedData;
//        }
//}

    public byte[] applyStamp(byte[] pdfData, StampData stamp) {
        try (PDDocument document = PDDocument.load(new ByteArrayInputStream(pdfData))) {
            // Get the first page to stamp
            logger.info("PDF loaded successfully.");

            PDPage page = document.getPage(0);

            // Construct stamp text
            String stampText = "Date: " + stamp.getDate() + " | Name: " + stamp.getName() + " | Comment: " + stamp.getComment();
            logger.info("Applying stamp: {}", stampText);
            // Append content to the page
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
                contentStream.beginText();
                // Set the font and size
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                // Set the fixed position (x=50, y=50; adjust as needed)
                contentStream.newLineAtOffset(50, 50);

                contentStream.showText(stampText);
                contentStream.endText();
            }

            // Save the updated PDF to a byte array
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            document.save(output);
            return output.toByteArray();
        } catch (IOException e) {
            logger.error("Error stamping PDF", e);

            throw new RuntimeException("Error stamping PDF", e);
        }
    }
}