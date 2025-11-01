package com.example.tijo.backend.model.dto;

import com.example.tijo.backend.model.Car;

public record CarDto(int id, String brand, String model, int horsePower, int year) {
    public static CarDto toDto(Car car) {
        return new CarDto(
                car.getId(),
                car.getBrand(),
                car.getModel(),
                car.getHorsePower(),
                car.getProductionYear()
        );
    }
}
