package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.InputDataErrorException;
import ru.yandex.practicum.filmorate.exceptions.UserNotExistException;
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
        user.setEmail("это-неправильный?эмейл@");

        assertThrows(InputDataErrorException.class, () -> userController.createUser(user));
    }

    @Test
    void createUserWithInvalidLogin() {
        UserControllerImpl userController = new UserControllerImpl();

        User user = new User();
        user.setLogin(" ");

        assertThrows(InputDataErrorException.class, () -> userController.createUser(user));
    }

    @Test
    void createUserWithNameEmpty() {
        UserControllerImpl userController = new UserControllerImpl();

        User user = new User();
        user.setLogin("testlogin");
        user.setEmail("test@example.com");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        User createdUser = userController.createUser(user);

        assertNotNull(createdUser);
        assertEquals(user.getLogin(), createdUser.getName());
    }

    @Test
    void createUserWithFutureBirthday() {
        UserControllerImpl userController = new UserControllerImpl();

        User user = new User();
        user.setLogin("testlogin");
        user.setEmail("test@example.com");
        user.setBirthday(LocalDate.now().plusDays(1));

        assertThrows(InputDataErrorException.class, () -> userController.createUser(user));
    }

    @Test
    void updateUser() {
        UserControllerImpl userController = new UserControllerImpl();

        User user = new User();
        user.setName("Test User");
        user.setLogin("testlogin");
        user.setEmail("test@example.com");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        User createdUser = userController.createUser(user);

        createdUser.setName("Updated User");
        createdUser.setLogin("updatedlogin");
        createdUser.setEmail("updated@example.com");
        createdUser.setBirthday(LocalDate.of(1991, 1, 1));
        User updatedUser = userController.updateUser(createdUser);

        assertEquals("Updated User", updatedUser.getName());
        assertEquals("updatedlogin", updatedUser.getLogin());
        assertEquals("updated@example.com", updatedUser.getEmail());
        assertEquals(LocalDate.of(1991, 1, 1), updatedUser.getBirthday());
    }

    @Test
    void updateUserNonExistentUser() {
        UserControllerImpl userController = new UserControllerImpl();

        User user = new User();
        user.setId(999);
        user.setName("Non Existent User");
        user.setLogin("nonexistentlogin");
        user.setEmail("nonexistent@example.com");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        assertThrows(UserNotExistException.class, () -> userController.updateUser(user));
    }
}