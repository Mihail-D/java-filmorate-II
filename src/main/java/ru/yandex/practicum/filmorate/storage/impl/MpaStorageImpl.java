package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

@Slf4j
@Component
public class MpaStorageImpl implements MpaStorage {

    JdbcTemplate jdbcTemplate;

    public String getMpa(int id) {
        String sql = "SELECT name FROM mpa WHERE mpa_id=?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getString(1), id);
    }
}
