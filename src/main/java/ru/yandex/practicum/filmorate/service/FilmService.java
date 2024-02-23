package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Set;

@Slf4j
@Service
public class FilmService {

    FilmStorage filmStorage;
    UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film getFilmById(long id) {
        return filmStorage.getFilmById(id);
    }

    public Film addLike(long filmId, long userId) {
        Film film = getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        Set<Long> filmLikes = film.getLikes();

        filmLikes.add(user.getId());

        filmStorage.updateFilm(film);

        return film;
    }

    public Film deleteLike(long filmId, long userId) {
        Film film = getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        Set<Long> filmLikes = film.getLikes();

        if (user != null) {
            filmLikes.removeIf(i -> i == userId);
        }
        else {
            throw new UserNotExistException("User not found");
        }

        filmStorage.updateFilm(film);

        return film;
    }
}

// *** PUT     /{id}/like/{userId}
// *** DELETE  /{id}/like/{userId}
// GET     /popular?count={count}  список из первых count фильмов по количеству лайков или первые 10