package com.example.vetclinic.application.service;

import com.example.vetclinic.application.dto.medicalrecord.CreateMedicalRecordDTO;
import com.example.vetclinic.application.dto.medicalrecord.MedicalRecordDTO;
import com.example.vetclinic.application.dto.medicalrecord.UpdateMedicalRecordDTO;
import com.example.vetclinic.domain.model.Appointment;
import com.example.vetclinic.domain.model.MedicalRecord;
import com.example.vetclinic.domain.model.Pet;
import com.example.vetclinic.domain.model.Vet;
import com.example.vetclinic.infrastructure.mapper.MedicalRecordMapper;
import com.example.vetclinic.infrastructure.persistence.AppointmentJpaRepository;
import com.example.vetclinic.infrastructure.persistence.MedicalRecordJpaRepository;
import com.example.vetclinic.infrastructure.persistence.PetJpaRepository;
import com.example.vetclinic.infrastructure.persistence.VetJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicalRecordService {

    private final MedicalRecordJpaRepository medicalRecordRepository;
    private final PetJpaRepository petRepository;
    private final VetJpaRepository vetRepository;
    private final AppointmentJpaRepository appointmentRepository;
    private final MedicalRecordMapper medicalRecordMapper;

    @Transactional(readOnly = true)
    public List<MedicalRecordDTO> getAllRecords() {
        return medicalRecordRepository.findAll().stream()
                .map(medicalRecordMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MedicalRecordDTO getRecordById(Long id) {
        MedicalRecord record = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Medical record not found with id: " + id));
        return medicalRecordMapper.toDTO(record);
    }

    @Transactional(readOnly = true)
    public List<MedicalRecordDTO> getRecordsByPet(Long petId) {
        if (!petRepository.existsById(petId)) {
            throw new EntityNotFoundException("Pet not found with id: " + petId);
        }
        return medicalRecordRepository.findByPetIdOrderByRecordDateDesc(petId).stream()
                .map(medicalRecordMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MedicalRecordDTO> getRecordsByVet(Long vetId) {
        if (!vetRepository.existsById(vetId)) {
            throw new EntityNotFoundException("Vet not found with id: " + vetId);
        }
        return medicalRecordRepository.findByVetId(vetId).stream()
                .map(medicalRecordMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public MedicalRecordDTO createRecord(CreateMedicalRecordDTO createDTO) {
        Pet pet = petRepository.findById(createDTO.getPetId())
                .orElseThrow(() -> new EntityNotFoundException("Pet not found with id: " + createDTO.getPetId()));

        Vet vet = vetRepository.findById(createDTO.getVetId())
                .orElseThrow(() -> new EntityNotFoundException("Vet not found with id: " + createDTO.getVetId()));

        MedicalRecord record = medicalRecordMapper.toEntity(createDTO);
        record.setPet(pet);
        record.setVet(vet);

        if (createDTO.getAppointmentId() != null) {
            Appointment appointment = appointmentRepository.findById(createDTO.getAppointmentId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Appointment not found with id: " + createDTO.getAppointmentId()));
            record.setAppointment(appointment);
        }

        MedicalRecord savedRecord = medicalRecordRepository.save(record);
        return medicalRecordMapper.toDTO(savedRecord);
    }

    @Transactional
    public MedicalRecordDTO updateRecord(Long id, UpdateMedicalRecordDTO updateDTO) {
        MedicalRecord record = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Medical record not found with id: " + id));

        medicalRecordMapper.updateEntityFromDTO(updateDTO, record);
        MedicalRecord updatedRecord = medicalRecordRepository.save(record);
        return medicalRecordMapper.toDTO(updatedRecord);
    }

    @Transactional
    public void deleteRecord(Long id) {
        if (!medicalRecordRepository.existsById(id)) {
            throw new EntityNotFoundException("Medical record not found with id: " + id);
        }
        medicalRecordRepository.deleteById(id);
    }
}
