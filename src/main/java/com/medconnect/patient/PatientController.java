package com.medconnect.patient;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.medconnect.tenant.TenantProvider;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/patients")
public class PatientController {
    private final PatientRepository patients;
    private final TenantProvider tenantProvider;

    public PatientController(PatientRepository patients, TenantProvider tenantProvider) {
        this.patients = patients;
        this.tenantProvider = tenantProvider;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PatientResponse create(@Valid @RequestBody CreatePatientRequest request) {
        Patient patient = patients.save(new Patient(tenantProvider.requireTenantId(), request.fullName(),
                request.email(), request.phone()));
        return PatientResponse.from(patient);
    }

    @GetMapping
    public List<PatientResponse> list() {
        return patients.findAllByTenantIdOrderByCreatedAtDesc(tenantProvider.requireTenantId())
                .stream().map(PatientResponse::from).toList();
    }

    public record CreatePatientRequest(
            @NotBlank @Size(max = 200) String fullName,
            @NotBlank @Email @Size(max = 320) String email,
            @Size(max = 40) String phone) { }

    public record PatientResponse(UUID id, String fullName, String email, String phone, Instant createdAt) {
        static PatientResponse from(Patient patient) {
            return new PatientResponse(patient.getId(), patient.getFullName(), patient.getEmail(),
                    patient.getPhone(), patient.getCreatedAt());
        }
    }
}