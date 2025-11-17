package com.example.vetclinic.infrastructure.persistence;

import com.example.vetclinic.domain.model.Vet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VetJpaRepository extends JpaRepository<Vet, Long> {
}
