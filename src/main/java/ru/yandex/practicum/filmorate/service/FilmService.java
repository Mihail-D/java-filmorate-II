package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FilmService {

}

// PUT /films/{id}/like/{userId}
// DELETE /films/{id}/like/{userId}
// GET /films/popular?count={count}  список из первых count фильмов по количеству лайков или первые 10