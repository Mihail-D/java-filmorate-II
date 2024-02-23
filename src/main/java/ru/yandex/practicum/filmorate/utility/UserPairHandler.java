package ru.yandex.practicum.filmorate.utility;

import ru.yandex.practicum.filmorate.exceptions.UserNotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Set;

public class UserPairHandler {

    private final User userOne;
    private final User userTwo;
    private final UserStorage userStorage;

    public UserPairHandler(long userOneId, long userTwoId, UserStorage userStorage) {
        this.userStorage = userStorage;
        this.userOne = userStorage.getUserById(userOneId);
        this.userTwo = userStorage.getUserById(userTwoId);
    }

    public void addFriend() {
        if (userOne == null || userTwo == null) {
            throw new UserNotExistException("User not found");
        }

        Set<Long> userOneFriends = userOne.getFriends();
        Set<Long> userTwoFriends = userTwo.getFriends();

        userOneFriends.add(userTwo.getId());
        userTwoFriends.add(userOne.getId());

        userStorage.updateUser(userOne);
        userStorage.updateUser(userTwo);
    }

    public void removeFriend() {
        Set<Long> userOneFriends = userOne.getFriends();
        Set<Long> userTwoFriends = userTwo.getFriends();

        userOneFriends.remove(userTwo.getId());
        userTwoFriends.remove(userOne.getId());

        userStorage.updateUser(userOne);
        userStorage.updateUser(userTwo);
    }
}
