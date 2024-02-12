package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.InputDataErrorException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserControllerImplTest {

    @Test
    void getUsers() {
        UserControllerImpl userController = new UserControllerImpl();

        User user1 = new User();
        user1.setId(1);
        user1.setName("User1");
        user1.setLogin("login1");
        user1.setEmail("user1@example.com");
        user1.setBirthday(LocalDate.of(1990, 1, 1)); // добавьте эту строку
        userController.createUser(user1);

        User user2 = new User();
        user2.setId(2);
        user2.setName("User2");
        user2.setLogin("login2");
        user2.setEmail("user2@example.com");
        user2.setBirthday(LocalDate.of(1990, 1, 2)); // добавьте эту строку
        userController.createUser(user2);

        List<User> users = userController.getUsers();

        assertEquals(2, users.size());

        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
    }

    @Test
    void createUser() {
        UserControllerImpl userController = new UserControllerImpl();

        User user = new User();
        user.setName("Test User");
        user.setLogin("testlogin");
        user.setEmail("test@example.com");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        User createdUser = userController.createUser(user);

        assertNotNull(createdUser);
        assertNotNull(createdUser.getId());

        List<User> users = userController.getUsers();
        assertTrue(users.contains(createdUser));
    }

    @Test
    void createUserWithEmptyFields() {
        UserControllerImpl userController = new UserControllerImpl();

        User user = new User();

        assertThrows(InputDataErrorException.class, () -> userController.createUser(user));
    }

    @Test
    void createUserWithInvalidEmail() {
        UserControllerImpl userController = new UserControllerImpl();

        User user = new User();
        user.setEmail("");

        assertThrows(InputDataErrorException.class, () -> userController.createUser(user));
    }

    @Test
    void createUserWithInvalidEmailPattern() {
        UserControllerImpl userController = new UserControllerImpl();

        User user = new User();
        user.setEmail("это-неправильный?эмейл@"); // Устанавливаем невалидный адрес электронной почты

        assertThrows(InputDataErrorException.class, () -> userController.createUser(user));
    }

    @Test
    void updateUser() {
    }
}