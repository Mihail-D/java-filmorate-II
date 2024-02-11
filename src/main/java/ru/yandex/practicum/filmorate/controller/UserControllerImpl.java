package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.InputDataErrorException;
import ru.yandex.practicum.filmorate.exceptions.UserAlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.UserNotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utility.UserValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class UserControllerImpl implements UserController {

    private int id = 0;
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @Override
    public User createUser(@RequestBody User user) {
        if (UserValidator.isMailEmpty(user) || UserValidator.isLoginEmpty(user) || UserValidator.isBirthdayValid(user)) {
            throw new InputDataErrorException("The mail field is filled in with errors");
        } else if (users.containsKey(user.getId())) {
            throw new UserAlreadyExistsException("The user is already included in the database");
        } else if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        id++;
        user.setId(id);
        users.put(id, user);

        return user;
    }

    @Override
    public User updateUser(@RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            throw new UserNotExistException("There is no such user in the database");
        } else {
            users.put(user.getId(), user);
        }
        return user;
    }
}
