package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.FilmNotExistException;
import ru.yandex.practicum.filmorate.exceptions.InputDataErrorException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class FilmControllerImplTest {

    private FilmControllerImpl filmController;
    private Film film1;
    private Film film2;

    @BeforeEach
    void setUp() {
        filmController = new FilmControllerImpl();
        film1 = Mockito.mock(Film.class);
        film2 = Mockito.mock(Film.class);
        when(film1.getId()).thenReturn(1);
        when(film1.getName()).thenReturn("Film 1 Title");
        when(film1.getDescription()).thenReturn("Film 1 Description");
        when(film1.getDuration()).thenReturn(67);
        when(film1.getReleaseDate()).thenReturn(LocalDate.of(2020, 1, 1));

        when(film2.getId()).thenReturn(2);
        when(film2.getName()).thenReturn("Film 2 Title");
        when(film2.getDescription()).thenReturn("Film 2 Description");
        when(film2.getDuration()).thenReturn(167);
        when(film2.getReleaseDate()).thenReturn(LocalDate.of(2021, 1, 1));
    }

    @Test
    void getFilms() {
        List<Film> expectedFilms = Arrays.asList(film1, film2);
        filmController.createFilm(film1);
        filmController.createFilm(film2);

        List<Film> actualFilms = filmController.getFilms();

        assertEquals(expectedFilms, actualFilms);
    }

    @Test
    void createFilm() {
        when(film1.getId()).thenReturn(1);
        Film actualFilm = filmController.createFilm(film1);

        assertEquals(film1, actualFilm);
    }

    @Test
    void createFilmWithEmptyFilm() {
        Film nullableFilm = null;
        assertThrows(InputDataErrorException.class, () -> filmController.createFilm(nullableFilm));
    }

    @Test
    void createFilmWithEmptyName() {
        Film filmWithEmptyName = Mockito.mock(Film.class);
        when(filmWithEmptyName.getName()).thenReturn("");
        assertThrows(InputDataErrorException.class, () -> filmController.createFilm(filmWithEmptyName));
    }

    @Test
    void createFilmWithEmptyDescription() {
        Film filmWithEmptyDescription = new Film();
        filmWithEmptyDescription.setId(1);
        filmWithEmptyDescription.setName("Test Film");
        filmWithEmptyDescription.setDescription("");
        filmWithEmptyDescription.setDuration(120);
        filmWithEmptyDescription.setReleaseDate(LocalDate.of(2020, 1, 1));

        assertDoesNotThrow(() -> filmController.createFilm(filmWithEmptyDescription));
    }

    @Test
    void createFilmWithDescriptionLength199() {
        Film film = new Film();
        film.setId(1);
        film.setName("Test Film");
        film.setDescription("A".repeat(199));
        film.setDuration(120);
        film.setReleaseDate(LocalDate.of(2020, 1, 1));

        assertDoesNotThrow(() -> filmController.createFilm(film));
    }

    @Test
    void createFilmWithDescriptionLength200() {
        Film film = new Film();
        film.setId(1);
        film.setName("Test Film");
        film.setDescription("A".repeat(200));
        film.setDuration(120);
        film.setReleaseDate(LocalDate.of(2020, 1, 1));

        assertDoesNotThrow(() -> filmController.createFilm(film));
    }

    @Test
    void createFilmWithDescriptionLength201() {
        Film film = new Film();
        film.setId(1);
        film.setName("Test Film");
        film.setDescription("A".repeat(201));
        film.setDuration(120);
        film.setReleaseDate(LocalDate.of(2020, 1, 1));

        assertThrows(InputDataErrorException.class, () -> filmController.createFilm(film));
    }

    @Test
    void createFilmWithReleaseDateBeforeEarliest() {
        Film film = new Film();
        film.setId(1);
        film.setName("Test Film");
        film.setDescription("Test Description");
        film.setDuration(120);
        film.setReleaseDate(LocalDate.of(1895, 12, 27));

        assertThrows(InputDataErrorException.class, () -> filmController.createFilm(film));
    }

    @Test
    void createFilmWithReleaseDateAtEarliest() {
        Film film = new Film();
        film.setId(1);
        film.setName("Test Film");
        film.setDescription("Test Description");
        film.setDuration(120);
        film.setReleaseDate(LocalDate.of(1895, 12, 28));

        assertDoesNotThrow(() -> filmController.createFilm(film));
    }

    @Test
    void createFilmWithReleaseDateAfterEarliest() {
        Film film = new Film();
        film.setId(1);
        film.setName("Test Film");
        film.setDescription("Test Description");
        film.setDuration(120);
        film.setReleaseDate(LocalDate.of(1895, 12, 29));

        assertDoesNotThrow(() -> filmController.createFilm(film));
    }

    @Test
    void createFilmWithDurationMinusOne() {
        Film film = new Film();
        film.setId(1);
        film.setName("Test Film");
        film.setDescription("Test Description");
        film.setDuration(-1);
        film.setReleaseDate(LocalDate.of(2020, 1, 1));

        assertThrows(InputDataErrorException.class, () -> filmController.createFilm(film));
    }

    @Test
    void createFilmWithDurationZero() {
        Film film = new Film();
        film.setId(1);
        film.setName("Test Film");
        film.setDescription("Test Description");
        film.setDuration(0);
        film.setReleaseDate(LocalDate.of(2020, 1, 1));

        assertThrows(InputDataErrorException.class, () -> filmController.createFilm(film));
    }

    @Test
    void createFilmWithDurationOne() {
        Film film = new Film();
        film.setId(1);
        film.setName("Test Film");
        film.setDescription("Test Description");
        film.setDuration(1);
        film.setReleaseDate(LocalDate.of(2020, 1, 1));

        assertDoesNotThrow(() -> filmController.createFilm(film));
    }

    @Test
    void updateFilm() {
        when(film1.getId()).thenReturn(1);
        filmController.createFilm(film1);
        when(film1.getName()).thenReturn("Updated Title");
        Film actualFilm = filmController.updateFilm(film1);

        assertEquals(film1, actualFilm);
    }

    @Test
    void updateFilmNotInDatabase() {
        Film film = new Film();
        film.setId(100);
        film.setName("Test Film");
        film.setDescription("Test Description");
        film.setDuration(120);
        film.setReleaseDate(LocalDate.of(2020, 1, 1));

        assertThrows(FilmNotExistException.class, () -> filmController.updateFilm(film));
    }

}