package ru.yandex.practicum.filmorate.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.InputDataErrorException;
import ru.yandex.practicum.filmorate.exceptions.UserAlreadyExistsException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Service
public class UserValidator {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserValidator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void isMailEmpty(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new InputDataErrorException("The mail field must not be empty and must comply with the format");
        }
    }

    public void isMailPatternValid(User user) {
        String emailPattern = "^[a-zA-Z0-9._%-]+@[a-zA-Z]+\\.[a-zA-Z]{2,3}$";
        if (!user.getEmail().matches(emailPattern)) {
            throw new InputDataErrorException("Email does not match the pattern");
        }
    }

    public void isLoginEmpty(User user) {
        if (user.getLogin().isBlank()) {
            throw new InputDataErrorException("Login must not be empty");
        }

    }

    public void isBirthdayValid(User user) {
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new InputDataErrorException("Birth date cannot be in the future");
        }

    }

    public void validateUserName(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }

    public boolean isUserExists(long id) {
        String sql = "SELECT COUNT(*) FROM users WHERE user_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getInt(1), id);
        return count != null && count > 0;
    }

    public void validateUserForCreation(User user) {
        isMailEmpty(user);
        isMailPatternValid(user);
        isLoginEmpty(user);
        isBirthdayValid(user);

        if (isUserExists(user.getId())) {
            throw new UserAlreadyExistsException("The user is already included in the database");
        }
    }
}