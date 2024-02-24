package ru.yandex.practicum.filmorate.utility;

import ru.yandex.practicum.filmorate.exceptions.UserNotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Set;

public class UserPairHandler {

    private final User userOne;
    private final User userTwo;
    private final UserService userService;

    public UserPairHandler(long userOneId, long userTwoId, UserService userService) {
        this.userService = userService;
        this.userOne = userService.getUserById(userOneId);
        this.userTwo = userService.getUserById(userTwoId);

        if (userOne == null || userTwo == null) {
            throw new UserNotExistException("User not found");
        }
    }

    public void addFriend() {
        Set<Long> userOneFriends = userOne.getFriends();
        Set<Long> userTwoFriends = userTwo.getFriends();

        userOneFriends.add(userTwo.getId());
        userTwoFriends.add(userOne.getId());

        updateUser();
    }

    public void removeFriend() {
        Set<Long> userOneFriends = userOne.getFriends();
        Set<Long> userTwoFriends = userTwo.getFriends();

        userOneFriends.remove(userTwo.getId());
        userTwoFriends.remove(userOne.getId());

        updateUser();
    }

    private void updateUser() {
        userService.updateUser(userOne);
        userService.updateUser(userTwo);
    }
}