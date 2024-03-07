package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.utility.FilmValidator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
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
        String sql = "SELECT * FROM films";

        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToFilm(rs));
    }

    @Override
    public Film createFilm(Film film) {
        if (filmValidator.validateFilm(film)) {
            id++;
            film.setId(id);
            Mpa mpa = mpaStorage.getMpaById(film.getMpa().getId());
            film.setMpa(mpa);
            String sql = "INSERT INTO films (film_id, name, description, release_date, duration, mpa_id) VALUES (?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql, film.getId(), film.getName(), film.getDescription(), film.getReleaseDate(),
                    film.getDuration(), film.getMpa().getId()
            );
        }

        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!filmValidator.isFilmExists(film.getId())) {
            throw new FilmNotExistException("There is no such film in the database");
        } else {
            Mpa mpa = mpaStorage.getMpaById(film.getMpa().getId());
            film.setMpa(mpa);
            String sql = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? " +
                    "WHERE film_id = ?";
            jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(),
                    film.getDuration(), film.getMpa().getId(), film.getId()
            );
        }
        return film;
    }

    @Override
    public Film getFilmById(long id) {
        String sql = "SELECT * FROM films WHERE film_id = ?";

        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToFilm(rs), id);
        if (films.isEmpty()) {
            throw new FilmNotExistException("Film not found");
        }
        Film film = films.get(0);

        if (film.getGenres() == null) {
            film.setGenres(new ArrayList<>());
        }

        return film;
    }

    private Film mapRowToFilm(ResultSet rs) throws SQLException {

        long id = rs.getLong("film_id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        int duration = rs.getInt("duration");
        int mpaId = rs.getInt("mpa_id");

        Mpa mpa = mpaStorage.getMpaById(mpaId);

        return Film.builder()
                .id(id)
                .name(name)
                .description(description)
                .releaseDate(releaseDate)
                .duration(duration)
                .mpa(mpa)
                .build();
    }
}
