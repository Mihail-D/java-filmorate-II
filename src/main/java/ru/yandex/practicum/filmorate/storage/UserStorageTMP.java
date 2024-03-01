package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorageTMP {

    List<User> getUsers();

    User createUser(User user);

    User updateUser(User user);

    User getUserById(long id);
}
