package com.medconnect.report;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "medical_reports")
public class MedicalReport {
    @Id
    private UUID id;
    @Column(name = "tenant_id", nullable = false, updatable = false)
    private UUID tenantId;
    @Column(name = "patient_id", nullable = false, updatable = false)
    private UUID patientId;
    @Column(name = "storage_object_key", nullable = false, length = 512, updatable = false)
    private String storageObjectKey;
    @Column(name = "original_filename", nullable = false, length = 255, updatable = false)
    private String originalFilename;
    @Column(name = "content_type", nullable = false, length = 120, updatable = false)
    private String contentType;
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected MedicalReport() { }

    public MedicalReport(UUID tenantId, UUID patientId, String storageObjectKey,
                         String originalFilename, String contentType) {
        this.id = UUID.randomUUID();
        this.tenantId = tenantId;
        this.patientId = patientId;
        this.storageObjectKey = storageObjectKey;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
        this.createdAt = Instant.now();
    }

    public UUID getId() { return id; }
    public String getStorageObjectKey() { return storageObjectKey; }
    public UUID getPatientId() { return patientId; }
    public String getOriginalFilename() { return originalFilename; }
    public String getContentType() { return contentType; }
    public Instant getCreatedAt() { return createdAt; }
}