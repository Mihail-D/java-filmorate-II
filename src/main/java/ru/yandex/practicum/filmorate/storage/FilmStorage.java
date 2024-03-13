package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Controller;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Controller
public interface FilmStorage {

    List<Film> getFilms();

    Film createFilm(Film film);

    Film updateFilm(Film film);

    Film getFilmById(long id);

    void addFilmGenres(Film film);
}
