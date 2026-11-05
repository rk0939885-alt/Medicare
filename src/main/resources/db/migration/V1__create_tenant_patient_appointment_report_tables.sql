CREATE TABLE hospitals (
    id UUID PRIMARY KEY,
    slug VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(200) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE patients (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL REFERENCES hospitals(id),
    full_name VARCHAR(200) NOT NULL,
    email VARCHAR(320) NOT NULL,
    phone VARCHAR(40),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (tenant_id, id)
);
CREATE INDEX idx_patients_tenant_created ON patients (tenant_id, created_at DESC);

CREATE TABLE appointments (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL REFERENCES hospitals(id),
    patient_id UUID NOT NULL,
    clinician_id VARCHAR(120) NOT NULL,
    starts_at TIMESTAMPTZ NOT NULL,
    virtual_visit BOOLEAN NOT NULL DEFAULT FALSE,
    status VARCHAR(24) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (tenant_id, id),
    CONSTRAINT fk_appointment_patient_tenant
        FOREIGN KEY (tenant_id, patient_id) REFERENCES patients (tenant_id, id)
);
CREATE INDEX idx_appointments_tenant_starts ON appointments (tenant_id, starts_at);

CREATE TABLE medical_reports (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL REFERENCES hospitals(id),
    patient_id UUID NOT NULL,
    storage_object_key VARCHAR(512) NOT NULL,
    original_filename VARCHAR(255) NOT NULL,
    content_type VARCHAR(120) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_report_patient_tenant
        FOREIGN KEY (tenant_id, patient_id) REFERENCES patients (tenant_id, id)
);
CREATE INDEX idx_reports_tenant_patient_created ON medical_reports (tenant_id, patient_id, created_at DESC);