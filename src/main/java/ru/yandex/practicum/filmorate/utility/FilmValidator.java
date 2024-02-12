package ru.yandex.practicum.filmorate.utility;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.exceptions.InputDataErrorException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@UtilityClass
public class FilmValidator {

    public boolean isValidDate(Film film) {
        if (film == null || film.getReleaseDate() == null) {
            return false;
        }
        LocalDate earliestDate = LocalDate.of(1895, 12, 28);
        return !film.getReleaseDate().isBefore(earliestDate);
    }

    public boolean isValidTitle(Film film) {
        if (film == null || film.getName() == null) {
            return false;
        }
        return !film.getName().isBlank() && !film.getName().isEmpty();
    }

    public boolean isValidDescription(Film film) {
        if (film == null || film.getDescription() == null) {
            return false;
        }
        return film.getDescription().length() <= 200;
    }

    public boolean isFilmNull(Film film) {
        return film == null;
    }

    public boolean isValidDuration(Film film) {
        if (film == null) {
            return false;
        }
        return film.getDuration() >= 1;
    }

    public boolean validateFilm(Film film) {

        if (isFilmNull(film)) {
            throw new InputDataErrorException("Film object cannot be null");
        }
        if (!isValidDate(film)) {
            throw new InputDataErrorException("Release date - no earlier than December 28, 1895");
        }
        if (!isValidTitle(film)) {
            throw new InputDataErrorException("Film name cannot be empty");
        }
        if (!isValidDescription(film)) {
            throw new InputDataErrorException("Description length exceeds 200 characters");
        }
        if (!isValidDuration(film)) {
            throw new InputDataErrorException("Movie duration cannot be negative");
        }

        return true;
    }
}
