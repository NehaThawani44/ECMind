package com.ecmind.document.management.service;

import org.springframework.stereotype.Service;

@Service
public class PdfRenderingService {

    public byte[] renderFirstPageAsJpeg(byte[] pdfData) {
        return pdfData;
    }
}
