package com.example.vetclinic.application.service;

import com.example.vetclinic.application.dto.appointment.AppointmentDTO;
import com.example.vetclinic.application.dto.appointment.CreateAppointmentDTO;
import com.example.vetclinic.application.dto.appointment.UpdateAppointmentDTO;
import com.example.vetclinic.domain.model.*;
import com.example.vetclinic.infrastructure.mapper.AppointmentMapper;
import com.example.vetclinic.infrastructure.persistence.AppointmentJpaRepository;
import com.example.vetclinic.infrastructure.persistence.PetJpaRepository;
import com.example.vetclinic.infrastructure.persistence.VetJpaRepository;
import com.example.vetclinic.infrastructure.persistence.VeterinaryServiceJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentJpaRepository appointmentRepository;
    private final PetJpaRepository petRepository;
    private final VetJpaRepository vetRepository;
    private final VeterinaryServiceJpaRepository serviceRepository;
    private final AppointmentMapper appointmentMapper;

    @Transactional(readOnly = true)
    public List<AppointmentDTO> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(appointmentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AppointmentDTO getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found with id: " + id));
        return appointmentMapper.toDTO(appointment);
    }

    @Transactional(readOnly = true)
    public List<AppointmentDTO> getAppointmentsByPet(Long petId) {
        if (!petRepository.existsById(petId)) {
            throw new EntityNotFoundException("Pet not found with id: " + petId);
        }
        return appointmentRepository.findByPetId(petId).stream()
                .map(appointmentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AppointmentDTO> getAppointmentsByVet(Long vetId) {
        if (!vetRepository.existsById(vetId)) {
            throw new EntityNotFoundException("Vet not found with id: " + vetId);
        }
        return appointmentRepository.findByVetId(vetId).stream()
                .map(appointmentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public AppointmentDTO createAppointment(CreateAppointmentDTO createDTO) {
        Pet pet = petRepository.findById(createDTO.getPetId())
                .orElseThrow(() -> new EntityNotFoundException("Pet not found with id: " + createDTO.getPetId()));

        Vet vet = vetRepository.findById(createDTO.getVetId())
                .orElseThrow(() -> new EntityNotFoundException("Vet not found with id: " + createDTO.getVetId()));

        Appointment appointment = appointmentMapper.toEntity(createDTO);
        appointment.setPet(pet);
        appointment.setVet(vet);

        if (createDTO.getServiceId() != null) {
            VeterinaryService service = serviceRepository.findById(createDTO.getServiceId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Service not found with id: " + createDTO.getServiceId()));
            appointment.setService(service);
        }

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return appointmentMapper.toDTO(savedAppointment);
    }

    @Transactional
    public AppointmentDTO updateAppointment(Long id, UpdateAppointmentDTO updateDTO) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found with id: " + id));

        appointmentMapper.updateEntityFromDTO(updateDTO, appointment);

        if (updateDTO.getVetId() != null) {
            Vet vet = vetRepository.findById(updateDTO.getVetId())
                    .orElseThrow(() -> new EntityNotFoundException("Vet not found with id: " + updateDTO.getVetId()));
            appointment.setVet(vet);
        }

        if (updateDTO.getServiceId() != null) {
            VeterinaryService service = serviceRepository.findById(updateDTO.getServiceId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Service not found with id: " + updateDTO.getServiceId()));
            appointment.setService(service);
        }

        Appointment updatedAppointment = appointmentRepository.save(appointment);
        return appointmentMapper.toDTO(updatedAppointment);
    }

    @Transactional
    public AppointmentDTO confirmAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found with id: " + id));
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        Appointment confirmedAppointment = appointmentRepository.save(appointment);
        return appointmentMapper.toDTO(confirmedAppointment);
    }

    @Transactional
    public AppointmentDTO cancelAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found with id: " + id));
        appointment.setStatus(AppointmentStatus.CANCELLED);
        Appointment cancelledAppointment = appointmentRepository.save(appointment);
        return appointmentMapper.toDTO(cancelledAppointment);
    }
}
