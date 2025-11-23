package com.example.tijo.backend.controller;

import com.example.tijo.backend.model.command.CreateCarCommand;
import com.example.tijo.backend.model.command.EditCarCommand;
import com.example.tijo.backend.model.dto.CarDto;
import com.example.tijo.backend.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Cars", description = "API do zarządzania pojazdami")
public class CarController {
    private final CarService carService;

    @Operation(
            summary = "Pobierz listę wszystkich pojazdów",
            description = """
                    Zwraca stronicowaną listę wszystkich pojazdów w systemie.
                    
                    Możliwe parametry paginacji:
                    - page: numer strony (domyślnie 0)
                    - size: rozmiar strony (domyślnie 10)
                    - sort: sortowanie (np. brand,asc lub year,desc)
                    
                    Przykład: /api/v1/cars?page=0&size=10&sort=brand,asc
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista pojazdów pobrana pomyślnie",
                    content = @Content(schema = @Schema(implementation = Page.class))
            )
    })
    @GetMapping
    public ResponseEntity<Page<CarDto>> findAllCars(
            @Parameter(description = "Parametry paginacji i sortowania")
            @PageableDefault Pageable pageable
    ) {
        return ResponseEntity.ok(carService.findAll(pageable));
    }

    @Operation(
            summary = "Usuń pojazd",
            description = "Usuwa pojazd z systemu na podstawie podanego ID. Operacja jest nieodwracalna."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Pojazd został pomyślnie usunięty"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pojazd o podanym ID nie istnieje",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(
            @Parameter(description = "ID pojazdu do usunięcia", required = true, example = "1")
            @PathVariable int id
    ) {
        carService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(
            summary = "Dodaj nowy pojazd",
            description = """
                    Tworzy nowy pojazd w systemie.
                    
                    Wymagane pola:
                    - brand: marka pojazdu
                    - model: model pojazdu
                    - vin: numer VIN (musi być zgodny z wzorcem: 17 znaków alfanumerycznych)
                    - horsePower: moc silnika (w KM)
                    - productionYear: rok produkcji
                    
                    VIN jest walidowany zgodnie z międzynarodowym standardem (17 znaków).
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Pojazd został pomyślnie utworzony",
                    content = @Content(schema = @Schema(implementation = CarDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Błędne dane wejściowe (np. nieprawidłowy VIN)",
                    content = @Content
            )
    })
    @PostMapping
    public ResponseEntity<CarDto> addCar(
            @Parameter(description = "Dane nowego pojazdu", required = true)
            @Valid @RequestBody CreateCarCommand command
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(carService.saveCar(command));
    }

    @Operation(
            summary = "Edytuj pojazd",
            description = """
                    Aktualizuje dane istniejącego pojazdu.
                    
                    Można zaktualizować:
                    - horsePower: moc silnika
                    - ownerId: ID właściciela (opcjonalne, null jeśli pojazd nie ma właściciela)
                    
                    ID w ścieżce musi być zgodne z ID w ciele zapytania.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Pojazd został pomyślnie zaktualizowany",
                    content = @Content(schema = @Schema(implementation = CarDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "ID w ścieżce nie zgadza się z ID w body lub błędne dane",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pojazd o podanym ID nie istnieje",
                    content = @Content
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<CarDto> updateCar(
            @Parameter(description = "ID pojazdu do edycji", required = true, example = "1")
            @PathVariable int id,
            @Parameter(description = "Zaktualizowane dane pojazdu", required = true)
            @RequestBody EditCarCommand command
    ) {
        if (id != command.getId()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(carService.editCar(command));
    }
}
