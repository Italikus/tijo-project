package com.example.tijo.backend.controller;

import com.example.tijo.backend.model.dto.CarDto;
import com.example.tijo.backend.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cars")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;

    @GetMapping
    public Page<CarDto> findAllCars(@PageableDefault Pageable pageable) {
        return carService.findAll(pageable);
    }
}
