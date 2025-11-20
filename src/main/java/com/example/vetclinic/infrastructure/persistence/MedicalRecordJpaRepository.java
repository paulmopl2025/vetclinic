package com.example.vetclinic.infrastructure.persistence;

import com.example.vetclinic.domain.model.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalRecordJpaRepository extends JpaRepository<MedicalRecord, Long> {
    List<MedicalRecord> findByPetId(Long petId);

    List<MedicalRecord> findByVetId(Long vetId);

    List<MedicalRecord> findByPetIdOrderByRecordDateDesc(Long petId);
}
