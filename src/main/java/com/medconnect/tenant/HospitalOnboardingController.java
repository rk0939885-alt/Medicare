package com.medconnect.tenant;

import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/platform/hospitals")
public class HospitalOnboardingController {
    private final HospitalRepository hospitals;

    public HospitalOnboardingController(HospitalRepository hospitals) {
        this.hospitals = hospitals;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HospitalResponse onboard(@Valid @RequestBody OnboardHospitalRequest request) {
        if (hospitals.existsBySlug(request.slug())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Hospital slug is already in use");
        }
        try {
            Hospital hospital = hospitals.saveAndFlush(new Hospital(request.slug(), request.name()));
            return new HospitalResponse(hospital.getId(), hospital.getSlug(), hospital.getName(), hospital.isActive());
        } catch (DataIntegrityViolationException exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Hospital slug is already in use");
        }
    }

    public record OnboardHospitalRequest(
            @NotBlank @Size(max = 100) @Pattern(regexp = "[a-z0-9]+(?:-[a-z0-9]+)*") String slug,
            @NotBlank @Size(max = 200) String name) { }

    public record HospitalResponse(UUID tenantId, String slug, String name, boolean active) { }
}