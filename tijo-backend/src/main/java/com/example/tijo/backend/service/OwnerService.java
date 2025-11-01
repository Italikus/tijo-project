package com.example.tijo.backend.service;

import com.example.tijo.backend.model.Owner;
import com.example.tijo.backend.model.command.CreateOwnerCommand;
import com.example.tijo.backend.model.dto.OwnerDto;
import com.example.tijo.backend.repository.CarRepository;
import com.example.tijo.backend.repository.OwnerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OwnerService {
    private final OwnerRepository ownerRepository;
    private final CarRepository carRepository;

    @Transactional(readOnly = true)
    public Page<OwnerDto> findAll(Pageable pageable) {
        return ownerRepository.findAll(pageable).map(OwnerDto::toDto);
    }

    @Transactional
    public OwnerDto saveOwner(CreateOwnerCommand command) {
        Owner owner = new Owner(command.getName(), command.getSurname(), command.getPesel());
        return OwnerDto.toDto(ownerRepository.saveAndFlush(owner));
    }

    @Transactional
    public void deleteById(int id) {
        ownerRepository.deleteById(id);
    }

    @Transactional
    public void assignCarToOwner(int ownerId, int carId) {
        var owner = ownerRepository.findById(ownerId).orElseThrow(EntityNotFoundException::new);
        var car = carRepository.findById(carId).orElseThrow(EntityNotFoundException::new);
        owner.addCar(car);
        ownerRepository.saveAndFlush(owner);
    }
}
