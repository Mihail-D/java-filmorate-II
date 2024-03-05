package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utility.FilmValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage {

    FilmValidator filmValidator;

    @Autowired
    public InMemoryFilmStorage(FilmValidator filmValidator) {
        this.filmValidator = filmValidator;
    }

    private long id = 0;
    private final Map<Long, Film> films = new HashMap<>();


    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }


    public Film createFilm(Film film) {
        if (filmValidator.validateFilm(film)) {
            id++;
            film.setId(id);
            films.put(film.getId(), film);
        }

        return film;
    }


    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new FilmNotExistException("There is no such film in the database");
        } else {
            films.put(film.getId(), film);
        }

        return film;
    }


    public Film getFilmById(long id) {
        if (!films.containsKey(id)) {
            throw new FilmNotExistException("There is no such film in the database");
        }
        return films.get(id);
    }
}
