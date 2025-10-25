package com.example.tijo.backend.repository;

import com.example.tijo.backend.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Integer> {
}
