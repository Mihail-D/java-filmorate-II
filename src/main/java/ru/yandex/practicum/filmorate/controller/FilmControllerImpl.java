package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.FilmNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utility.FilmValidator;

import javax.validation.Valid;
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
    public Film createFilm(@Valid @RequestBody Film film) {
        if (!FilmValidator.validateFilm(film)) {
            id++;
            film.setId(id);
            films.put(film.getId(), film);
        }

        log.info("Создан фильм " + film.getName());
        log.info("В списке фильмов " + films.size() + " человек");

        return film;
    }

    @Override
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            throw new FilmNotExistException("There is no such film in the database");
        } else {
            films.put(film.getId(), film);
        }

        log.info("Фильм " + film.getName() + " изменен");

        return film;
    }
}