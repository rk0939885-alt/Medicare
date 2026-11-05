package com.medconnect.appointment;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.medconnect.patient.PatientRepository;
import com.medconnect.tenant.TenantProvider;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {
    private final AppointmentRepository appointments;
    private final PatientRepository patients;
    private final TenantProvider tenantProvider;

    public AppointmentController(AppointmentRepository appointments, PatientRepository patients,
                                 TenantProvider tenantProvider) {
        this.appointments = appointments;
        this.patients = patients;
        this.tenantProvider = tenantProvider;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentResponse book(@Valid @RequestBody BookAppointmentRequest request) {
        UUID tenantId = tenantProvider.requireTenantId();
        if (!patients.existsByIdAndTenantId(request.patientId(), tenantId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found");
        }
        Appointment appointment = appointments.save(new Appointment(tenantId, request.patientId(),
                request.clinicianId(), request.startsAt(), request.virtualVisit()));
        return AppointmentResponse.from(appointment);
    }

    @GetMapping
    public List<AppointmentResponse> list() {
        return appointments.findAllByTenantIdOrderByStartsAtAsc(tenantProvider.requireTenantId())
                .stream().map(AppointmentResponse::from).toList();
    }

    public record BookAppointmentRequest(
            @NotNull UUID patientId,
            @NotBlank @Size(max = 120) String clinicianId,
            @NotNull OffsetDateTime startsAt,
            boolean virtualVisit) { }

    public record AppointmentResponse(UUID id, UUID patientId, String clinicianId,
                                      OffsetDateTime startsAt, boolean virtualVisit,
                                      AppointmentStatus status, Instant createdAt) {
        static AppointmentResponse from(Appointment appointment) {
            return new AppointmentResponse(appointment.getId(), appointment.getPatientId(),
                    appointment.getClinicianId(), appointment.getStartsAt(), appointment.isVirtualVisit(),
                    appointment.getStatus(), appointment.getCreatedAt());
        }
    }
}