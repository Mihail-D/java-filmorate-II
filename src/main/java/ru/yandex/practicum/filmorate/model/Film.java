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
    String name;

    @Max(value = 200, message = "description should not be longer than 200")
    String description;

    LocalDate releaseDate;

    @Positive
    int duration;

    int genreId;

    @NotEmpty
    int mpaRaring;

    Mpa mpa;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Film film = (Film) o;

        if (getId() != film.getId()) {
            return false;
        }
        if (getDuration() != film.getDuration()) {
            return false;
        }
        if (getGenreId() != film.getGenreId()) {
            return false;
        }
        if (getMpaRaring() != film.getMpaRaring()) {
            return false;
        }
        if (!getName().equals(film.getName())) {
            return false;
        }
        if (getDescription() != null ? !getDescription().equals(film.getDescription()) : film.getDescription() != null) {
            return false;
        }
        return getReleaseDate().equals(film.getReleaseDate());
    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + getName().hashCode();
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + getReleaseDate().hashCode();
        result = 31 * result + getDuration();
        result = 31 * result + getGenreId();
        result = 31 * result + getMpaRaring();
        return result;
    }
}
