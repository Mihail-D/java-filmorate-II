package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Getter
@Setter
public class User {

    @NotBlank
    private long id;

    @Email
    String email;

    @NotBlank
    String login;

    @NotBlank
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
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + getEmail().hashCode();
        result = 31 * result + getLogin().hashCode();
        result = 31 * result + getName().hashCode();
        result = 31 * result + getBirthday().hashCode();
        return result;
    }
}
