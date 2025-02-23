package com.ecmind.document.management.service;

import com.ecmind.document.management.model.StampData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PdfStampServiceTest {


        private final PdfStampService pdfStampService = new PdfStampService();

        @Test
        public void testApplyStamp() {
            byte[] dummyPdf = "Dummy PDF".getBytes();
            StampData stamp = new StampData();
            stamp.setDate("2025-02-22");
            stamp.setName("John Doe");
            stamp.setComment("Approved");

            byte[] stampedPdf = pdfStampService.applyStamp(dummyPdf, stamp);
            String result = new String(stampedPdf);

            assertTrue(result.contains("Stamp - Date: 2025-02-22"));
            assertTrue(result.contains("Name: John Doe"));
            assertTrue(result.contains("Comment: Approved"));
        }
    }
