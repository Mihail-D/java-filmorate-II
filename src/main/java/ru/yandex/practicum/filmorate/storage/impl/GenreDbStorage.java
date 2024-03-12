package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
public class GenreDbStorage implements GenreStorage {

    JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getGenreById(int id) {
        if (id < 1 || id > 6) {
            throw new GenreNotFoundException("Genre not found");
        }

        String sql = "SELECT * FROM genre WHERE genre_id=?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapRowToGenre(rs), id);
    }

    @Override
    public List<Genre> getAllGenre() {
        String sql = "SELECT * FROM genre ORDER BY genre_id";

        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToGenre(rs));
    }

    @Override
    public List<Genre> getGenresByFilmId(long filmId) {
        String sql = "SELECT g.* FROM genre g INNER JOIN film_genre fg ON g.genre_id = fg.genre_id WHERE fg.film_id = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            int id = rs.getInt("genre_id");
            String name = rs.getString("name");

            return Genre.builder()
                    .id(id)
                    .name(name)
                    .build();
        }, filmId);
    }

    private Genre mapRowToGenre(ResultSet rs) throws SQLException {

        int id = rs.getInt("genre_id");
        String name = rs.getString("name");

        return Genre.builder()
                .id(id)
                .name(name)
                .build();
    }
}
