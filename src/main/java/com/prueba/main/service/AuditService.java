package com.prueba.main.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.prueba.main.entity.Audit;
import com.prueba.main.repository.AuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditService {

    @Autowired
    private AuditRepository auditRepository;

    public ByteArrayInputStream generateAuditReport(LocalDateTime startDate, LocalDateTime endDate) {
        List<Audit> audits = auditRepository.findByUpdateAtBetween(startDate, endDate);
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            PdfPTable table = new PdfPTable(3); // Puedes ajustar el número de columnas
            table.addCell("ID");
            table.addCell("Created At");
            table.addCell("Updated At");

            for (Audit audit : audits) {
                table.addCell(String.valueOf(audit.getId()));
                table.addCell(audit.getCreatedAd().toString());
                table.addCell(audit.getUpdateAt().toString());
            }

            document.add(table);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
