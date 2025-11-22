package com.example.tijo.backend.model.dto;

import com.example.tijo.backend.model.Car;
import com.example.tijo.backend.model.Owner;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OwnerDtoTest {

    @Test
    void toDto_shouldMapOwnerWithNoCars_correctly() {
        // Given
        Owner owner = new Owner("Jan", "Kowalski", "12345678901");
        owner.setId(1);

        // When
        OwnerDto result = OwnerDto.toDto(owner);

        // Then
        assertNotNull(result);
        assertEquals(1, result.id());
        assertEquals("Jan", result.name());
        assertEquals("Kowalski", result.surname());
        assertEquals(0, result.ownedCars());
    }

    @Test
    void toDto_shouldMapOwnerWithMultipleCars_correctly() {
        // Given
        Owner owner = new Owner("Anna", "Nowak", "98765432109");
        owner.setId(2);

        Car car1 = new Car("Toyota", "Corolla", "VIN123", 120, 2020);
        Car car2 = new Car("Honda", "Civic", "VIN456", 150, 2021);
        Car car3 = new Car("Ford", "Focus", "VIN789", 100, 2019);

        owner.addCar(car1);
        owner.addCar(car2);
        owner.addCar(car3);

        // When
        OwnerDto result = OwnerDto.toDto(owner);

        // Then
        assertNotNull(result);
        assertEquals(2, result.id());
        assertEquals("Anna", result.name());
        assertEquals("Nowak", result.surname());
        assertEquals(3, result.ownedCars());
    }
}

