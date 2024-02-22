package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter

public class User {

    private final Set<Long> friends = new HashSet<>();
    long id;

    @Email
    String email;

    @NotBlank
    String login;

    String name;

    @Past
    LocalDate birthday;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User user = (User) o;

        if (getId() != user.getId()) {
            return false;
        }
        if (getFriends() != null ? !getFriends().equals(user.getFriends()) : user.getFriends() != null) {
            return false;
        }
        if (!getEmail().equals(user.getEmail())) {
            return false;
        }
        if (!getLogin().equals(user.getLogin())) {
            return false;
        }
        if (!getName().equals(user.getName())) {
            return false;
        }
        return getBirthday().equals(user.getBirthday());
    }

    @Override
    public int hashCode() {
        int result = getFriends() != null ? getFriends().hashCode() : 0;
        result = 31 * result + (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + getEmail().hashCode();
        result = 31 * result + getLogin().hashCode();
        result = 31 * result + getName().hashCode();
        result = 31 * result + getBirthday().hashCode();
        return result;
    }
}
