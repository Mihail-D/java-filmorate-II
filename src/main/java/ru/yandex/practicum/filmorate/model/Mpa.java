package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class Mpa {

    int id;

    @NotBlank
    String name;

    @NotBlank
    String description;
}