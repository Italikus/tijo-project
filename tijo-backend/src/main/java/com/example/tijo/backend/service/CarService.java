package com.example.tijo.backend.service;

import com.example.tijo.backend.model.Car;
import com.example.tijo.backend.model.dto.CarDto;
import com.example.tijo.backend.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarService {
    private final CarRepository carRepository;

    @Transactional(readOnly = true)
    public Page<CarDto> findAll(Pageable pageable) {
        return carRepository.findAll(pageable).map(CarDto::toDto);
    }

    @Transactional
    public void deleteById(int id) {
        carRepository.deleteById(id);
    }

    public List<Car> findCarsByYearLessThan(List<Car> cars, int year) {
        if (cars == null || cars.isEmpty()) {
            return List.of();
        }

        return cars.stream()
                .filter(c -> c.getProductionYear() < year)
                .toList();
    }

    public List<Car> findCarsByYearGreaterThan(List<Car> cars, int year) {
        if (cars == null || cars.isEmpty()) {
            return List.of();
        }

        return cars.stream()
                .filter(c -> c.getProductionYear() > year)
                .toList();
    }

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
