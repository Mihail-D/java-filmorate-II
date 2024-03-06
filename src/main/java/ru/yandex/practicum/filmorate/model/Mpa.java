package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
public class Mpa {

    private int id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;
}
