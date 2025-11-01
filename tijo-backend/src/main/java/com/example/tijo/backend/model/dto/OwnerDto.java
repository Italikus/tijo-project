package com.example.tijo.backend.model.dto;

import com.example.tijo.backend.model.Owner;

public record OwnerDto(int id, String name, String surname, int ownedCars) {
    public static OwnerDto toDto(Owner owner) {
        return new OwnerDto(
                owner.getId(),
                owner.getName(),
                owner.getSurname(),
                owner.getCars() == null ? 0 : owner.getCars().size()
        );
    }
}
