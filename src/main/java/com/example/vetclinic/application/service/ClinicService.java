package com.example.vetclinic.application.service;

import com.example.vetclinic.application.dto.clinic.ClinicConfigDTO;
import com.example.vetclinic.application.dto.clinic.ClinicDTO;
import com.example.vetclinic.application.dto.clinic.ClinicStatsDTO;
import com.example.vetclinic.domain.model.AppointmentStatus;
import com.example.vetclinic.domain.model.Clinic;
import com.example.vetclinic.infrastructure.mapper.ClinicMapper;
import com.example.vetclinic.infrastructure.persistence.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ClinicService {

    private final ClinicJpaRepository clinicRepository;
    private final AppointmentJpaRepository appointmentRepository;
    private final PetJpaRepository petRepository;
    private final OwnerJpaRepository ownerRepository;
    private final VetJpaRepository vetRepository;
    private final MedicalRecordJpaRepository medicalRecordRepository;
    private final VeterinaryServiceJpaRepository veterinaryServiceRepository;
    private final ClinicMapper clinicMapper;

    @Transactional(readOnly = true)
    public ClinicDTO getClinicConfig() {
        Clinic clinic = clinicRepository.findFirstByActiveTrue()
                .orElseThrow(() -> new EntityNotFoundException("No active clinic configuration found"));
        return clinicMapper.toDTO(clinic);
    }

    @Transactional
    public ClinicDTO updateClinicConfig(ClinicConfigDTO configDTO) {
        Clinic clinic = clinicRepository.findFirstByActiveTrue()
                .orElseGet(() -> {
                    Clinic newClinic = clinicMapper.toEntity(configDTO);
                    newClinic.setActive(true);
                    return newClinic;
                });

        if (clinic.getId() != null) {
            clinicMapper.updateEntityFromDTO(configDTO, clinic);
        }

        Clinic savedClinic = clinicRepository.save(clinic);
        return clinicMapper.toDTO(savedClinic);
    }

    @Transactional(readOnly = true)
    public ClinicStatsDTO getClinicStats() {
        long totalAppointments = appointmentRepository.count();
        long pendingAppointments = appointmentRepository.countByStatus(AppointmentStatus.PENDING);
        long confirmedAppointments = appointmentRepository.countByStatus(AppointmentStatus.CONFIRMED);
        long completedAppointments = appointmentRepository.countByStatus(AppointmentStatus.COMPLETED);
        long cancelledAppointments = appointmentRepository.countByStatus(AppointmentStatus.CANCELLED);

        long totalPets = petRepository.count();
        long totalOwners = ownerRepository.count();
        long totalVets = vetRepository.count();
        long totalMedicalRecords = medicalRecordRepository.count();

        // Calculate estimated revenue from completed appointments
        BigDecimal estimatedRevenue = appointmentRepository.findAll().stream()
                .filter(appointment -> appointment.getStatus() == AppointmentStatus.COMPLETED)
                .map(appointment -> appointment.getService().getBaseCost())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return ClinicStatsDTO.builder()
                .totalAppointments(totalAppointments)
                .pendingAppointments(pendingAppointments)
                .confirmedAppointments(confirmedAppointments)
                .completedAppointments(completedAppointments)
                .cancelledAppointments(cancelledAppointments)
                .totalPets(totalPets)
                .totalOwners(totalOwners)
                .totalVets(totalVets)
                .totalMedicalRecords(totalMedicalRecords)
                .estimatedRevenue(estimatedRevenue)
                .build();
    }
}
