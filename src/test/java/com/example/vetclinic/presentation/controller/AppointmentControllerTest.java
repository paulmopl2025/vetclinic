package com.example.vetclinic.presentation.controller;

import com.example.vetclinic.application.dto.appointment.AppointmentDTO;
import com.example.vetclinic.application.dto.appointment.CreateAppointmentDTO;
import com.example.vetclinic.application.service.AppointmentService;
import com.example.vetclinic.domain.model.AppointmentStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AppointmentService appointmentService;

    private AppointmentDTO appointmentDTO;
    private CreateAppointmentDTO createAppointmentDTO;

    @BeforeEach
    void setUp() {
        appointmentDTO = new AppointmentDTO();
        appointmentDTO.setId(1L);
        appointmentDTO.setStatus(AppointmentStatus.PENDING);
        appointmentDTO.setAppointmentDate(LocalDateTime.now().plusDays(2));

        createAppointmentDTO = new CreateAppointmentDTO();
        createAppointmentDTO.setPetId(1L);
        createAppointmentDTO.setVetId(1L);
        createAppointmentDTO.setServiceId(1L);
        createAppointmentDTO.setAppointmentDate(LocalDateTime.now().plusDays(2));
    }

    @Test
    @WithMockUser(roles = "RECEPCIONISTA")
    void getAllAppointments_ShouldReturnList() throws Exception {
        // Given
        when(appointmentService.getAllAppointments()).thenReturn(Arrays.asList(appointmentDTO));

        // When & Then
        mockMvc.perform(get("/api/appointments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }

    @Test
    @WithMockUser(roles = "RECEPCIONISTA")
    void createAppointment_ShouldReturnCreated() throws Exception {
        // Given
        when(appointmentService.createAppointment(any(CreateAppointmentDTO.class)))
                .thenReturn(appointmentDTO);

        // When & Then
        mockMvc.perform(post("/api/appointments")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createAppointmentDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(roles = "VET")
    void confirmAppointment_ShouldReturnUpdated() throws Exception {
        // Given
        appointmentDTO.setStatus(AppointmentStatus.CONFIRMED);
        when(appointmentService.confirmAppointment(1L)).thenReturn(appointmentDTO);

        // When & Then
        mockMvc.perform(patch("/api/appointments/1/confirm")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    @WithMockUser(roles = "RECEPCIONISTA")
    void cancelAppointment_ShouldReturnUpdated() throws Exception {
        // Given
        appointmentDTO.setStatus(AppointmentStatus.CANCELLED);
        when(appointmentService.cancelAppointment(1L)).thenReturn(appointmentDTO);

        // When & Then
        mockMvc.perform(patch("/api/appointments/1/cancel")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    @Test
    void createAppointment_ShouldReturn401_WhenNotAuthenticated() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/appointments")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createAppointmentDTO)))
                .andExpect(status().isUnauthorized());
    }
}
