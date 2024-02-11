package ru.yandex.practicum.filmorate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Controller
public interface UserController {

    @GetMapping("/users")
    List<User> getUsers();

    @PostMapping("/users")
    User createUser(User user);

    @PutMapping("/users")
    User updateUser(User user);
}