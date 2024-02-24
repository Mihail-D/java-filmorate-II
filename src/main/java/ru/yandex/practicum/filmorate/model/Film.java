package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class Film {

    private Set<Long> likes = new HashSet<>();
    private long id;

    @NotEmpty
    String name;

    @Max(value = 200, message = "description should not be longer than 200")
    String description;

    LocalDate releaseDate;

    @Positive
    int duration;

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
        if (getLikes() != null ? !getLikes().equals(film.getLikes()) : film.getLikes() != null) {
            return false;
        }
        if (getName() != null ? !getName().equals(film.getName()) : film.getName() != null) {
            return false;
        }
        if (getDescription() != null ? !getDescription().equals(film.getDescription()) : film.getDescription() != null) {
            return false;
        }
        return getReleaseDate().equals(film.getReleaseDate());
    }

    @Override
    public int hashCode() {
        int result = getLikes() != null ? getLikes().hashCode() : 0;
        result = 31 * result + (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + getReleaseDate().hashCode();
        result = 31 * result + getDuration();
        return result;
    }
}
