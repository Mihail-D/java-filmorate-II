package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
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
}
