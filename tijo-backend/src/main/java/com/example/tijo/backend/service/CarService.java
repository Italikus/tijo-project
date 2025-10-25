package com.example.tijo.backend.service;

import com.example.tijo.backend.model.Car;
import com.example.tijo.backend.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarService {
    private final CarRepository carRepository;

    public List<Car> findCarsByBrand(List<Car> cars, String brand) {
        if (cars == null || cars.isEmpty()) {
            return List.of();
        }

        return cars.stream()
                .filter(c -> c.getBrand().equals(brand))
                .toList();
    }

    public Map<String, List<Car>> classifyCarsByHorsePower(List<Car> cars) {
        if (cars == null || cars.isEmpty()) {
            return Map.of();
        }

        return cars.stream()
                .collect(Collectors.groupingBy(this::getCarHorsePowerCategory));
    }

    private String getCarHorsePowerCategory(Car car) {
        int hp = car.getHorsePower();

        if (hp < 200) {
            return "slow";
        } else if (hp < 400) {
            return "medium";
        } else {
            return "fast";
        }
    }
}
