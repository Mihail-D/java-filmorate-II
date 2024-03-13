package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotExistException;
import ru.yandex.practicum.filmorate.exceptions.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.InputDataErrorException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
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
    GenreStorage genreDbStorage;
    int id = 0;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, FilmValidator filmValidator, MpaStorage mpaStorage, GenreStorage genreDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmValidator = filmValidator;
        this.mpaStorage = mpaStorage;
        this.genreDbStorage = genreDbStorage;
    }

    @Override
    public List<Film> getFilms() {
        String sql = "SELECT * FROM films";

        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToFilm(rs));
    }

    @Override
    public Film createFilm(Film film) {
        if (film.getGenres() == null) {
            film.setGenres(new ArrayList<>());
        }

        if (filmValidator.validateFilm(film)) {
            id++;
            film.setId(id);
            try {
                Mpa mpa = mpaStorage.getMpaById(film.getMpa().getId());
                film.setMpa(mpa);
            } catch (Exception e) {
                throw new InputDataErrorException("There is no such MPA rating.");
            }
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
            if (film.getGenres() == null) {
                film.setGenres(new ArrayList<>());
            }

            Mpa mpa = mpaStorage.getMpaById(film.getMpa().getId());
            film.setMpa(mpa);
            String sql = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? " +
                    "WHERE film_id = ?";
            jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(),
                    film.getDuration(), film.getMpa().getId(), film.getId()
            );

            sql = "DELETE FROM film_genre WHERE film_id = ?";
            jdbcTemplate.update(sql, film.getId());

            addFilmGenres(film);

            List<Genre> updatedGenres = getGenresByFilmId(film.getId());
            film.setGenres(updatedGenres);

            return getFilmById(film.getId());
        }
    }

    @Override
    public Film getFilmById(long id) {
        String sql = "SELECT * FROM films WHERE film_id = ?";

        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToFilm(rs), id);
        if (films.isEmpty()) {
            throw new FilmNotExistException("Film not found");
        }
        Film film = films.get(0);

        List<Genre> updatedGenres = getGenresByFilmId(film.getId());
        film.setGenres(updatedGenres);

        return film;
    }

    private List<Genre> getGenresByFilmId(long filmId) {
        try {
            return genreDbStorage.getGenresByFilmId(filmId);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public void addFilmGenres(Film film) {
        List<Genre> uniqueGenres = new ArrayList<>();

        for (Genre genre : film.getGenres()) {
            if (!uniqueGenres.contains(genre)) {
                uniqueGenres.add(genre);
            }
        }

        for (Genre genre : uniqueGenres) {
            Genre dbGenre;
            try {
                dbGenre = genreDbStorage.getGenreById(genre.getId());
            } catch (GenreNotFoundException e) {
                log.error("Genre with id {} not found", genre.getId());
                continue;
            }

            String sql = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
            jdbcTemplate.update(sql, film.getId(), dbGenre.getId());
        }
    }

    private Film mapRowToFilm(ResultSet rs) throws SQLException {

        long id = rs.getLong("film_id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        int duration = rs.getInt("duration");
        int mpaId = rs.getInt("mpa_id");

        Mpa mpa = mpaStorage.getMpaById(mpaId);

        List<Genre> genres = getGenresByFilmId(id);

        return Film.builder()
                .id(id)
                .name(name)
                .description(description)
                .releaseDate(releaseDate)
                .duration(duration)
                .mpa(mpa)
                .genres(genres)
                .build();
    }
}
