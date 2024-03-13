package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    FilmStorage filmStorage;
    UserStorage userStorage;
    MpaStorage mpaStorage;
    GenreStorage genreStorage;
    LikeStorage likeStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage, MpaStorage mpaStorage, GenreStorage genreStorage, LikeStorage likeStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
        this.likeStorage = likeStorage;
    }

    public Film getFilmById(long id) {

        return filmStorage.getFilmById(id);
    }

    public List<Film> getFilms() {
        List<Film> films = filmStorage.getFilms();

        films.sort(Comparator.comparing(Film::getId));

        for (Film film : films) {
            Mpa mpa = mpaStorage.getMpaById(film.getMpa().getId());
            List<Genre> genres = genreStorage.getGenresByFilmId(film.getId());

            film.setGenres(Objects.requireNonNullElseGet(genres, ArrayList::new));

            if (mpa != null) {
                film.setMpa(mpa);
            } else {
                throw new MpaNotFoundException("Mpa not found for id: " + film.getMpa().getId());
            }
        }

        return films;
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film addLike(long filmId, long userId) {
        Film film = getFilmById(filmId);

        likeStorage.addLike(filmId, userId);

        return film;
    }

    public Film deleteLike(long filmId, long userId) {
        Film film = getFilmById(filmId);
        User user = userStorage.getUserById(userId);

        if (film != null && user != null) {
            likeStorage.deleteLike(filmId, userId);
        }

        return film;
    }

    public List<Film> getPopularFilms(int count) {
        List<Film> films = getFilms();
        films.sort(Comparator.comparingInt((Film film) -> likeStorage.getLikesCount(film.getId())).reversed());

        if (count < 0) {
            count = 10;
        }

        return films.stream().limit(count).collect(Collectors.toList());
    }
}