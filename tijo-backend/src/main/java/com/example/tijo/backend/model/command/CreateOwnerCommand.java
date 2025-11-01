package com.example.tijo.backend.model.command;

import com.example.tijo.backend.validation.ValidPesel;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOwnerCommand {
    @NotBlank
    private String name;
    @NotBlank
    private String surname;
    @NotBlank
    @ValidPesel
    private String pesel;
}

