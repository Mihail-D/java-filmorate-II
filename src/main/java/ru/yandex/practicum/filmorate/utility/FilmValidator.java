package ru.yandex.practicum.filmorate.utility;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.exceptions.InputDataErrorException;
import ru.yandex.practicum.filmorate.exceptions.InvalidDateException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@UtilityClass
public class FilmValidator {

    public boolean isValidDate(Film film) {
        LocalDate earliestDate = LocalDate.of(1895, 12, 28);
        if (film.getReleaseDate().isBefore(earliestDate)) {
            throw new InvalidDateException("Release date is not valid");
        }
        return false;
    }

    public boolean isValidTitle(Film film) {

        if (film.getName() == null || film.getName().isBlank() || film.getName().isEmpty()) {
            throw new InputDataErrorException("Movie title cannot be empty");
        }
        return false;
    }

    public boolean isValidDescription(Film film) {

        if (film.getDescription().length() > 200) {
            throw new InputDataErrorException("Description length exceeds 200 characters");
        }
        return false;
    }

    public boolean isValidDuration(Film film) {

        if (film.getDuration() < 1) {
            throw new InputDataErrorException("Movie duration cannot be negative");
        }
        return false;
    }
}
