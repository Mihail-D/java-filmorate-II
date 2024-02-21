package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Controller
public interface FilmStorage {

    @GetMapping("/films")
    List<Film> getFilms();

    @PostMapping("/films")
    Film createFilm(Film film);

    @PutMapping("/films")
    Film updateFilm(Film film);
}
