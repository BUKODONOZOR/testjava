package com.prueba.main.controller;

import com.prueba.main.service.AuditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/audit")
public class AuditController {

    @Autowired
    private AuditService auditService;

    @Operation(summary = "Generate an audit report",
            description = "Generates a PDF report of the audit records within the specified date range. Only accessible to users with ADMIN role.",
            tags = { "Audit" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Audit report generated successfully", content = {
                    @Content(mediaType = "application/pdf") }),
            @ApiResponse(responseCode = "400", description = "Invalid input date format", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access forbidden", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error while generating the report", content = @Content)
    })
    @RolesAllowed("ADMIN")
    @GetMapping("/report")
    public ResponseEntity<byte[]> getAuditReport(
            @Parameter(description = "Start date for the audit report in the format yyyy-MM-ddTHH:mm:ss")
            @RequestParam("startDate") String startDate,
            @Parameter(description = "End date for the audit report in the format yyyy-MM-ddTHH:mm:ss")
            @RequestParam("endDate") String endDate) {

        LocalDateTime start = LocalDateTime.parse(startDate);
        LocalDateTime end = LocalDateTime.parse(endDate);

        ByteArrayInputStream bis = auditService.generateAuditReport(start, end);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=audit_report.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(bis.readAllBytes());
    }
}
