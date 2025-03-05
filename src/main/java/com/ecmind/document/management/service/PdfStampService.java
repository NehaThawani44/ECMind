package com.ecmind.document.management.service;


import com.ecmind.document.management.model.StampData;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class PdfStampService {
    private static final Logger logger = LoggerFactory.getLogger(PdfStampService.class);


    public byte[] applyStamp(byte[] pdfData, StampData stamp) {
        try (PDDocument document = PDDocument.load(new ByteArrayInputStream(pdfData))) {

            logger.info("PDF loaded successfully.");

            PDPage page = document.getPage(0);

            String stampText = "Date: " + stamp.getDate() + " | Name: " + stamp.getName() + " | Comment: " + stamp.getComment();
            logger.info("Applying stamp: {}", stampText);
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
                contentStream.beginText();
                contentStream.setNonStrokingColor(Color.RED);
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.newLineAtOffset(50, 50);
                contentStream.showText(stampText);
                contentStream.endText();
            }

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            document.save(output);
            return output.toByteArray();
        } catch (IOException e) {
            logger.error("Error stamping PDF", e);
            throw new RuntimeException("Error stamping PDF", e);
        }
    }
}