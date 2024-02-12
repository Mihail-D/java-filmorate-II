package ru.yandex.practicum.filmorate.utility;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.exceptions.InputDataErrorException;
import ru.yandex.practicum.filmorate.exceptions.UserAlreadyExistsException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Map;

@UtilityClass
public class UserValidator {

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

    public void validateUserForCreation(User user, Map<Integer, User> users) {
        isMailEmpty(user);
        isMailPatternValid(user);
        isLoginEmpty(user);
        isBirthdayValid(user);

        if (users.containsKey(user.getId())) {
            throw new UserAlreadyExistsException("The user is already included in the database");
        }
    }
}