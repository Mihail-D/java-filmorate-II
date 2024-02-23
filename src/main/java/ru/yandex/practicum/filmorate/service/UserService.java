package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.utility.UserPairHandler;

import java.util.ArrayList;
import java.util.HashSet;
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
        User user = userStorage.getUserById(id);
        if (user == null) {
            throw new UserNotExistException("User with id " + id + " not found");
        }
        return user;
    }

    public List<User> getUserFriends(long id) {
        List<User> users = getUsers();
        Set<Long> friendsId = getUserById(id).getFriends();

        List<User> userFriends = new ArrayList<>();

        for (User i : new ArrayList<>(users)) {
            if (friendsId.contains(i.getId())) {
                userFriends.add(i);
            }
        }

        return userFriends;
    }

    public User addFriend(long userOneId, long userTwoId) {
        UserPairHandler userPairHandler = new UserPairHandler(userOneId, userTwoId, userStorage);
        userPairHandler.addFriend();

        return userStorage.getUserById(userOneId);
    }

    public void removeFriend(long userOneId, long userTwoId) {
        UserPairHandler userPairHandler = new UserPairHandler(userOneId, userTwoId, userStorage);
        userPairHandler.removeFriend();
    }

    public List<User> getMutualFriends(long id, long otherId) {
        User userOne = userStorage.getUserById(id);
        User userTwo = userStorage.getUserById(otherId);

        if (userOne == null || userTwo == null) {
            throw new UserNotExistException("User not found");
        }

        Set<Long> mutualFriendsIds = new HashSet<>(userOne.getFriends());
        mutualFriendsIds.retainAll(userTwo.getFriends());

        List<User> mutualFriends = new ArrayList<>();
        for (Long friendId : mutualFriendsIds) {
            mutualFriends.add(userStorage.getUserById(friendId));
        }

        return mutualFriends;
    }

}
