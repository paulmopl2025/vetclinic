package com.example.vetclinic.infrastructure.persistence;

import com.example.vetclinic.domain.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerJpaRepository extends JpaRepository<Owner, Long> {
}
