package com.example.vetclinic.application.service;

import com.example.vetclinic.application.dto.pet.CreatePetDTO;
import com.example.vetclinic.application.dto.pet.PetDTO;
import com.example.vetclinic.application.dto.pet.UpdatePetDTO;
import com.example.vetclinic.domain.model.Owner;
import com.example.vetclinic.domain.model.Pet;
import com.example.vetclinic.infrastructure.mapper.PetMapper;
import com.example.vetclinic.infrastructure.persistence.OwnerJpaRepository;
import com.example.vetclinic.infrastructure.persistence.PetJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @Mock
    private PetJpaRepository petRepository;

    @Mock
    private OwnerJpaRepository ownerRepository;

    @Mock
    private PetMapper petMapper;

    @InjectMocks
    private PetService petService;

    private Pet pet;
    private PetDTO petDTO;
    private CreatePetDTO createPetDTO;
    private Owner owner;

    @BeforeEach
    void setUp() {
        owner = Owner.builder()
                .id(1L)
                .firstName("Juan")
                .lastName("Pérez")
                .build();

        pet = Pet.builder()
                .id(1L)
                .name("Max")
                .species("Perro")
                .breed("Labrador")
                .birthDate(LocalDate.of(2020, 5, 15))
                .owner(owner)
                .build();

        petDTO = new PetDTO();
        petDTO.setId(1L);
        petDTO.setName("Max");
        petDTO.setSpecies("Perro");

        createPetDTO = new CreatePetDTO();
        createPetDTO.setName("Max");
        createPetDTO.setSpecies("Perro");
        createPetDTO.setBreed("Labrador");
        createPetDTO.setBirthDate(LocalDate.of(2020, 5, 15));
        createPetDTO.setOwnerId(1L);
    }

    @Test
    void getPetById_ShouldReturnPet_WhenExists() {
        // Given
        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));
        when(petMapper.toDTO(pet)).thenReturn(petDTO);

        // When
        PetDTO result = petService.getPetById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Max");
        verify(petRepository).findById(1L);
    }

    @Test
    void getPetById_ShouldThrowException_WhenNotFound() {
        // Given
        when(petRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> petService.getPetById(999L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Pet not found");
    }

    @Test
    void createPet_ShouldReturnCreatedPet() {
        // Given
        when(ownerRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(petMapper.toEntity(createPetDTO)).thenReturn(pet);
        when(petRepository.save(any(Pet.class))).thenReturn(pet);
        when(petMapper.toDTO(pet)).thenReturn(petDTO);

        // When
        PetDTO result = petService.createPet(createPetDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Max");
        verify(ownerRepository).findById(1L);
        verify(petRepository).save(any(Pet.class));
    }

    @Test
    void createPet_ShouldThrowException_WhenOwnerNotFound() {
        // Given
        when(ownerRepository.findById(999L)).thenReturn(Optional.empty());
        createPetDTO.setOwnerId(999L);

        // When & Then
        assertThatThrownBy(() -> petService.createPet(createPetDTO))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Owner not found");
    }

    @Test
    void getPetsByOwnerId_ShouldReturnPetsList() {
        // Given
        when(ownerRepository.existsById(1L)).thenReturn(true);
        List<Pet> pets = Arrays.asList(pet);
        when(petRepository.findByOwnerId(1L)).thenReturn(pets);
        when(petMapper.toDTO(pet)).thenReturn(petDTO);

        // When
        List<PetDTO> result = petService.getPetsByOwnerId(1L);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Max");
        verify(ownerRepository).existsById(1L);
        verify(petRepository).findByOwnerId(1L);
    }

    @Test
    void updatePet_ShouldReturnUpdatedPet() {
        // Given
        UpdatePetDTO updateDTO = new UpdatePetDTO();
        updateDTO.setName("Max Updated");

        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));
        doNothing().when(petMapper).updateEntityFromDTO(updateDTO, pet);
        when(petRepository.save(pet)).thenReturn(pet);
        when(petMapper.toDTO(pet)).thenReturn(petDTO);

        // When
        PetDTO result = petService.updatePet(1L, updateDTO);

        // Then
        assertThat(result).isNotNull();
        verify(petMapper).updateEntityFromDTO(updateDTO, pet);
        verify(petRepository).save(pet);
    }

    @Test
    void deletePet_ShouldCallRepository() {
        // Given
        when(petRepository.existsById(1L)).thenReturn(true);
        doNothing().when(petRepository).deleteById(1L);

        // When
        petService.deletePet(1L);

        // Then
        verify(petRepository).existsById(1L);
        verify(petRepository).deleteById(1L);
    }
}
