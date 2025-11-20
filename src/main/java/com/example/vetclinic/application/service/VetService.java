package com.example.vetclinic.application.service;

import com.example.vetclinic.application.dto.vet.CreateVetDTO;
import com.example.vetclinic.application.dto.vet.UpdateVetDTO;
import com.example.vetclinic.application.dto.vet.VetDTO;
import com.example.vetclinic.domain.model.Specialty;
import com.example.vetclinic.domain.model.Vet;
import com.example.vetclinic.infrastructure.mapper.VetMapper;
import com.example.vetclinic.infrastructure.persistence.SpecialtyJpaRepository;
import com.example.vetclinic.infrastructure.persistence.VetJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VetService {

    private final VetJpaRepository vetRepository;
    private final SpecialtyJpaRepository specialtyRepository;
    private final VetMapper vetMapper;

    @Transactional(readOnly = true)
    public List<VetDTO> getAllVets() {
        return vetRepository.findAll().stream()
                .map(vetMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VetDTO getVetById(Long id) {
        Vet vet = vetRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vet not found with id: " + id));
        return vetMapper.toDTO(vet);
    }

    @Transactional
    public VetDTO createVet(CreateVetDTO createDTO) {
        Vet vet = vetMapper.toEntity(createDTO);

        if (createDTO.getSpecialtyIds() != null && !createDTO.getSpecialtyIds().isEmpty()) {
            Set<Specialty> specialties = loadSpecialties(createDTO.getSpecialtyIds());
            vet.setSpecialties(specialties);
        } else {
            vet.setSpecialties(new HashSet<>());
        }

        Vet savedVet = vetRepository.save(vet);
        return vetMapper.toDTO(savedVet);
    }

    @Transactional
    public VetDTO updateVet(Long id, UpdateVetDTO updateDTO) {
        Vet vet = vetRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vet not found with id: " + id));

        vetMapper.updateEntityFromDTO(updateDTO, vet);

        if (updateDTO.getSpecialtyIds() != null) {
            Set<Specialty> specialties = loadSpecialties(updateDTO.getSpecialtyIds());
            vet.setSpecialties(specialties);
        }

        Vet updatedVet = vetRepository.save(vet);
        return vetMapper.toDTO(updatedVet);
    }

    @Transactional
    public void deleteVet(Long id) {
        if (!vetRepository.existsById(id)) {
            throw new EntityNotFoundException("Vet not found with id: " + id);
        }
        vetRepository.deleteById(id);
    }

    private Set<Specialty> loadSpecialties(Set<Long> specialtyIds) {
        Set<Specialty> specialties = new HashSet<>();
        for (Long specialtyId : specialtyIds) {
            Specialty specialty = specialtyRepository.findById(specialtyId)
                    .orElseThrow(() -> new EntityNotFoundException("Specialty not found with id: " + specialtyId));
            specialties.add(specialty);
        }
        return specialties;
    }
}
