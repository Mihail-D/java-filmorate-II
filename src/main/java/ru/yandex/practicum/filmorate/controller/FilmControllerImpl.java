package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.FilmAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.FilmNotExistException;
import ru.yandex.practicum.filmorate.exceptions.InputDataErrorException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utility.Validator;

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
    @GetMapping("/films")
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    @PostMapping("/films")
    public Film createFilm(@RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            throw new FilmAlreadyExistException("The film is already included in the database");

        } else if (Validator.isValidDate(film) || Validator.isValidTitle(film)
                || Validator.isValidDescription(film) || Validator.isValidDuration(film)) {

            throw new InputDataErrorException("Release date - no earlier than December 28, 1895");
        } else {
            id++;
            film.setId(id);
            films.put(film.getId(), film);
        }

        return film;
    }

    @Override
    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            throw new FilmNotExistException("There is no such film in the database");
        } else {
            films.put(film.getId(), film);
        }

        return film;
    }
}