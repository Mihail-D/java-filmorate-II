package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

public interface GenreStorage {

    Genre getGenreById(int id);
}
