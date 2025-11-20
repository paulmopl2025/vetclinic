package com.example.vetclinic.application.service;

import com.example.vetclinic.application.dto.specialty.CreateSpecialtyDTO;
import com.example.vetclinic.application.dto.specialty.SpecialtyDTO;
import com.example.vetclinic.application.dto.specialty.UpdateSpecialtyDTO;
import com.example.vetclinic.domain.exception.BusinessException;
import com.example.vetclinic.domain.model.Specialty;
import com.example.vetclinic.infrastructure.mapper.SpecialtyMapper;
import com.example.vetclinic.infrastructure.persistence.SpecialtyJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpecialtyService {

    private final SpecialtyJpaRepository specialtyRepository;
    private final SpecialtyMapper specialtyMapper;

    @Transactional(readOnly = true)
    public List<SpecialtyDTO> getAllSpecialties() {
        return specialtyRepository.findAll().stream()
                .map(specialtyMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SpecialtyDTO getSpecialtyById(Long id) {
        Specialty specialty = specialtyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Specialty not found with id: " + id));
        return specialtyMapper.toDTO(specialty);
    }

    @Transactional
    public SpecialtyDTO createSpecialty(CreateSpecialtyDTO createDTO) {
        if (specialtyRepository.existsByName(createDTO.getName())) {
            throw new BusinessException("Specialty with name '" + createDTO.getName() + "' already exists");
        }

        Specialty specialty = specialtyMapper.toEntity(createDTO);
        Specialty savedSpecialty = specialtyRepository.save(specialty);
        return specialtyMapper.toDTO(savedSpecialty);
    }

    @Transactional
    public SpecialtyDTO updateSpecialty(Long id, UpdateSpecialtyDTO updateDTO) {
        Specialty specialty = specialtyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Specialty not found with id: " + id));

        if (updateDTO.getName() != null && !updateDTO.getName().equals(specialty.getName())) {
            if (specialtyRepository.existsByName(updateDTO.getName())) {
                throw new BusinessException("Specialty with name '" + updateDTO.getName() + "' already exists");
            }
        }

        specialtyMapper.updateEntityFromDTO(updateDTO, specialty);
        Specialty updatedSpecialty = specialtyRepository.save(specialty);
        return specialtyMapper.toDTO(updatedSpecialty);
    }

    @Transactional
    public void deleteSpecialty(Long id) {
        if (!specialtyRepository.existsById(id)) {
            throw new EntityNotFoundException("Specialty not found with id: " + id);
        }
        specialtyRepository.deleteById(id);
    }
}
