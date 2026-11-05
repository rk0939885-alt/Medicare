package com.medconnect.report;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.medconnect.patient.PatientRepository;
import com.medconnect.tenant.TenantProvider;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/patients/{patientId}/reports")
public class MedicalReportController {
    private final MedicalReportRepository reports;
    private final PatientRepository patients;
    private final TenantProvider tenantProvider;

    public MedicalReportController(MedicalReportRepository reports, PatientRepository patients,
                                   TenantProvider tenantProvider) {
        this.reports = reports;
        this.patients = patients;
        this.tenantProvider = tenantProvider;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReportResponse register(@PathVariable UUID patientId,
                                   @Valid @RequestBody RegisterReportRequest request) {
        UUID tenantId = tenantProvider.requireTenantId();
        ensurePatientInTenant(patientId, tenantId);
        MedicalReport report = reports.save(new MedicalReport(tenantId, patientId,
                request.storageObjectKey(), request.originalFilename(), request.contentType()));
        return ReportResponse.from(report);
    }

    @GetMapping
    public List<ReportResponse> list(@PathVariable UUID patientId) {
        UUID tenantId = tenantProvider.requireTenantId();
        ensurePatientInTenant(patientId, tenantId);
        return reports.findAllByTenantIdAndPatientIdOrderByCreatedAtDesc(tenantId, patientId)
                .stream().map(ReportResponse::from).toList();
    }

    private void ensurePatientInTenant(UUID patientId, UUID tenantId) {
        if (!patients.existsByIdAndTenantId(patientId, tenantId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found");
        }
    }

    public record RegisterReportRequest(
            @NotBlank @Size(max = 512) String storageObjectKey,
            @NotBlank @Size(max = 255) String originalFilename,
            @NotBlank @Size(max = 120) String contentType) { }

    public record ReportResponse(UUID id, UUID patientId, String originalFilename,
                                 String contentType, Instant createdAt) {
        static ReportResponse from(MedicalReport report) {
            return new ReportResponse(report.getId(), report.getPatientId(), report.getOriginalFilename(),
                    report.getContentType(), report.getCreatedAt());
        }
    }
}