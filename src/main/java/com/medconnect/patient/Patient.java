package com.medconnect.patient;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "patients")
public class Patient {
    @Id
    private UUID id;
    @Column(name = "tenant_id", nullable = false, updatable = false)
    private UUID tenantId;
    @Column(nullable = false, length = 200)
    private String fullName;
    @Column(nullable = false, length = 320)
    private String email;
    @Column(length = 40)
    private String phone;
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    protected Patient() { }

    public Patient(UUID tenantId, String fullName, String email, String phone) {
        this.id = UUID.randomUUID();
        this.tenantId = tenantId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.createdAt = Instant.now();
    }

    public UUID getId() { return id; }
    public UUID getTenantId() { return tenantId; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public Instant getCreatedAt() { return createdAt; }
}