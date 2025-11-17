package com.example.vetclinic.infrastructure.persistence;

import com.example.vetclinic.domain.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitJpaRepository extends JpaRepository<Visit, Long> {
}
