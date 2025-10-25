package com.example.tijo.backend.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "vin")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String brand;
    private String model;
    @Column(unique = true, nullable = false, updatable = false)
    private String vin;
    private int horsePower;
    private int productionYear;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;

    public Car(String brand, String model, String vin, int horsePower, int productionYear) {
        this.brand = brand;
        this.model = model;
        this.vin = vin;
        this.horsePower = horsePower;
        this.productionYear = productionYear;
    }
}
