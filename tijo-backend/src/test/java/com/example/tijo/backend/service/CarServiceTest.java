package com.example.tijo.backend.service;

import com.example.tijo.backend.model.Car;
import com.example.tijo.backend.model.Owner;
import com.example.tijo.backend.model.command.CreateCarCommand;
import com.example.tijo.backend.model.command.EditCarCommand;
import com.example.tijo.backend.model.dto.CarDto;
import com.example.tijo.backend.repository.CarRepository;
import com.example.tijo.backend.repository.OwnerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private OwnerRepository ownerRepository;

    @InjectMocks
    private CarService carService;

    private List<Car> testCars;

    @BeforeEach
    void setUp() {
        testCars = new ArrayList<>();
        testCars.add(new Car("Toyota", "Corolla", "VIN001", 120, 2015));
        testCars.add(new Car("BMW", "X5", "VIN002", 250, 2020));
        testCars.add(new Car("Honda", "Civic", "VIN003", 150, 2018));
        testCars.add(new Car("Toyota", "Camry", "VIN004", 180, 2019));
        testCars.add(new Car("Ferrari", "488", "VIN005", 670, 2021));
        testCars.add(new Car("BMW", "M3", "VIN006", 390, 2022));
    }

    // Testy dla findCarsByYearLessThan

    @Test
    void findCarsByYearLessThan_shouldReturnCarsWithYearLessThanGiven() {
        // When
        List<Car> result = carService.findCarsByYearLessThan(testCars, 2019);

        // Then
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(car -> car.getProductionYear() < 2019));
        assertTrue(result.stream().anyMatch(car -> car.getVin().equals("VIN001")));
        assertTrue(result.stream().anyMatch(car -> car.getVin().equals("VIN003")));
    }

    @Test
    void findCarsByYearLessThan_shouldReturnEmptyList_whenNoCarsMeetCriteria() {
        // When
        List<Car> result = carService.findCarsByYearLessThan(testCars, 2010);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void findCarsByYearLessThan_shouldReturnEmptyList_whenInputListIsNull() {
        // When
        List<Car> result = carService.findCarsByYearLessThan(null, 2020);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findCarsByYearLessThan_shouldReturnEmptyList_whenInputListIsEmpty() {
        // When
        List<Car> result = carService.findCarsByYearLessThan(new ArrayList<>(), 2020);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findCarsByYearLessThan_shouldReturnAllCars_whenYearIsGreaterThanAll() {
        // When
        List<Car> result = carService.findCarsByYearLessThan(testCars, 2025);

        // Then
        assertEquals(6, result.size());
    }

    // Testy dla findCarsByYearGreaterThan

    @Test
    void findCarsByYearGreaterThan_shouldReturnCarsWithYearGreaterThanGiven() {
        // When
        List<Car> result = carService.findCarsByYearGreaterThan(testCars, 2019);

        // Then
        assertEquals(3, result.size());
        assertTrue(result.stream().allMatch(car -> car.getProductionYear() > 2019));
        assertTrue(result.stream().anyMatch(car -> car.getVin().equals("VIN002")));
        assertTrue(result.stream().anyMatch(car -> car.getVin().equals("VIN005")));
        assertTrue(result.stream().anyMatch(car -> car.getVin().equals("VIN006")));
    }

    @Test
    void findCarsByYearGreaterThan_shouldReturnEmptyList_whenNoCarsMeetCriteria() {
        // When
        List<Car> result = carService.findCarsByYearGreaterThan(testCars, 2025);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void findCarsByYearGreaterThan_shouldReturnEmptyList_whenInputListIsNull() {
        // When
        List<Car> result = carService.findCarsByYearGreaterThan(null, 2020);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findCarsByYearGreaterThan_shouldReturnEmptyList_whenInputListIsEmpty() {
        // When
        List<Car> result = carService.findCarsByYearGreaterThan(new ArrayList<>(), 2020);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findCarsByYearGreaterThan_shouldReturnAllCars_whenYearIsLessThanAll() {
        // When
        List<Car> result = carService.findCarsByYearGreaterThan(testCars, 2010);

        // Then
        assertEquals(6, result.size());
    }

    // Testy dla findCarsByBrand

    @Test
    void findCarsByBrand_shouldReturnCarsWithSpecificBrand() {
        // When
        List<Car> result = carService.findCarsByBrand(testCars, "Toyota");

        // Then
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(car -> car.getBrand().equals("Toyota")));
    }

    @Test
    void findCarsByBrand_shouldReturnEmptyList_whenNoCarsWithBrand() {
        // When
        List<Car> result = carService.findCarsByBrand(testCars, "Mercedes");

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void findCarsByBrand_shouldReturnEmptyList_whenInputListIsNull() {
        // When
        List<Car> result = carService.findCarsByBrand(null, "Toyota");

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findCarsByBrand_shouldReturnEmptyList_whenInputListIsEmpty() {
        // When
        List<Car> result = carService.findCarsByBrand(new ArrayList<>(), "Toyota");

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findCarsByBrand_shouldReturnMultipleCarsWithSameBrand() {
        // When
        List<Car> result = carService.findCarsByBrand(testCars, "BMW");

        // Then
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(car -> car.getBrand().equals("BMW")));
    }

    // Testy dla classifyCarsByHorsePower

    @Test
    void classifyCarsByHorsePower_shouldClassifyCarsIntoThreeCategories() {
        // When
        Map<String, List<Car>> result = carService.classifyCarsByHorsePower(testCars);

        // Then
        assertEquals(3, result.size());
        assertTrue(result.containsKey("slow"));
        assertTrue(result.containsKey("medium"));
        assertTrue(result.containsKey("fast"));
    }

    @Test
    void classifyCarsByHorsePower_shouldClassifySlowCarsCorrectly() {
        // When
        Map<String, List<Car>> result = carService.classifyCarsByHorsePower(testCars);

        // Then
        List<Car> slowCars = result.get("slow");
        assertEquals(3, slowCars.size());
        assertTrue(slowCars.stream().allMatch(car -> car.getHorsePower() < 200));
        assertTrue(slowCars.stream().anyMatch(car -> car.getBrand().equals("Toyota") && car.getModel().equals("Corolla")));
    }

    @Test
    void classifyCarsByHorsePower_shouldClassifyMediumCarsCorrectly() {
        // When
        Map<String, List<Car>> result = carService.classifyCarsByHorsePower(testCars);

        // Then
        List<Car> mediumCars = result.get("medium");
        assertEquals(2, mediumCars.size());
        assertTrue(mediumCars.stream().allMatch(car -> car.getHorsePower() >= 200 && car.getHorsePower() < 400));
    }

    @Test
    void classifyCarsByHorsePower_shouldClassifyFastCarsCorrectly() {
        // When
        Map<String, List<Car>> result = carService.classifyCarsByHorsePower(testCars);

        // Then
        List<Car> fastCars = result.get("fast");
        assertEquals(1, fastCars.size());
        assertTrue(fastCars.stream().allMatch(car -> car.getHorsePower() >= 400));
        assertEquals("Ferrari", fastCars.get(0).getBrand());
    }

    @Test
    void classifyCarsByHorsePower_shouldReturnEmptyMap_whenInputListIsNull() {
        // When
        Map<String, List<Car>> result = carService.classifyCarsByHorsePower(null);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void classifyCarsByHorsePower_shouldReturnEmptyMap_whenInputListIsEmpty() {
        // When
        Map<String, List<Car>> result = carService.classifyCarsByHorsePower(new ArrayList<>());

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void classifyCarsByHorsePower_shouldHandleBoundaryValues() {
        // Given
        List<Car> boundaryCars = new ArrayList<>();
        boundaryCars.add(new Car("Car1", "Model1", "VIN101", 199, 2020)); // slow
        boundaryCars.add(new Car("Car2", "Model2", "VIN102", 200, 2020)); // medium
        boundaryCars.add(new Car("Car3", "Model3", "VIN103", 399, 2020)); // medium
        boundaryCars.add(new Car("Car4", "Model4", "VIN104", 400, 2020)); // fast

        // When
        Map<String, List<Car>> result = carService.classifyCarsByHorsePower(boundaryCars);

        // Then
        assertEquals(1, result.get("slow").size());
        assertEquals(2, result.get("medium").size());
        assertEquals(1, result.get("fast").size());
    }

    @Test
    void classifyCarsByHorsePower_shouldHandleOnlySlowCars() {
        // Given
        List<Car> slowCars = new ArrayList<>();
        slowCars.add(new Car("Car1", "Model1", "VIN201", 100, 2020));
        slowCars.add(new Car("Car2", "Model2", "VIN202", 150, 2020));

        // When
        Map<String, List<Car>> result = carService.classifyCarsByHorsePower(slowCars);

        // Then
        assertEquals(1, result.size());
        assertTrue(result.containsKey("slow"));
        assertEquals(2, result.get("slow").size());
    }

    // Testy dla metod @Transactional

    // Testy dla findAll

    @Test
    void findAll_shouldReturnPageOfCarDtos() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<Car> cars = List.of(
                new Car("Toyota", "Corolla", "VIN001", 120, 2015),
                new Car("BMW", "X5", "VIN002", 250, 2020)
        );
        Page<Car> carPage = new PageImpl<>(cars, pageable, cars.size());
        when(carRepository.findAll(pageable)).thenReturn(carPage);

        // When
        Page<CarDto> result = carService.findAll(pageable);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals("Toyota", result.getContent().get(0).brand());
        assertEquals("BMW", result.getContent().get(1).brand());
        verify(carRepository, times(1)).findAll(pageable);
    }

    @Test
    void findAll_shouldReturnEmptyPage_whenNoCarsExist() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Car> emptyPage = new PageImpl<>(List.of(), pageable, 0);
        when(carRepository.findAll(pageable)).thenReturn(emptyPage);

        // When
        Page<CarDto> result = carService.findAll(pageable);

        // Then
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
        verify(carRepository, times(1)).findAll(pageable);
    }

    @Test
    void findAll_shouldHandlePagination() {
        // Given
        Pageable pageable = PageRequest.of(1, 2);
        List<Car> cars = List.of(
                new Car("Honda", "Civic", "VIN003", 150, 2018),
                new Car("Toyota", "Camry", "VIN004", 180, 2019)
        );
        Page<Car> carPage = new PageImpl<>(cars, pageable, 10);
        when(carRepository.findAll(pageable)).thenReturn(carPage);

        // When
        Page<CarDto> result = carService.findAll(pageable);

        // Then
        assertNotNull(result);
        assertEquals(10, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals(1, result.getNumber());
        verify(carRepository, times(1)).findAll(pageable);
    }

    // Testy dla deleteById

    @Test
    void deleteById_shouldDeleteCar() {
        // Given
        int carId = 1;
        doNothing().when(carRepository).deleteById(carId);

        // When
        carService.deleteById(carId);

        // Then
        verify(carRepository, times(1)).deleteById(carId);
    }

    @Test
    void deleteById_shouldCallRepositoryWithCorrectId() {
        // Given
        int carId = 999;
        doNothing().when(carRepository).deleteById(carId);

        // When
        carService.deleteById(carId);

        // Then
        verify(carRepository, times(1)).deleteById(999);
    }

    // Testy dla saveCar

    @Test
    void saveCar_shouldCreateAndSaveNewCar() {
        // Given
        CreateCarCommand command = new CreateCarCommand();
        command.setBrand("Audi");
        command.setModel("A4");
        command.setVin("WAUZZZ8K0DA123456");
        command.setHorsePower(190);
        command.setProductionYear(2021);

        Car savedCar = new Car("Audi", "A4", "WAUZZZ8K0DA123456", 190, 2021);
        savedCar.setId(1);

        when(carRepository.saveAndFlush(any(Car.class))).thenReturn(savedCar);

        // When
        CarDto result = carService.saveCar(command);

        // Then
        assertNotNull(result);
        assertEquals(1, result.id());
        assertEquals("Audi", result.brand());
        assertEquals("A4", result.model());
        assertEquals(190, result.horsePower());
        assertEquals(2021, result.year());
        verify(carRepository, times(1)).saveAndFlush(any(Car.class));
    }

    @Test
    void saveCar_shouldMapAllFieldsFromCommand() {
        // Given
        CreateCarCommand command = new CreateCarCommand();
        command.setBrand("Mercedes");
        command.setModel("C-Class");
        command.setVin("WDB2040061F123456");
        command.setHorsePower(255);
        command.setProductionYear(2022);

        Car savedCar = new Car("Mercedes", "C-Class", "WDB2040061F123456", 255, 2022);
        savedCar.setId(2);

        when(carRepository.saveAndFlush(any(Car.class))).thenReturn(savedCar);

        // When
        CarDto result = carService.saveCar(command);

        // Then
        assertNotNull(result);
        assertEquals("Mercedes", result.brand());
        assertEquals("C-Class", result.model());
        assertEquals(255, result.horsePower());
        assertEquals(2022, result.year());
    }

    @Test
    void saveCar_shouldReturnCarDtoWithGeneratedId() {
        // Given
        CreateCarCommand command = new CreateCarCommand();
        command.setBrand("Ford");
        command.setModel("Mustang");
        command.setVin("1FA6P8CF5H5123456");
        command.setHorsePower(450);
        command.setProductionYear(2023);

        Car savedCar = new Car("Ford", "Mustang", "1FA6P8CF5H5123456", 450, 2023);
        savedCar.setId(100);

        when(carRepository.saveAndFlush(any(Car.class))).thenReturn(savedCar);

        // When
        CarDto result = carService.saveCar(command);

        // Then
        assertEquals(100, result.id());
        verify(carRepository, times(1)).saveAndFlush(any(Car.class));
    }

    // Testy dla editCar

    @Test
    void editCar_shouldUpdateHorsePowerOnly_whenOwnerIdIsNull() {
        // Given
        EditCarCommand command = new EditCarCommand();
        command.setId(1);
        command.setHorsePower(200);
        command.setOwnerId(null);

        Car existingCar = new Car("Toyota", "Corolla", "VIN001", 120, 2015);
        existingCar.setId(1);

        Car updatedCar = new Car("Toyota", "Corolla", "VIN001", 200, 2015);
        updatedCar.setId(1);

        when(carRepository.findById(1)).thenReturn(Optional.of(existingCar));
        when(carRepository.saveAndFlush(existingCar)).thenReturn(updatedCar);

        // When
        CarDto result = carService.editCar(command);

        // Then
        assertNotNull(result);
        assertEquals(1, result.id());
        assertEquals(200, result.horsePower());
        verify(carRepository, times(1)).findById(1);
        verify(carRepository, times(1)).saveAndFlush(existingCar);
        verify(ownerRepository, never()).findById(anyInt());
    }

    @Test
    void editCar_shouldUpdateHorsePowerAndOwner_whenOwnerIdIsProvided() {
        // Given
        EditCarCommand command = new EditCarCommand();
        command.setId(1);
        command.setHorsePower(300);
        command.setOwnerId(5);

        Car existingCar = new Car("BMW", "X5", "VIN002", 250, 2020);
        existingCar.setId(1);

        Owner owner = new Owner("Jan", "Kowalski", "12345678901");
        owner.setId(5);

        Car updatedCar = new Car("BMW", "X5", "VIN002", 300, 2020);
        updatedCar.setId(1);
        updatedCar.setOwner(owner);

        when(carRepository.findById(1)).thenReturn(Optional.of(existingCar));
        when(ownerRepository.findById(5)).thenReturn(Optional.of(owner));
        when(carRepository.saveAndFlush(existingCar)).thenReturn(updatedCar);

        // When
        CarDto result = carService.editCar(command);

        // Then
        assertNotNull(result);
        assertEquals(1, result.id());
        assertEquals(300, result.horsePower());
        verify(carRepository, times(1)).findById(1);
        verify(ownerRepository, times(1)).findById(5);
        verify(carRepository, times(1)).saveAndFlush(existingCar);
    }

    @Test
    void editCar_shouldThrowException_whenCarNotFound() {
        // Given
        EditCarCommand command = new EditCarCommand();
        command.setId(999);
        command.setHorsePower(200);

        when(carRepository.findById(999)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> carService.editCar(command));
        verify(carRepository, times(1)).findById(999);
        verify(carRepository, never()).saveAndFlush(any(Car.class));
    }

    @Test
    void editCar_shouldThrowException_whenOwnerNotFound() {
        // Given
        EditCarCommand command = new EditCarCommand();
        command.setId(1);
        command.setHorsePower(200);
        command.setOwnerId(999);

        Car existingCar = new Car("Toyota", "Corolla", "VIN001", 120, 2015);
        existingCar.setId(1);

        when(carRepository.findById(1)).thenReturn(Optional.of(existingCar));
        when(ownerRepository.findById(999)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> carService.editCar(command));
        verify(carRepository, times(1)).findById(1);
        verify(ownerRepository, times(1)).findById(999);
        verify(carRepository, never()).saveAndFlush(any(Car.class));
    }

    @Test
    void editCar_shouldOnlyUpdateHorsePower_notOtherFields() {
        // Given
        EditCarCommand command = new EditCarCommand();
        command.setId(1);
        command.setHorsePower(350);
        command.setOwnerId(null);

        Car existingCar = new Car("Honda", "Civic", "VIN003", 150, 2018);
        existingCar.setId(1);

        when(carRepository.findById(1)).thenReturn(Optional.of(existingCar));
        when(carRepository.saveAndFlush(existingCar)).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        CarDto result = carService.editCar(command);

        // Then
        assertEquals("Honda", result.brand());
        assertEquals("Civic", result.model());
        assertEquals(2018, result.year());
        assertEquals(350, result.horsePower());
    }
}

