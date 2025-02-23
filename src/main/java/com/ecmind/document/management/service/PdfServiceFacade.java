package com.ecmind.document.management.service;

import com.ecmind.document.management.model.PdfMetadata;
import com.ecmind.document.management.model.StampData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
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

    public void uploadPdf(MultipartFile file) {
        pdfUploadService.upload(file);
    }

    public List<PdfMetadata> listPdfs() {
        return pdfStorageService.getAllPdfs();
    }

    public byte[] getPreview(String pdfId) {
        byte[] pdfData = pdfStorageService.getPdfById(pdfId);
        return pdfRenderingService.renderFirstPageAsJpeg(pdfData);
    }

    public byte[] stampPdf(String pdfId, StampData stamp) {
        byte[] pdfData = pdfStorageService.getPdfById(pdfId);
        byte[] stampedPdf = pdfStampService.applyStamp(pdfData, stamp);
        pdfStorageService.updatePdf(pdfId, stampedPdf);
        return pdfRenderingService.renderFirstPageAsJpeg(stampedPdf);
    }

    public byte[] downloadPdf(String pdfId) {
        return pdfStorageService.getPdfById(pdfId);
    }

    public void deletePdf(String pdfId) {
        pdfStorageService.deletePdf(pdfId);
    }
}
