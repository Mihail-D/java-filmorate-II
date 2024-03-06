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

    private int genreId;

    private Mpa mpa;

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
        if (!getName().equals(film.getName())) {
            return false;
        }
        if (getDescription() != null ? !getDescription().equals(film.getDescription()) : film.getDescription() != null) {
            return false;
        }
        if (!getReleaseDate().equals(film.getReleaseDate())) {
            return false;
        }
        return getMpa().equals(film.getMpa());
    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + getName().hashCode();
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + getReleaseDate().hashCode();
        result = 31 * result + getDuration();
        result = 31 * result + getGenreId();
        result = 31 * result + getMpa().hashCode();
        return result;
    }
}
