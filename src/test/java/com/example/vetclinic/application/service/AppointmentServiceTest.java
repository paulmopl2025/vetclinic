package com.example.vetclinic.application.service;

import com.example.vetclinic.application.dto.appointment.AppointmentDTO;
import com.example.vetclinic.application.dto.appointment.CreateAppointmentDTO;
import com.example.vetclinic.domain.model.*;
import com.example.vetclinic.infrastructure.mapper.AppointmentMapper;
import com.example.vetclinic.infrastructure.persistence.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentJpaRepository appointmentRepository;

    @Mock
    private PetJpaRepository petRepository;

    @Mock
    private VetJpaRepository vetRepository;

    @Mock
    private VeterinaryServiceJpaRepository serviceRepository;

    @Mock
    private AppointmentMapper appointmentMapper;

    @InjectMocks
    private AppointmentService appointmentService;

    private Appointment appointment;
    private AppointmentDTO appointmentDTO;
    private CreateAppointmentDTO createAppointmentDTO;
    private Pet pet;
    private Vet vet;
    private VeterinaryService service;

    @BeforeEach
    void setUp() {
        pet = Pet.builder()
                .id(1L)
                .name("Max")
                .species("Perro")
                .breed("Labrador")
                .birthDate(LocalDate.of(2020, 5, 15))
                .build();

        vet = Vet.builder()
                .id(1L)
                .firstName("María")
                .lastName("González")
                .build();

        service = VeterinaryService.builder()
                .id(1L)
                .name("Vacunación")
                .serviceType(ServiceType.VACCINATION)
                .baseCost(new BigDecimal("50.00"))
                .estimatedDurationMinutes(15)
                .active(true)
                .build();

        appointment = Appointment.builder()
                .id(1L)
                .appointmentDate(LocalDateTime.now().plusDays(2))
                .status(AppointmentStatus.PENDING)
                .notes("Primera vacunación")
                .pet(pet)
                .vet(vet)
                .service(service)
                .build();

        appointmentDTO = new AppointmentDTO();
        appointmentDTO.setId(1L);
        appointmentDTO.setStatus(AppointmentStatus.PENDING);

        createAppointmentDTO = new CreateAppointmentDTO();
        createAppointmentDTO.setPetId(1L);
        createAppointmentDTO.setVetId(1L);
        createAppointmentDTO.setServiceId(1L);
        createAppointmentDTO.setAppointmentDate(LocalDateTime.now().plusDays(2));
    }

    @Test
    void getAppointmentById_ShouldReturnAppointment_WhenExists() {
        // Given
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(appointmentMapper.toDTO(appointment)).thenReturn(appointmentDTO);

        // When
        AppointmentDTO result = appointmentService.getAppointmentById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(appointmentRepository).findById(1L);
    }

    @Test
    void createAppointment_ShouldReturnCreatedAppointment() {
        // Given
        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));
        when(vetRepository.findById(1L)).thenReturn(Optional.of(vet));
        when(serviceRepository.findById(1L)).thenReturn(Optional.of(service));
        when(appointmentMapper.toEntity(createAppointmentDTO)).thenReturn(appointment);
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);
        when(appointmentMapper.toDTO(appointment)).thenReturn(appointmentDTO);

        // When
        AppointmentDTO result = appointmentService.createAppointment(createAppointmentDTO);

        // Then
        assertThat(result).isNotNull();
        verify(petRepository).findById(1L);
        verify(vetRepository).findById(1L);
        verify(serviceRepository).findById(1L);
        verify(appointmentRepository).save(any(Appointment.class));
    }

    @Test
    void confirmAppointment_ShouldChangeStatusToConfirmed() {
        // Given
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(appointment)).thenReturn(appointment);
        when(appointmentMapper.toDTO(appointment)).thenReturn(appointmentDTO);

        // When
        AppointmentDTO result = appointmentService.confirmAppointment(1L);

        // Then
        assertThat(appointment.getStatus()).isEqualTo(AppointmentStatus.CONFIRMED);
        verify(appointmentRepository).save(appointment);
    }

    @Test
    void cancelAppointment_ShouldChangeStatusToCancelled() {
        // Given
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(appointment)).thenReturn(appointment);
        when(appointmentMapper.toDTO(appointment)).thenReturn(appointmentDTO);

        // When
        AppointmentDTO result = appointmentService.cancelAppointment(1L);

        // Then
        assertThat(appointment.getStatus()).isEqualTo(AppointmentStatus.CANCELLED);
        verify(appointmentRepository).save(appointment);
    }

    @Test
    void createAppointment_ShouldThrowException_WhenPetNotFound() {
        // Given
        when(petRepository.findById(999L)).thenReturn(Optional.empty());
        createAppointmentDTO.setPetId(999L);

        // When & Then
        assertThatThrownBy(() -> appointmentService.createAppointment(createAppointmentDTO))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Pet not found");
    }
}
