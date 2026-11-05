package com.medconnect.appointment;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "appointments")
public class Appointment {
    @Id
    private UUID id;
    @Column(name = "tenant_id", nullable = false, updatable = false)
    private UUID tenantId;
    @Column(name = "patient_id", nullable = false, updatable = false)
    private UUID patientId;
    @Column(name = "clinician_id", nullable = false, length = 120)
    private String clinicianId;
    @Column(name = "starts_at", nullable = false)
    private OffsetDateTime startsAt;
    @Column(name = "virtual_visit", nullable = false)
    private boolean virtualVisit;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 24)
    private AppointmentStatus status;
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected Appointment() { }

    public Appointment(UUID tenantId, UUID patientId, String clinicianId,
                       OffsetDateTime startsAt, boolean virtualVisit) {
        this.id = UUID.randomUUID();
        this.tenantId = tenantId;
        this.patientId = patientId;
        this.clinicianId = clinicianId;
        this.startsAt = startsAt;
        this.virtualVisit = virtualVisit;
        this.status = AppointmentStatus.BOOKED;
        this.createdAt = Instant.now();
    }

    public UUID getId() { return id; }
    public UUID getTenantId() { return tenantId; }
    public UUID getPatientId() { return patientId; }
    public String getClinicianId() { return clinicianId; }
    public OffsetDateTime getStartsAt() { return startsAt; }
    public boolean isVirtualVisit() { return virtualVisit; }
    public AppointmentStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
}