package com.example.tijo.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String brand;
    private String model;
    @Column(unique = true, nullable = false, updatable = false)
    private String vin;
    private int horsePower;
    private int year;

    public Car(String brand, String model, String vin, int horsePower, int year) {
        this.brand = brand;
        this.model = model;
        this.vin = vin;
        this.horsePower = horsePower;
        this.year = year;
    }
}
