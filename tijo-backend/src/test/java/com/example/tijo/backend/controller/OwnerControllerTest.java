package com.example.tijo.backend.controller;

import com.example.tijo.backend.model.command.CreateOwnerCommand;
import com.example.tijo.backend.model.dto.OwnerDto;
import com.example.tijo.backend.service.OwnerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OwnerController.class)
class OwnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OwnerService ownerService;

    @Test
    void findAllOwners_shouldReturnPageOfOwners() throws Exception {
        // Given
        OwnerDto owner1 = new OwnerDto(1, "Jan", "Kowalski", 2);
        OwnerDto owner2 = new OwnerDto(2, "Anna", "Nowak", 1);
        Page<OwnerDto> page = new PageImpl<>(List.of(owner1, owner2), PageRequest.of(0, 10), 2);

        when(ownerService.findAll(any())).thenReturn(page);

        // When & Then
        mockMvc.perform(get("/api/v1/owners")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].name", is("Jan")))
                .andExpect(jsonPath("$.content[0].surname", is("Kowalski")))
                .andExpect(jsonPath("$.content[0].ownedCars", is(2)))
                .andExpect(jsonPath("$.content[1].id", is(2)))
                .andExpect(jsonPath("$.content[1].name", is("Anna")))
                .andExpect(jsonPath("$.content[1].surname", is("Nowak")))
                .andExpect(jsonPath("$.content[1].ownedCars", is(1)))
                .andExpect(jsonPath("$.totalElements", is(2)));

        verify(ownerService, times(1)).findAll(any());
    }

    @Test
    void findAllOwners_shouldReturnEmptyPage_whenNoOwners() throws Exception {
        // Given
        Page<OwnerDto> emptyPage = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);
        when(ownerService.findAll(any())).thenReturn(emptyPage);

        // When & Then
        mockMvc.perform(get("/api/v1/owners")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)))
                .andExpect(jsonPath("$.totalElements", is(0)));

        verify(ownerService, times(1)).findAll(any());
    }

    @Test
    void findAllOwners_shouldHandlePagination() throws Exception {
        // Given
        OwnerDto owner = new OwnerDto(3, "Piotr", "Wiśniewski", 0);
        Page<OwnerDto> page = new PageImpl<>(List.of(owner), PageRequest.of(1, 5), 10);

        when(ownerService.findAll(any())).thenReturn(page);

        // When & Then
        mockMvc.perform(get("/api/v1/owners")
                        .param("page", "1")
                        .param("size", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.totalElements", is(10)))
                .andExpect(jsonPath("$.number", is(1)))
                .andExpect(jsonPath("$.size", is(5)));
    }

    // Testy dla POST /api/v1/owners

    @Test
    void addOwner_shouldCreateNewOwner_whenValidCommand() throws Exception {
        // Given
        CreateOwnerCommand command = new CreateOwnerCommand();
        command.setName("Maria");
        command.setSurname("Lewandowska");
        command.setPesel("92071314764");

        OwnerDto createdOwner = new OwnerDto(1, "Maria", "Lewandowska", 0);
        when(ownerService.saveOwner(any(CreateOwnerCommand.class))).thenReturn(createdOwner);

        // When & Then
        mockMvc.perform(post("/api/v1/owners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Maria")))
                .andExpect(jsonPath("$.surname", is("Lewandowska")))
                .andExpect(jsonPath("$.ownedCars", is(0)));

        verify(ownerService, times(1)).saveOwner(any(CreateOwnerCommand.class));
    }

    @Test
    void addOwner_shouldReturnBadRequest_whenNameIsBlank() throws Exception {
        // Given
        CreateOwnerCommand command = new CreateOwnerCommand();
        command.setName("");
        command.setSurname("Kowalski");
        command.setPesel("44051401458");

        // When & Then
        mockMvc.perform(post("/api/v1/owners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest());

        verify(ownerService, never()).saveOwner(any(CreateOwnerCommand.class));
    }

    @Test
    void addOwner_shouldReturnBadRequest_whenSurnameIsBlank() throws Exception {
        // Given
        CreateOwnerCommand command = new CreateOwnerCommand();
        command.setName("Jan");
        command.setSurname("");
        command.setPesel("44051401458");

        // When & Then
        mockMvc.perform(post("/api/v1/owners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest());

        verify(ownerService, never()).saveOwner(any(CreateOwnerCommand.class));
    }

    @Test
    void addOwner_shouldReturnBadRequest_whenPeselIsNull() throws Exception {
        // Given
        CreateOwnerCommand command = new CreateOwnerCommand();
        command.setName("Jan");
        command.setSurname("Kowalski");
        command.setPesel(null);

        // When & Then
        mockMvc.perform(post("/api/v1/owners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest());

        verify(ownerService, never()).saveOwner(any(CreateOwnerCommand.class));
    }

    @Test
    void addOwner_shouldReturnBadRequest_whenPeselIsInvalid() throws Exception {
        // Given
        CreateOwnerCommand command = new CreateOwnerCommand();
        command.setName("Jan");
        command.setSurname("Kowalski");
        command.setPesel("12345678901"); // niepoprawny PESEL

        // When & Then
        mockMvc.perform(post("/api/v1/owners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest());

        verify(ownerService, never()).saveOwner(any(CreateOwnerCommand.class));
    }

    // Testy dla DELETE /api/v1/owners/{id}

    @Test
    void deleteOwner_shouldDeleteOwner_whenValidId() throws Exception {
        // Given
        int ownerId = 1;
        doNothing().when(ownerService).deleteById(ownerId);

        // When & Then
        mockMvc.perform(delete("/api/v1/owners/{id}", ownerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(ownerService, times(1)).deleteById(ownerId);
    }

    @Test
    void deleteOwner_shouldCallServiceWithCorrectId() throws Exception {
        // Given
        int ownerId = 999;
        doNothing().when(ownerService).deleteById(ownerId);

        // When & Then
        mockMvc.perform(delete("/api/v1/owners/{id}", ownerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(ownerService, times(1)).deleteById(999);
    }

    // Testy dla POST /api/v1/owners/{id}/cars/{carId}

    @Test
    void assignCar_shouldAssignCarToOwner_whenValidIds() throws Exception {
        // Given
        int ownerId = 1;
        int carId = 5;
        doNothing().when(ownerService).assignCarToOwner(ownerId, carId);

        // When & Then
        mockMvc.perform(post("/api/v1/owners/{id}/cars/{carId}", ownerId, carId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(ownerService, times(1)).assignCarToOwner(ownerId, carId);
    }

    @Test
    void assignCar_shouldCallServiceWithCorrectIds() throws Exception {
        // Given
        int ownerId = 10;
        int carId = 20;
        doNothing().when(ownerService).assignCarToOwner(ownerId, carId);

        // When & Then
        mockMvc.perform(post("/api/v1/owners/{id}/cars/{carId}", ownerId, carId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(ownerService, times(1)).assignCarToOwner(10, 20);
    }

    @Test
    void assignCar_shouldHandleDifferentIdCombinations() throws Exception {
        // Given
        int ownerId = 7;
        int carId = 3;
        doNothing().when(ownerService).assignCarToOwner(ownerId, carId);

        // When & Then
        mockMvc.perform(post("/api/v1/owners/{id}/cars/{carId}", ownerId, carId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(ownerService, times(1)).assignCarToOwner(7, 3);
    }
}

