package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class Film {

    private long id;

    @NotEmpty
    private String name;

    @Max(value = 200, message = "description should not be longer than 200")
    private String description;

    private LocalDate releaseDate;

    @Positive
    private int duration;

    private Genre genre;

    private Mpa mpa;


}
