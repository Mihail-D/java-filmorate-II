package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
public class Genre {

    private int id;

    @NotBlank
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Genre genre = (Genre) o;

        return getId() == genre.getId();
    }

    @Override
    public int hashCode() {
        return getId();
    }
}
