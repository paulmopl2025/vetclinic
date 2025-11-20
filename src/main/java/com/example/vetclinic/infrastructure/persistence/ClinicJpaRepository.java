package com.example.vetclinic.infrastructure.persistence;

import com.example.vetclinic.domain.model.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClinicJpaRepository extends JpaRepository<Clinic, Long> {
    Optional<Clinic> findFirstByActiveTrue();
}
