package com.example.vetclinic.application.service;

import com.example.vetclinic.application.dto.vet.CreateVetDTO;
import com.example.vetclinic.application.dto.vet.VetDTO;
import com.example.vetclinic.domain.model.Specialty;
import com.example.vetclinic.domain.model.Vet;
import com.example.vetclinic.infrastructure.mapper.VetMapper;
import com.example.vetclinic.infrastructure.persistence.SpecialtyJpaRepository;
import com.example.vetclinic.infrastructure.persistence.VetJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VetServiceTest {

    @Mock
    private VetJpaRepository vetRepository;

    @Mock
    private SpecialtyJpaRepository specialtyRepository;

    @Mock
    private VetMapper vetMapper;

    @InjectMocks
    private VetService vetService;

    private Vet vet;
    private VetDTO vetDTO;
    private CreateVetDTO createVetDTO;
    private Specialty specialty;

    @BeforeEach
    void setUp() {
        specialty = Specialty.builder()
                .id(1L)
                .name("Cirugía")
                .build();

        vet = Vet.builder()
                .id(1L)
                .firstName("María")
                .lastName("González")
                .specialties(new HashSet<>(Set.of(specialty)))
                .build();

        vetDTO = new VetDTO();
        vetDTO.setId(1L);
        vetDTO.setFirstName("María");
        vetDTO.setLastName("González");

        createVetDTO = new CreateVetDTO();
        createVetDTO.setFirstName("María");
        createVetDTO.setLastName("González");
        createVetDTO.setSpecialtyIds(Set.of(1L));
    }

    @Test
    void getVetById_ShouldReturnVet_WhenExists() {
        // Given
        when(vetRepository.findById(1L)).thenReturn(Optional.of(vet));
        when(vetMapper.toDTO(vet)).thenReturn(vetDTO);

        // When
        VetDTO result = vetService.getVetById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFirstName()).isEqualTo("María");
        verify(vetRepository).findById(1L);
    }

    @Test
    void getVetById_ShouldThrowException_WhenNotFound() {
        // Given
        when(vetRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> vetService.getVetById(999L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Vet not found");
    }

    @Test
    void createVet_ShouldReturnCreatedVet_WithSpecialties() {
        // Given
        when(specialtyRepository.findById(1L)).thenReturn(Optional.of(specialty));
        when(vetMapper.toEntity(createVetDTO)).thenReturn(vet);
        when(vetRepository.save(any(Vet.class))).thenReturn(vet);
        when(vetMapper.toDTO(vet)).thenReturn(vetDTO);

        // When
        VetDTO result = vetService.createVet(createVetDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("María");
        verify(specialtyRepository).findById(1L);
        verify(vetRepository).save(any(Vet.class));
    }

    @Test
    void createVet_ShouldThrowException_WhenSpecialtyNotFound() {
        // Given
        when(specialtyRepository.findById(999L)).thenReturn(Optional.empty());
        createVetDTO.setSpecialtyIds(Set.of(999L));
        when(vetMapper.toEntity(createVetDTO)).thenReturn(vet);

        // When & Then
        assertThatThrownBy(() -> vetService.createVet(createVetDTO))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Specialty not found");
    }

    @Test
    void createVet_ShouldCreateWithoutSpecialties_WhenNoSpecialtiesProvided() {
        // Given
        createVetDTO.setSpecialtyIds(null);
        when(vetMapper.toEntity(createVetDTO)).thenReturn(vet);
        when(vetRepository.save(any(Vet.class))).thenReturn(vet);
        when(vetMapper.toDTO(vet)).thenReturn(vetDTO);

        // When
        VetDTO result = vetService.createVet(createVetDTO);

        // Then
        assertThat(result).isNotNull();
        verify(vetRepository).save(any(Vet.class));
        verify(specialtyRepository, never()).findById(any());
    }

    @Test
    void deleteVet_ShouldCallRepository() {
        // Given
        when(vetRepository.existsById(1L)).thenReturn(true);
        doNothing().when(vetRepository).deleteById(1L);

        // When
        vetService.deleteVet(1L);

        // Then
        verify(vetRepository).existsById(1L);
        verify(vetRepository).deleteById(1L);
    }
}
