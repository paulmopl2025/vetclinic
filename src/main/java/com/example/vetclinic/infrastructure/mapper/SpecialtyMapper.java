package com.example.vetclinic.infrastructure.mapper;

import com.example.vetclinic.application.dto.specialty.CreateSpecialtyDTO;
import com.example.vetclinic.application.dto.specialty.SpecialtyDTO;
import com.example.vetclinic.application.dto.specialty.UpdateSpecialtyDTO;
import com.example.vetclinic.domain.model.Specialty;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface SpecialtyMapper {

    SpecialtyDTO toDTO(Specialty specialty);

    @Mapping(target = "id", ignore = true)
    Specialty toEntity(CreateSpecialtyDTO createDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDTO(UpdateSpecialtyDTO updateDTO, @MappingTarget Specialty specialty);
}
