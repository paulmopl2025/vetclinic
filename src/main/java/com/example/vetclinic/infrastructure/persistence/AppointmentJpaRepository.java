package com.example.vetclinic.infrastructure.persistence;

import com.example.vetclinic.domain.model.Appointment;
import com.example.vetclinic.domain.model.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentJpaRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPetId(Long petId);

    List<Appointment> findByVetId(Long vetId);

    long countByStatus(AppointmentStatus status);

    List<Appointment> findByStatus(AppointmentStatus status);
}
