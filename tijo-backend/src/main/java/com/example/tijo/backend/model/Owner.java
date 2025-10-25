package com.example.tijo.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String surname;
    private String pesel;

    @OneToMany(mappedBy = "owner")
    private List<Car> cars = new ArrayList<>();

    public Owner(String name, String surname, String pesel) {
        this.name = name;
        this.surname = surname;
        this.pesel = pesel;
    }

    public void addCar(Car car) {
        if (car == null) throw new IllegalArgumentException("car is null");

        cars.add(car);
        car.setOwner(this);
    }
}
