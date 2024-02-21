package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Controller;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Controller
public interface UserStorage {

    List<User> getUsers();

    User createUser(User user);

    User updateUser(User user);
}