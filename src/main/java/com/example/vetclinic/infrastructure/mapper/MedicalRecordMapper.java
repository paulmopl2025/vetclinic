package com.example.vetclinic.infrastructure.mapper;

import com.example.vetclinic.application.dto.medicalrecord.CreateMedicalRecordDTO;
import com.example.vetclinic.application.dto.medicalrecord.MedicalRecordDTO;
import com.example.vetclinic.application.dto.medicalrecord.UpdateMedicalRecordDTO;
import com.example.vetclinic.domain.model.MedicalRecord;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface MedicalRecordMapper {

    @Mapping(source = "pet.id", target = "petId")
    @Mapping(source = "pet.name", target = "petName")
    @Mapping(source = "vet.id", target = "vetId")
    @Mapping(source = "vet.firstName", target = "vetName")
    @Mapping(source = "appointment.id", target = "appointmentId")
    MedicalRecordDTO toDTO(MedicalRecord medicalRecord);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pet", ignore = true)
    @Mapping(target = "vet", ignore = true)
    @Mapping(target = "appointment", ignore = true)
    MedicalRecord toEntity(CreateMedicalRecordDTO createDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pet", ignore = true)
    @Mapping(target = "vet", ignore = true)
    @Mapping(target = "appointment", ignore = true)
    void updateEntityFromDTO(UpdateMedicalRecordDTO updateDTO, @MappingTarget MedicalRecord medicalRecord);
}
