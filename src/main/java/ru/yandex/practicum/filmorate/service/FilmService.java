package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class FilmService {

    FilmStorage filmStorage;
    UserStorage userStorage;
    MpaStorage mpaStorage;
    GenreStorage genreStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage, MpaStorage mpaStorage, GenreStorage genreStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
    }

        public Film getFilmById(long id) {

        return filmStorage.getFilmById(id);
    }

    public List<Film> getFilms() {
        List<Film> films = filmStorage.getFilms();

        for (Film film : films) {
            Mpa mpa = mpaStorage.getMpaById(film.getMpa().getId());

            if (film.getGenres() == null) {
                film.setGenres(new ArrayList<>());
            }

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

/*   public Film addLike(long filmId, long userId) {
        Film film = getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        Set<Long> filmLikes = film.getLikes();

        filmLikes.add(user.getId());

        filmStorage.updateFilm(film);

        return film;
    }*/

/*    public Film deleteLike(long filmId, long userId) {
        Film film = getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        Set<Long> filmLikes = film.getLikes();

        if (user != null) {
            filmLikes.removeIf(i -> i == userId);
        } else {
            throw new UserNotExistException("User not found");
        }

        filmStorage.updateFilm(film);

        return film;
    }*/

/*    public List<Film> getPopularFilms(int count) {
        List<Film> films = getFilms();
        films.sort(Comparator.comparingInt((Film film) -> film.getLikes().size()).reversed());

        if (count < 0) {
            count = 10;
        }

        return films.stream().limit(count).collect(Collectors.toList());
    }*/
}