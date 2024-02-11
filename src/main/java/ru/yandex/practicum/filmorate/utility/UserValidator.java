package ru.yandex.practicum.filmorate.utility;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.exceptions.InputDataErrorException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@UtilityClass
public class UserValidator {

    public boolean isMailEmpty(User user) {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new InputDataErrorException("The mail field must not be empty and must comply with the format");
        }

        return false;
    }

    public boolean isLoginEmpty(User user) {
        if (user.getLogin().isBlank()) {
            throw new InputDataErrorException("Login must not be empty");
        }

        return false;
    }

    public boolean isBirthdayValid(User user) {
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new InputDataErrorException("Birth date cannot be in the future");
        }

        return false;
    }

}
