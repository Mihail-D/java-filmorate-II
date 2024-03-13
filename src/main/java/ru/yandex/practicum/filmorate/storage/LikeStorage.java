package ru.yandex.practicum.filmorate.storage;

public interface LikeStorage {

    void addLike(long filmId, long userId);

    void deleteLike(long filmId, long userId);

    int getLikesCount(long filmId);
}
