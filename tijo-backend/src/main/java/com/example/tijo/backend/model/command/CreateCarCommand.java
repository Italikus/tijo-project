package com.example.tijo.backend.model.command;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import static com.example.tijo.backend.util.CarUtils.*;

@Getter
@Setter
public class CreateCarCommand {
    private String brand;
    private String model;
    @NotNull(message = VALIDATION_VIN_NOT_NULL_MESSAGE)
    @Pattern(regexp = VIN_REGEX, message = VALIDATION_VIN_PATTERN_MESSAGE)
    private String vin;
    private int horsePower;
    private int productionYear;
}
