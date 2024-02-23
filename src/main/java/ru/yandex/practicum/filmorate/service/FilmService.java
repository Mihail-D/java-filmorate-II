package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

@Slf4j
@Service
public class FilmService {

    FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film getFilmById(long id) {
        return filmStorage.getFilmById(id);
    }

}

// PUT     /{id}/like/{userId}
// DELETE  /{id}/like/{userId}
// GET     /popular?count={count}  список из первых count фильмов по количеству лайков или первые 10