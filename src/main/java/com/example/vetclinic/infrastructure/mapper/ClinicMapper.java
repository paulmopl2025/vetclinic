package com.example.vetclinic.infrastructure.mapper;

import com.example.vetclinic.application.dto.clinic.ClinicConfigDTO;
import com.example.vetclinic.application.dto.clinic.ClinicDTO;
import com.example.vetclinic.domain.model.Clinic;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ClinicMapper {

    ClinicDTO toDTO(Clinic clinic);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    Clinic toEntity(ClinicConfigDTO configDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    void updateEntityFromDTO(ClinicConfigDTO configDTO, @MappingTarget Clinic clinic);
}
