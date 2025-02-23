package com.ecmind.document.management.repository;

import com.ecmind.document.management.model.PdfMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PdfRepository extends JpaRepository<PdfMetadata, Long> {
}
