package com.example.vetclinic.presentation.controller;

import com.example.vetclinic.application.dto.owner.CreateOwnerDTO;
import com.example.vetclinic.application.dto.owner.OwnerDTO;
import com.example.vetclinic.application.service.OwnerService;
import com.example.vetclinic.security.JwtTokenProvider;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class OwnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OwnerService ownerService;

    private OwnerDTO ownerDTO;
    private CreateOwnerDTO createOwnerDTO;

    @BeforeEach
    void setUp() {
        ownerDTO = new OwnerDTO();
        ownerDTO.setId(1L);
        ownerDTO.setFirstName("Juan");
        ownerDTO.setLastName("Pérez");
        ownerDTO.setPhone("555-0101");
        ownerDTO.setEmail("juan@email.com");

        createOwnerDTO = new CreateOwnerDTO();
        createOwnerDTO.setFirstName("Juan");
        createOwnerDTO.setLastName("Pérez");
        createOwnerDTO.setPhone("555-0101");
        createOwnerDTO.setEmail("juan@email.com");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllOwners_ShouldReturnOwnersList() throws Exception {
        // Given
        List<OwnerDTO> owners = Arrays.asList(ownerDTO);
        when(ownerService.getAllOwners()).thenReturn(owners);

        // When & Then
        mockMvc.perform(get("/api/owners"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("Juan"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getOwnerById_ShouldReturnOwner() throws Exception {
        // Given
        when(ownerService.getOwnerById(1L)).thenReturn(ownerDTO);

        // When & Then
        mockMvc.perform(get("/api/owners/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Juan"))
                .andExpect(jsonPath("$.email").value("juan@email.com"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createOwner_ShouldReturnCreatedOwner() throws Exception {
        // Given
        when(ownerService.createOwner(any(CreateOwnerDTO.class))).thenReturn(ownerDTO);

        // When & Then
        mockMvc.perform(post("/api/owners")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createOwnerDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Juan"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteOwner_ShouldReturnNoContent() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/owners/1")
                .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAllOwners_ShouldReturn401_WhenNotAuthenticated() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/owners"))
                .andExpect(status().isUnauthorized());
    }
}
