package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    UserValidator userValidator;

    @Autowired
    public InMemoryUserStorage(UserValidator userValidator) {
        this.userValidator = userValidator;
    }

    private long id = 0;
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User createUser(User user) {
        userValidator.validateUserForCreation(user, users);
        userValidator.validateUserName(user);

        id++;
        user.setId(id);
        users.put(id, user);

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
            existingUser.setFriends(user.getFriends());

            users.put(user.getId(), existingUser);
        }

        return users.get(user.getId());
    }

    @Override
    public User getUserById(long id) {
        return users.get(id);
    }
}
