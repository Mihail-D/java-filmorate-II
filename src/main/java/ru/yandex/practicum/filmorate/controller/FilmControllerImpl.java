package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.FilmAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.FilmNotExistException;
import ru.yandex.practicum.filmorate.exceptions.InputDataErrorException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utility.FilmValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class FilmControllerImpl implements FilmController {

    private int id = 0;
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @Override
    public Film createFilm(@RequestBody Film film) {
        if (!FilmValidator.isFilmNull(film)) {
            throw new InputDataErrorException("Film object cannot be null");
        }
        if (film.getId() == 0) {
            throw new InputDataErrorException("Film ID cannot be null or zero");
        }
        if (!FilmValidator.isValidDate(film)) {
            throw new InputDataErrorException("Release date - no earlier than December 28, 1895");
        }
        if (!FilmValidator.isValidTitle(film)) {
            throw new InputDataErrorException("Film name cannot be empty");
        }
        if (!FilmValidator.isValidDescription(film)) {
            throw new InputDataErrorException("Description length exceeds 200 characters");
        }
        if (!FilmValidator.isValidDuration(film)) {
            throw new InputDataErrorException("Movie duration cannot be negative");
        }
        if (films.containsKey(film.getId())) {
            throw new FilmAlreadyExistException("The film is already included in the database");
        }

        id++;
        film.setId(id);
        films.put(film.getId(), film);

        return film;
    }

    @Override
    public Film updateFilm(@RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            throw new FilmNotExistException("There is no such film in the database");
        } else {
            films.put(film.getId(), film);
        }

        return film;
    }
}