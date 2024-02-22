package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utility.UserValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private long id = 0;
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User createUser(User user) {
        UserValidator.validateUserForCreation(user, users);

        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        id++;
        user.setId(id);
        users.put(id, user);

        log.info("Создан пользователь " + user.getName());
        log.info("В списке пользователей " + users.size() + " человек");

        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new UserNotExistException("There is no such user in the database");
        } else {
            User existingUser = users.get(user.getId());
            existingUser.setName(user.getName());
            existingUser.setEmail(user.getEmail());
            existingUser.setBirthday(user.getBirthday());
            existingUser.setLogin(user.getLogin());

            users.put(user.getId(), existingUser);
        }

        log.info("Пользователь " + user.getName() + " изменен");

        return users.get(user.getId());
    }
}
