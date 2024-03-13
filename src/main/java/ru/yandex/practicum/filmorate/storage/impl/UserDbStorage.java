package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.utility.UserValidator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserValidator userValidator;
    private long id = 0;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate, UserValidator userValidator) {
        this.jdbcTemplate = jdbcTemplate;
        this.userValidator = userValidator;
    }

    @Override
    public List<User> getUsers() {
        String sql = "SELECT * FROM users";

        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToUser(rs));
    }

    @Override
    public User createUser(User user) {
        userValidator.validateUserForCreation(user);
        userValidator.validateUserName(user);

        id++;
        user.setId(id);

        String sql = "INSERT INTO users (user_id, name, email, birthday, login) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getId(), user.getName(), user.getEmail(), user.getBirthday(), user.getLogin());

        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!userValidator.isUserExists(user.getId())) {
            throw new UserNotExistException("There is no such user in the database");
        } else {
            String sql = "UPDATE users SET name = ?, email = ?, birthday = ?, login = ? WHERE user_id = ?";
            jdbcTemplate.update(sql, user.getName(), user.getEmail(), user.getBirthday(), user.getLogin(), user.getId());
        }
        return user;
    }

    @Override
    public User getUserById(long id) {
        String sql = "SELECT * FROM users WHERE user_id = ?";

        List<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToUser(rs), id);
        if (users.isEmpty()) {
            throw new UserNotExistException("User not found");
        }

        return users.get(0);

    }

    @Override
    public void addFriend(long userOneId, long userTwoId, boolean status) {
        String sql = "INSERT INTO FRIENDSHIP (USER_ID, FRIEND_ID, STATUS) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, userOneId, userTwoId, status);
    }

    @Override
    public void updateFriendshipStatus(long userOneId, long userTwoId, boolean status) {
        String sql = "UPDATE FRIENDSHIP SET STATUS = ? WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sql, status, userOneId, userTwoId);
    }

    @Override
    public boolean isFriendAlready(long userOneId, long userTwoId) {
        String sql = "SELECT COUNT(*) FROM FRIENDSHIP WHERE USER_ID = ? AND FRIEND_ID = ?";
        Integer count = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getInt(1), userOneId, userTwoId);
        return count != null && count > 0;
    }

    @Override
    public List<Long> getFriendsIds(long userId) {
        String sql = "SELECT FRIEND_ID FROM FRIENDSHIP WHERE USER_ID = ?";
        return jdbcTemplate.queryForList(sql, Long.class, userId);
    }

    @Override
    public void removeFriend(long userOneId, long userTwoId) {
        String sql = "DELETE FROM FRIENDSHIP WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sql, userOneId, userTwoId);
    }

    @Override
    public void setFriendshipStatusFalse(long userOneId, long userTwoId) {
        String sql = "UPDATE FRIENDSHIP SET STATUS = false WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sql, userOneId, userTwoId);
    }

    @Override
    public List<Long> getMutualFriends(long userOneId, long userTwoId) {
        String sql = "SELECT FRIEND_ID FROM FRIENDSHIP WHERE USER_ID = ? AND FRIEND_ID IN (SELECT FRIEND_ID FROM FRIENDSHIP WHERE USER_ID = ?)";
        return jdbcTemplate.queryForList(sql, Long.class, userOneId, userTwoId);
    }

    private User mapRowToUser(ResultSet rs) throws SQLException {

        long id = rs.getLong("user_id");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();

        return User.builder()
                .id(id)
                .email(email)
                .login(login)
                .name(name)
                .birthday(birthday)
                .build();
    }

}

