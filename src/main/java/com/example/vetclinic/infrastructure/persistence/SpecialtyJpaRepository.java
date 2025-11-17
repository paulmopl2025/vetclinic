package com.example.vetclinic.infrastructure.persistence;

import com.example.vetclinic.domain.model.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialtyJpaRepository extends JpaRepository<Specialty, Long> {
}
