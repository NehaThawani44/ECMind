package com.ecmind.document.management.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "pdf_metadata")
public class PdfMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String fileName;

    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadDate;

    /**
     * Optional: Store the PDF file data in the database as a BLOB.
     * If files are large, consider storing only a file path or reference instead.
     */
    @Lob
    private byte[] data;

    public PdfMetadata() {
    }

    public PdfMetadata(String fileName, Date uploadDate, byte[] data) {
        this.fileName = fileName;
        this.uploadDate = uploadDate;
        this.data = data;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getUploadDate() {
        return uploadDate;
    }
    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public byte[] getData() {
        return data;
    }
    public void setData(byte[] data) {
        this.data = data;
    }
}
