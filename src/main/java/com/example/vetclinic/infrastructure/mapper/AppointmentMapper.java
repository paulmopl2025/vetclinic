package com.example.vetclinic.infrastructure.mapper;

import com.example.vetclinic.application.dto.appointment.AppointmentDTO;
import com.example.vetclinic.application.dto.appointment.CreateAppointmentDTO;
import com.example.vetclinic.application.dto.appointment.UpdateAppointmentDTO;
import com.example.vetclinic.domain.model.Appointment;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    @Mapping(source = "pet.id", target = "petId")
    @Mapping(source = "pet.name", target = "petName")
    @Mapping(source = "vet.id", target = "vetId")
    @Mapping(target = "vetName", expression = "java(appointment.getVet().getFirstName() + \" \" + appointment.getVet().getLastName())")
    @Mapping(source = "service.id", target = "serviceId")
    @Mapping(source = "service.name", target = "serviceName")
    AppointmentDTO toDTO(Appointment appointment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "pet", ignore = true)
    @Mapping(target = "vet", ignore = true)
    @Mapping(target = "service", ignore = true)
    Appointment toEntity(CreateAppointmentDTO createDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "pet", ignore = true)
    @Mapping(target = "vet", ignore = true)
    @Mapping(target = "service", ignore = true)
    void updateEntityFromDTO(UpdateAppointmentDTO updateDTO, @MappingTarget Appointment appointment);
}
