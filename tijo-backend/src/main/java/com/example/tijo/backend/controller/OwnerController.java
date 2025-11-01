package com.example.tijo.backend.controller;

import com.example.tijo.backend.model.command.CreateOwnerCommand;
import com.example.tijo.backend.model.dto.OwnerDto;
import com.example.tijo.backend.service.OwnerService;
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
public class OwnerController {
    private final OwnerService ownerService;

    @GetMapping
    public ResponseEntity<Page<OwnerDto>> findAllOwners(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(ownerService.findAll(pageable));
    }

    @PostMapping
    public ResponseEntity<OwnerDto> addOwner(@Valid @RequestBody CreateOwnerCommand command) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ownerService.saveOwner(command));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOwner(@PathVariable int id) {
        ownerService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/{id}/cars/{carId}")
    public ResponseEntity<Void> assignCar(@PathVariable int id, @PathVariable("carId") int carId) {
        ownerService.assignCarToOwner(id, carId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

