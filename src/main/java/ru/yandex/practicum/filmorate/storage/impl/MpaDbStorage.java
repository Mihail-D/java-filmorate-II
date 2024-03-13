package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
public class MpaDbStorage implements MpaStorage {

    JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa getMpaById(int id) {
        if (id < 1 || id > 5) {
            throw new MpaNotFoundException("MPA rating not found");
        }

        String sql = "SELECT * FROM mpa WHERE mpa_id=?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapRowToMpa(rs), id);
    }

    @Override
    public List<Mpa> getAllMpa() {
        String sql = "SELECT * FROM mpa ORDER BY mpa_id";

        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToMpa(rs));
    }

    private Mpa mapRowToMpa(ResultSet rs) throws SQLException {

        int id = rs.getInt("mpa_id");
        String name = rs.getString("name");
        String description = rs.getString("description");

        return Mpa.builder()
                .id(id)
                .name(name)
                .description(description)
                .build();
    }
}
