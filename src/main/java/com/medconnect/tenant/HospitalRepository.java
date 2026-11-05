package com.medconnect.tenant;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HospitalRepository extends JpaRepository<Hospital, UUID> {
    boolean existsBySlug(String slug);
}