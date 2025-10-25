package com.example.tijo.backend.controller;

import com.example.tijo.backend.model.CreateCarCommand;
import com.example.tijo.backend.model.command.EditCarCommand;
import com.example.tijo.backend.model.dto.CarDto;
import com.example.tijo.backend.service.CarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cars")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;

    @GetMapping
    public ResponseEntity<Page<CarDto>> findAllCars(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(carService.findAll(pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable int id) {
        carService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping
    public ResponseEntity<CarDto> addCar(@Valid @RequestBody CreateCarCommand command) {
        return ResponseEntity.status(HttpStatus.CREATED).body(carService.saveCar(command));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarDto> addCar(@PathVariable int id, @RequestBody EditCarCommand command) {
        if (id != command.getId()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(carService.editCar(command));
    }
}
