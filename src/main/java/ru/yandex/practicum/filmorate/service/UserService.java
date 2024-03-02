package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

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

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public User getUserById(long id) {
        User user = userStorage.getUserById(id);
        if (user == null) {
            throw new UserNotExistException("User with id " + id + " not found");
        }
        return user;
    }

    public User addFriend(long userOneId, long userTwoId) {
        User userOne = getUserById(userOneId);
        User userTwo = getUserById(userTwoId);

        if (userOne == null || userTwo == null) {
            throw new UserNotExistException("User not found");
        }

        boolean isFriendAlready = userStorage.isFriendAlready(userOneId, userTwoId);

        if (isFriendAlready) {
            userStorage.updateFriendshipStatus(userOneId, userTwoId, true);
            userStorage.addFriend(userOneId, userTwoId, true);
        } else {
            userStorage.addFriend(userOneId, userTwoId, false);
        }

        return userOne;
    }

    public List<User> getUserFriends(long id) {
        User user = getUserById(id);
        if (user == null) {
            throw new UserNotExistException("User with id " + id + " not found");
        }

        List<Long> friendsIds = userStorage.getFriendsIds(id);
        List<User> friends = new ArrayList<>();
        for (Long friendId : friendsIds) {
            User friend = userStorage.getUserById(friendId);
            if (friend != null) {
                friends.add(friend);
            }
        }

        return friends;
    }

/*    public void removeFriend(long userOneId, long userTwoId) {
        User userOne = getUserById(userOneId);
        User userTwo = getUserById(userTwoId);

        if (userOne == null || userTwo == null) {
            throw new UserNotExistException("User not found");
        }

        userOne.getFriends().remove(userTwo.getId());
        userTwo.getFriends().remove(userOne.getId());

        updateUser(userOne);
        updateUser(userTwo);
    }*/

/*    public List<User> getMutualFriends(long id, long otherId) {
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
    }*/
}