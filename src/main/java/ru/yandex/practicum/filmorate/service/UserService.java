package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
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

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User getUserById(long id) {
        List<User> users = getUsers();
        for (User user : users) {
            if (user.getId() == id) {
                return user;
            }
        }
        throw new UserNotExistException("User with id " + id + " not found");
    }

    private void addFriendToUser(User user, long friendId) {
        user.getFriends().add(friendId);
        userStorage.updateUser(user);
    }

    public User addFriend(long friendOneId, long friendTwoId) {
        User friendOne = getUserById(friendOneId);
        User friendTwo = getUserById(friendTwoId);

        addFriendToUser(friendOne, friendTwoId);
        addFriendToUser(friendTwo, friendOneId);

        return friendOne;
    }

    public List<User> getUserFriends(long id) {
        List<User> users = getUsers();
        Set<Long> friendsId = getUserById(id).getFriends();
        List<User> userFriends = new ArrayList<>();

        for (User i : users) {
            if (friendsId.contains(i.getId())) {
                userFriends.add(i);
            }
        }

        return userFriends;
    }

    private void removeFriendFromUser(User user, long friendId) {
        user.getFriends().remove(friendId);
        userStorage.updateUser(user);
    }

    public void removeFriend(long friendOneId, long friendTwoId) {
        User friendOne = getUserById(friendOneId);
        User friendTwo = getUserById(friendTwoId);

        removeFriendFromUser(friendOne, friendTwoId);
        removeFriendFromUser(friendTwo, friendOneId);
    }


}
// *** GET .../users/{id}
// *** PUT /users/{id}/friends/{friendId}
// *** DELETE /users/{id}/friends/{friendId}
// *** GET /users/{id}/friends                  список пользователей, являющихся его друзьями
// GET /users/{id}/friends/common/{otherId} список друзей, общих с другим пользователем
