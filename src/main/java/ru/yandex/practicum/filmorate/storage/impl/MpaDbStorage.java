package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

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
        RowMapper<Mpa> rowMapper = (rs, rowNum) -> {
            Mpa mpa = new Mpa();
            mpa.setId(rs.getInt("mpa_id"));
            mpa.setName(rs.getString("name"));
            mpa.setDescription(rs.getString("description"));
            return mpa;
        };

        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    @Override
    public List<Mpa> getAllMpa() {
        String sql = "SELECT * FROM mpa ORDER BY mpa_id";
        RowMapper<Mpa> rowMapper = (rs, rowNum) -> {
            Mpa mpa = new Mpa();
            mpa.setId(rs.getInt("mpa_id"));
            mpa.setName(rs.getString("name"));
            mpa.setDescription(rs.getString("description"));
            return mpa;
        };

        return jdbcTemplate.query(sql, rowMapper);
    }
}
