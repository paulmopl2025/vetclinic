package com.example.vetclinic.infrastructure.mapper;

import com.example.vetclinic.application.dto.vet.CreateVetDTO;
import com.example.vetclinic.application.dto.vet.UpdateVetDTO;
import com.example.vetclinic.application.dto.vet.VetDTO;
import com.example.vetclinic.domain.model.Vet;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = { SpecialtyMapper.class })
public interface VetMapper {

    VetDTO toDTO(Vet vet);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "specialties", ignore = true)
    Vet toEntity(CreateVetDTO createDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "specialties", ignore = true)
    void updateEntityFromDTO(UpdateVetDTO updateDTO, @MappingTarget Vet vet);
}
