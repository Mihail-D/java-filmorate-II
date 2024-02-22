package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class UserService {
    UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User getUserById(long id) {
        List<User> users = userStorage.getUsers();
        for (User user : users) {
            if (user.getId() == id) {
                return user;
            }
        }
        throw new UserNotExistException("User with id " + id + " not found");
    }

    public User addFriend(long friendOneId, long friendTwoId) {
        User friendOne = getUserById(friendOneId);
        User friendTwo = getUserById(friendTwoId);

        Set<User> friendsSetOne = friendOne.getFriends();
        Set<User> friendsSetTwo = friendTwo.getFriends();

        friendsSetOne.add(getUserById(friendTwoId));
        friendsSetTwo.add(getUserById(friendOneId));

        log.info("friendOne длина списка друзей " + friendsSetOne.size());
        log.info("friendTwo длина списка друзей " + friendsSetTwo.size());

        userStorage.updateUser(friendOne);
        userStorage.updateUser(friendTwo);

        return friendOne;
    }
}
// *** GET .../users/{id}
// PUT /users/{id}/friends/{friendId}
// DELETE /users/{id}/friends/{friendId}
// GET /users/{id}/friends                  список пользователей, являющихся его друзьями
// GET /users/{id}/friends/common/{otherId} список друзей, общих с другим пользователем
