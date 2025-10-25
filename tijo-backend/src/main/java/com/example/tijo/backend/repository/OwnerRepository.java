package com.example.tijo.backend.repository;

import com.example.tijo.backend.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerRepository extends JpaRepository<Owner, Integer> {
}
