package com.example.tijo.backend.model.command;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditCarCommand {
    private int id;
    private int horsePower;
    private Integer ownerId;
}