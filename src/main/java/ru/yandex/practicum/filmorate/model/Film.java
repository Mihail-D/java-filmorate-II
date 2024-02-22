package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Data
public class Film {

    long id;

    @NotEmpty
    String name;

    @Max(value = 200, message = "description should not be longer than 200")
    String description;

    LocalDate releaseDate;

    @Positive
    int duration;
}
