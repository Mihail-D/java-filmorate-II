package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

@Component
public class LikeDbStorage implements LikeStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLike(long filmId, long userId) {
        String checkQuery = "SELECT * FROM likes WHERE film_id = ? AND user_id = ?";
        Boolean exists = jdbcTemplate.query(checkQuery, resultSet -> resultSet.next() ? true : false, filmId, userId);

        if (Boolean.FALSE.equals(exists)) {
            String insertQuery = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";
            jdbcTemplate.update(insertQuery, filmId, userId);
        }
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        String checkQuery = "SELECT * FROM likes WHERE film_id = ? AND user_id = ?";
        Boolean exists = jdbcTemplate.query(checkQuery, resultSet -> resultSet.next() ? true : false, filmId, userId);

        if (Boolean.TRUE.equals(exists)) {
            String deleteQuery = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
            jdbcTemplate.update(deleteQuery, filmId, userId);
        }
    }

    @Override
    public int getLikesCount(long filmId) {
        String sql = "SELECT COUNT(*) FROM likes WHERE film_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getInt(1), filmId);
        return count != null ? count : 0;
    }
}