package com.example.vetclinic.application.service;

import com.example.vetclinic.application.dto.clinic.ClinicConfigDTO;
import com.example.vetclinic.application.dto.clinic.ClinicDTO;
import com.example.vetclinic.application.dto.clinic.ClinicStatsDTO;
import com.example.vetclinic.domain.model.*;
import com.example.vetclinic.infrastructure.mapper.ClinicMapper;
import com.example.vetclinic.infrastructure.persistence.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClinicServiceTest {

    @Mock
    private ClinicJpaRepository clinicRepository;

    @Mock
    private AppointmentJpaRepository appointmentRepository;

    @Mock
    private PetJpaRepository petRepository;

    @Mock
    private OwnerJpaRepository ownerRepository;

    @Mock
    private VetJpaRepository vetRepository;

    @Mock
    private MedicalRecordJpaRepository medicalRecordRepository;

    @Mock
    private VeterinaryServiceJpaRepository veterinaryServiceRepository;

    @Mock
    private ClinicMapper clinicMapper;

    @InjectMocks
    private ClinicService clinicService;

    private Clinic clinic;
    private ClinicDTO clinicDTO;
    private ClinicConfigDTO configDTO;

    @BeforeEach
    void setUp() {
        clinic = Clinic.builder()
                .id(1L)
                .name("Veterinaria Central")
                .address("Calle Principal 123")
                .phone("555-1234")
                .email("info@vetclinic.com")
                .openingTime(LocalTime.of(8, 0))
                .closingTime(LocalTime.of(18, 0))
                .maxDailyAppointments(20)
                .active(true)
                .build();

        clinicDTO = new ClinicDTO();
        clinicDTO.setId(1L);
        clinicDTO.setName("Veterinaria Central");

        configDTO = new ClinicConfigDTO();
        configDTO.setName("Veterinaria Central");
        configDTO.setAddress("Calle Principal 123");
        configDTO.setPhone("555-1234");
        configDTO.setEmail("info@vetclinic.com");
        configDTO.setOpeningTime(LocalTime.of(8, 0));
        configDTO.setClosingTime(LocalTime.of(18, 0));
        configDTO.setMaxDailyAppointments(20);
    }

    @Test
    void getClinicConfig_ShouldReturnConfig_WhenExists() {
        // Given
        when(clinicRepository.findFirstByActiveTrue()).thenReturn(Optional.of(clinic));
        when(clinicMapper.toDTO(clinic)).thenReturn(clinicDTO);

        // When
        ClinicDTO result = clinicService.getClinicConfig();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Veterinaria Central");
        verify(clinicRepository).findFirstByActiveTrue();
    }

    @Test
    void updateClinicConfig_ShouldReturnUpdatedConfig() {
        // Given
        when(clinicRepository.findFirstByActiveTrue()).thenReturn(Optional.of(clinic));
        doNothing().when(clinicMapper).updateEntityFromDTO(configDTO, clinic);
        when(clinicRepository.save(clinic)).thenReturn(clinic);
        when(clinicMapper.toDTO(clinic)).thenReturn(clinicDTO);

        // When
        ClinicDTO result = clinicService.updateClinicConfig(configDTO);

        // Then
        assertThat(result).isNotNull();
        verify(clinicMapper).updateEntityFromDTO(configDTO, clinic);
        verify(clinicRepository).save(clinic);
    }

    @Test
    void getClinicStats_ShouldReturnStatistics() {
        // Given
        when(appointmentRepository.count()).thenReturn(100L);
        when(appointmentRepository.countByStatus(AppointmentStatus.PENDING)).thenReturn(20L);
        when(appointmentRepository.countByStatus(AppointmentStatus.CONFIRMED)).thenReturn(30L);
        when(appointmentRepository.countByStatus(AppointmentStatus.COMPLETED)).thenReturn(40L);
        when(appointmentRepository.countByStatus(AppointmentStatus.CANCELLED)).thenReturn(10L);
        when(petRepository.count()).thenReturn(50L);
        when(ownerRepository.count()).thenReturn(30L);
        when(vetRepository.count()).thenReturn(5L);
        when(medicalRecordRepository.count()).thenReturn(80L);
        when(appointmentRepository.findAll()).thenReturn(new ArrayList<>());

        // When
        ClinicStatsDTO result = clinicService.getClinicStats();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTotalAppointments()).isEqualTo(100L);
        assertThat(result.getPendingAppointments()).isEqualTo(20L);
        assertThat(result.getConfirmedAppointments()).isEqualTo(30L);
        assertThat(result.getCompletedAppointments()).isEqualTo(40L);
        assertThat(result.getCancelledAppointments()).isEqualTo(10L);
        assertThat(result.getTotalPets()).isEqualTo(50L);
        assertThat(result.getTotalOwners()).isEqualTo(30L);
        assertThat(result.getTotalVets()).isEqualTo(5L);
        assertThat(result.getTotalMedicalRecords()).isEqualTo(80L);
        verify(appointmentRepository).count();
        verify(petRepository).count();
    }

    @Test
    void updateClinicConfig_ShouldCreateNew_WhenNotExists() {
        // Given
        when(clinicRepository.findFirstByActiveTrue()).thenReturn(Optional.empty());
        when(clinicMapper.toEntity(configDTO)).thenReturn(clinic);
        when(clinicRepository.save(any(Clinic.class))).thenReturn(clinic);
        when(clinicMapper.toDTO(clinic)).thenReturn(clinicDTO);

        // When
        ClinicDTO result = clinicService.updateClinicConfig(configDTO);

        // Then
        assertThat(result).isNotNull();
        verify(clinicMapper).toEntity(configDTO);
        verify(clinicRepository).save(any(Clinic.class));
    }
}
