package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.utility.FilmValidator;

import java.util.List;

@Slf4j
@Component
public class FilmDbStorage implements FilmStorage {

    JdbcTemplate jdbcTemplate;
    FilmValidator filmValidator;
    MpaStorage mpaStorage;
    int id = 0;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, FilmValidator filmValidator, MpaStorage mpaStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmValidator = filmValidator;
        this.mpaStorage = mpaStorage;
    }

    @Override
    public List<Film> getFilms() {
        return null;
    }

    @Override
    public Film createFilm(Film film) {
        if (filmValidator.validateFilm(film)) {
            id++;
            film.setId(id);
            String sql = "INSERT INTO films (film_id, name, description, release_date, duration, mpa_id) VALUES (?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql, film.getId(), film.getName(), film.getDescription(), film.getReleaseDate(),
                    film.getDuration(), film.getMpaRaring()
            );

            return film;
        }

        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        return null;
    }

    @Override
    public Film getFilmById(long id) {
        return null;
    }
}
