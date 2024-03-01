package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.utility.UserValidator;

import java.util.List;

@Slf4j
@Component
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserValidator userValidator;
    private long id = 0;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate, UserValidator userValidator) {
        this.jdbcTemplate = jdbcTemplate;
        this.userValidator = userValidator;
    }

    @Override
    public List<User> getUsers() {
        String sql = "SELECT * FROM users";
        RowMapper<User> rowMapper = (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getLong("user_id"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setBirthday(rs.getDate("birthday").toLocalDate());
            user.setLogin(rs.getString("login"));
            return user;
        };
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public User createUser(User user) {
        userValidator.validateUserForCreation(user);
        userValidator.validateUserName(user);

        id++;
        user.setId(id);

        String sql = "INSERT INTO users (user_id, name, email, birthday, login) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getId(), user.getName(), user.getEmail(), user.getBirthday(), user.getLogin());

        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!userValidator.isUserExists(user.getId())) {
            throw new UserNotExistException("There is no such user in the database");
        } else {
            String sql = "UPDATE users SET name = ?, email = ?, birthday = ?, login = ? WHERE user_id = ?";
            jdbcTemplate.update(sql, user.getName(), user.getEmail(), user.getBirthday(), user.getLogin(), user.getId());
        }
        return user;
    }

    @Override
    public User getUserById(long id) {
        return null;
    }
}
