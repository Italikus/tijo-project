package com.example.tijo.backend.controller;

import com.example.tijo.backend.model.command.CreateOwnerCommand;
import com.example.tijo.backend.model.dto.OwnerDto;
import com.example.tijo.backend.service.OwnerService;
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
@RequestMapping("/api/v1/owners")
@RequiredArgsConstructor
@Tag(name = "Owners", description = "API do zarządzania właścicielami pojazdów")
public class OwnerController {
    private final OwnerService ownerService;

    @Operation(
            summary = "Pobierz listę wszystkich właścicieli",
            description = """
                    Zwraca stronicowaną listę wszystkich właścicieli w systemie.
                    
                    Możliwe parametry paginacji:
                    - page: numer strony (domyślnie 0)
                    - size: rozmiar strony (domyślnie 10)
                    - sort: sortowanie (np. surname,asc lub name,desc)
                    
                    Przykład: /api/v1/owners?page=0&size=10&sort=surname,asc
                    
                    W odpowiedzi zwracana jest liczba posiadanych pojazdów dla każdego właściciela.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista właścicieli pobrana pomyślnie",
                    content = @Content(schema = @Schema(implementation = Page.class))
            )
    })
    @GetMapping
    public ResponseEntity<Page<OwnerDto>> findAllOwners(
            @Parameter(description = "Parametry paginacji i sortowania")
            @PageableDefault Pageable pageable
    ) {
        return ResponseEntity.ok(ownerService.findAll(pageable));
    }

    @Operation(
            summary = "Dodaj nowego właściciela",
            description = """
                    Tworzy nowego właściciela w systemie.
                    
                    Wymagane pola:
                    - name: imię właściciela (nie może być puste)
                    - surname: nazwisko właściciela (nie może być puste)
                    - pesel: numer PESEL (musi być prawidłowy i zawierać 11 cyfr)
                    
                    PESEL jest walidowany za pomocą algorytmu sumy kontrolnej:
                    - musi składać się z 11 cyfr
                    - ostatnia cyfra musi być prawidłową cyfrą kontrolną
                    - cyfra kontrolna jest obliczana na podstawie wag [1, 3, 7, 9, 1, 3, 7, 9, 1, 3]
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Właściciel został pomyślnie utworzony",
                    content = @Content(schema = @Schema(implementation = OwnerDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Błędne dane wejściowe (np. nieprawidłowy PESEL lub puste pola)",
                    content = @Content
            )
    })
    @PostMapping
    public ResponseEntity<OwnerDto> addOwner(
            @Parameter(description = "Dane nowego właściciela", required = true)
            @Valid @RequestBody CreateOwnerCommand command
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ownerService.saveOwner(command));
    }

    @Operation(
            summary = "Usuń właściciela",
            description = """
                    Usuwa właściciela z systemu na podstawie podanego ID.
                    
                    Uwaga: Operacja jest nieodwracalna. Usunięcie właściciela nie usuwa jego pojazdów,
                    ale rozwiązuje powiązania między właścicielem a pojazdami.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Właściciel został pomyślnie usunięty"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Właściciel o podanym ID nie istnieje",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOwner(
            @Parameter(description = "ID właściciela do usunięcia", required = true, example = "1")
            @PathVariable int id
    ) {
        ownerService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(
            summary = "Przypisz pojazd do właściciela",
            description = """
                    Tworzy powiązanie między właścicielem a pojazdem.
                    
                    Operacja ta:
                    - Przypisuje pojazd o podanym carId do właściciela o podanym id
                    - Jeden pojazd może mieć tylko jednego właściciela
                    - Jeden właściciel może mieć wiele pojazdów
                    
                    Jeśli pojazd był już przypisany do innego właściciela, zostanie przepisany
                    do nowego właściciela.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Pojazd został pomyślnie przypisany do właściciela"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Właściciel lub pojazd o podanym ID nie istnieje",
                    content = @Content
            )
    })
    @PostMapping("/{id}/cars/{carId}")
    public ResponseEntity<Void> assignCar(
            @Parameter(description = "ID właściciela", required = true, example = "1")
            @PathVariable int id,
            @Parameter(description = "ID pojazdu do przypisania", required = true, example = "1")
            @PathVariable("carId") int carId
    ) {
        ownerService.assignCarToOwner(id, carId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

