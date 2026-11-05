package com.medconnect.report;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalReportRepository extends JpaRepository<MedicalReport, UUID> {
    List<MedicalReport> findAllByTenantIdAndPatientIdOrderByCreatedAtDesc(UUID tenantId, UUID patientId);
}