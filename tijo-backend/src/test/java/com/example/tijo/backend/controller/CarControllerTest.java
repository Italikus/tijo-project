package com.example.tijo.backend.controller;

import com.example.tijo.backend.model.command.CreateCarCommand;
import com.example.tijo.backend.model.command.EditCarCommand;
import com.example.tijo.backend.model.dto.CarDto;
import com.example.tijo.backend.service.CarService;
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

@WebMvcTest(CarController.class)
class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CarService carService;

    @Test
    void findAllCars_shouldReturnPageOfCars() throws Exception {
        // Given
        CarDto car1 = new CarDto(1, "Toyota", "Corolla", 120, 2015);
        CarDto car2 = new CarDto(2, "BMW", "X5", 250, 2020);
        Page<CarDto> page = new PageImpl<>(List.of(car1, car2), PageRequest.of(0, 10), 2);

        when(carService.findAll(any())).thenReturn(page);

        // When & Then
        mockMvc.perform(get("/api/v1/cars")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].brand", is("Toyota")))
                .andExpect(jsonPath("$.content[0].model", is("Corolla")))
                .andExpect(jsonPath("$.content[0].horsePower", is(120)))
                .andExpect(jsonPath("$.content[0].year", is(2015)))
                .andExpect(jsonPath("$.content[1].brand", is("BMW")))
                .andExpect(jsonPath("$.totalElements", is(2)));

        verify(carService, times(1)).findAll(any());
    }

    @Test
    void findAllCars_shouldReturnEmptyPage_whenNoCars() throws Exception {
        // Given
        Page<CarDto> emptyPage = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);
        when(carService.findAll(any())).thenReturn(emptyPage);

        // When & Then
        mockMvc.perform(get("/api/v1/cars")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)))
                .andExpect(jsonPath("$.totalElements", is(0)));

        verify(carService, times(1)).findAll(any());
    }

    @Test
    void findAllCars_shouldHandlePagination() throws Exception {
        // Given
        CarDto car1 = new CarDto(3, "Honda", "Civic", 150, 2018);
        Page<CarDto> page = new PageImpl<>(List.of(car1), PageRequest.of(1, 5), 10);

        when(carService.findAll(any())).thenReturn(page);

        // When & Then
        mockMvc.perform(get("/api/v1/cars")
                        .param("page", "1")
                        .param("size", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.totalElements", is(10)))
                .andExpect(jsonPath("$.number", is(1)))
                .andExpect(jsonPath("$.size", is(5)));
    }

    @Test
    void addCar_shouldCreateNewCar_whenValidCommand() throws Exception {
        // Given
        CreateCarCommand command = new CreateCarCommand();
        command.setBrand("Audi");
        command.setModel("A4");
        command.setVin("WAUZZZ8K0DA123456");
        command.setHorsePower(190);
        command.setProductionYear(2021);

        CarDto createdCar = new CarDto(1, "Audi", "A4", 190, 2021);
        when(carService.saveCar(any(CreateCarCommand.class))).thenReturn(createdCar);

        // When & Then
        mockMvc.perform(post("/api/v1/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.brand", is("Audi")))
                .andExpect(jsonPath("$.model", is("A4")))
                .andExpect(jsonPath("$.horsePower", is(190)))
                .andExpect(jsonPath("$.year", is(2021)));

        verify(carService, times(1)).saveCar(any(CreateCarCommand.class));
    }

    @Test
    void addCar_shouldReturnBadRequest_whenVinIsNull() throws Exception {
        // Given
        CreateCarCommand command = new CreateCarCommand();
        command.setBrand("Audi");
        command.setModel("A4");
        command.setVin(null);
        command.setHorsePower(190);
        command.setProductionYear(2021);

        // When & Then
        mockMvc.perform(post("/api/v1/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest());

        verify(carService, never()).saveCar(any(CreateCarCommand.class));
    }

    @Test
    void addCar_shouldReturnBadRequest_whenVinIsInvalid() throws Exception {
        // Given
        CreateCarCommand command = new CreateCarCommand();
        command.setBrand("Audi");
        command.setModel("A4");
        command.setVin("INVALID");
        command.setHorsePower(190);
        command.setProductionYear(2021);

        // When & Then
        mockMvc.perform(post("/api/v1/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest());

        verify(carService, never()).saveCar(any(CreateCarCommand.class));
    }

    @Test
    void deleteCar_shouldDeleteCar_whenValidId() throws Exception {
        // Given
        int carId = 1;
        doNothing().when(carService).deleteById(carId);

        // When & Then
        mockMvc.perform(delete("/api/v1/cars/{id}", carId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(carService, times(1)).deleteById(carId);
    }

    @Test
    void deleteCar_shouldCallServiceWithCorrectId() throws Exception {
        // Given
        int carId = 999;
        doNothing().when(carService).deleteById(carId);

        // When & Then
        mockMvc.perform(delete("/api/v1/cars/{id}", carId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(carService, times(1)).deleteById(999);
    }

    @Test
    void editCar_shouldUpdateCar_whenIdMatches() throws Exception {
        // Given
        int carId = 1;
        EditCarCommand command = new EditCarCommand();
        command.setId(carId);
        command.setHorsePower(200);
        command.setOwnerId(null);

        CarDto updatedCar = new CarDto(1, "Toyota", "Corolla", 200, 2015);
        when(carService.editCar(any(EditCarCommand.class))).thenReturn(updatedCar);

        // When & Then
        mockMvc.perform(put("/api/v1/cars/{id}", carId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.horsePower", is(200)));

        verify(carService, times(1)).editCar(any(EditCarCommand.class));
    }

    @Test
    void editCar_shouldReturnBadRequest_whenIdDoesNotMatch() throws Exception {
        // Given
        int pathId = 1;
        EditCarCommand command = new EditCarCommand();
        command.setId(2); // różne ID
        command.setHorsePower(200);

        // When & Then
        mockMvc.perform(put("/api/v1/cars/{id}", pathId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest());

        verify(carService, never()).editCar(any(EditCarCommand.class));
    }

    @Test
    void editCar_shouldUpdateCarWithOwner_whenOwnerIdProvided() throws Exception {
        // Given
        int carId = 1;
        EditCarCommand command = new EditCarCommand();
        command.setId(carId);
        command.setHorsePower(300);
        command.setOwnerId(5);

        CarDto updatedCar = new CarDto(1, "BMW", "X5", 300, 2020);
        when(carService.editCar(any(EditCarCommand.class))).thenReturn(updatedCar);

        // When & Then
        mockMvc.perform(put("/api/v1/cars/{id}", carId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.horsePower", is(300)));

        verify(carService, times(1)).editCar(any(EditCarCommand.class));
    }

    @Test
    void editCar_shouldReturnBadRequest_whenCommandBodyIsEmpty() throws Exception {
        // When & Then
        mockMvc.perform(put("/api/v1/cars/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());

        verify(carService, never()).editCar(any(EditCarCommand.class));
    }
}

