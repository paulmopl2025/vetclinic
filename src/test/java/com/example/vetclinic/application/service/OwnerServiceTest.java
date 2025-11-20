package com.example.vetclinic.application.service;

import com.example.vetclinic.application.dto.owner.CreateOwnerDTO;
import com.example.vetclinic.application.dto.owner.OwnerDTO;
import com.example.vetclinic.application.dto.owner.UpdateOwnerDTO;
import com.example.vetclinic.domain.model.Owner;
import com.example.vetclinic.infrastructure.mapper.OwnerMapper;
import com.example.vetclinic.infrastructure.persistence.OwnerJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OwnerServiceTest {

    @Mock
    private OwnerJpaRepository ownerRepository;

    @Mock
    private OwnerMapper ownerMapper;

    @InjectMocks
    private OwnerService ownerService;

    private Owner owner;
    private OwnerDTO ownerDTO;
    private CreateOwnerDTO createOwnerDTO;

    @BeforeEach
    void setUp() {
        owner = Owner.builder()
                .id(1L)
                .firstName("Juan")
                .lastName("Pérez")
                .phone("555-0101")
                .email("juan@email.com")
                .pets(new HashSet<>())
                .build();

        ownerDTO = new OwnerDTO();
        ownerDTO.setId(1L);
        ownerDTO.setFirstName("Juan");
        ownerDTO.setLastName("Pérez");
        ownerDTO.setPhone("555-0101");
        ownerDTO.setEmail("juan@email.com");

        createOwnerDTO = new CreateOwnerDTO();
        createOwnerDTO.setFirstName("Juan");
        createOwnerDTO.setLastName("Pérez");
        createOwnerDTO.setPhone("555-0101");
        createOwnerDTO.setEmail("juan@email.com");
    }

    @Test
    void getOwnerById_ShouldReturnOwner_WhenOwnerExists() {
        // Given
        when(ownerRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(ownerMapper.toDTO(owner)).thenReturn(ownerDTO);

        // When
        OwnerDTO result = ownerService.getOwnerById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFirstName()).isEqualTo("Juan");
        verify(ownerRepository).findById(1L);
        verify(ownerMapper).toDTO(owner);
    }

    @Test
    void getOwnerById_ShouldThrowException_WhenOwnerNotFound() {
        // Given
        when(ownerRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> ownerService.getOwnerById(999L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Owner not found");
        verify(ownerRepository).findById(999L);
        verify(ownerMapper, never()).toDTO(any());
    }

    @Test
    void createOwner_ShouldReturnCreatedOwner() {
        // Given
        when(ownerMapper.toEntity(createOwnerDTO)).thenReturn(owner);
        when(ownerRepository.save(owner)).thenReturn(owner);
        when(ownerMapper.toDTO(owner)).thenReturn(ownerDTO);

        // When
        OwnerDTO result = ownerService.createOwner(createOwnerDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("Juan");
        verify(ownerMapper).toEntity(createOwnerDTO);
        verify(ownerRepository).save(owner);
        verify(ownerMapper).toDTO(owner);
    }

    @Test
    void updateOwner_ShouldReturnUpdatedOwner_WhenOwnerExists() {
        // Given
        UpdateOwnerDTO updateDTO = new UpdateOwnerDTO();
        updateDTO.setFirstName("Juan Carlos");
        updateDTO.setPhone("555-9999");

        when(ownerRepository.findById(1L)).thenReturn(Optional.of(owner));
        doNothing().when(ownerMapper).updateEntityFromDTO(updateDTO, owner);
        when(ownerRepository.save(owner)).thenReturn(owner);
        when(ownerMapper.toDTO(owner)).thenReturn(ownerDTO);

        // When
        OwnerDTO result = ownerService.updateOwner(1L, updateDTO);

        // Then
        assertThat(result).isNotNull();
        verify(ownerRepository).findById(1L);
        verify(ownerMapper).updateEntityFromDTO(updateDTO, owner);
        verify(ownerRepository).save(owner);
    }

    @Test
    void deleteOwner_ShouldCallRepository_WhenOwnerExists() {
        // Given
        when(ownerRepository.existsById(1L)).thenReturn(true);
        doNothing().when(ownerRepository).deleteById(1L);

        // When
        ownerService.deleteOwner(1L);

        // Then
        verify(ownerRepository).existsById(1L);
        verify(ownerRepository).deleteById(1L);
    }

    @Test
    void deleteOwner_ShouldThrowException_WhenOwnerNotFound() {
        // Given
        when(ownerRepository.existsById(999L)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> ownerService.deleteOwner(999L))
                .isInstanceOf(EntityNotFoundException.class);
        verify(ownerRepository).existsById(999L);
        verify(ownerRepository, never()).deleteById(any());
    }
}
