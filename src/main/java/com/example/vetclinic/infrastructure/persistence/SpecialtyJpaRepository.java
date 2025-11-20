package com.example.vetclinic.infrastructure.persistence;

import com.example.vetclinic.domain.model.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpecialtyJpaRepository extends JpaRepository<Specialty, Long> {
    Optional<Specialty> findByName(String name);

    boolean existsByName(String name);
}
