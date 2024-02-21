package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

}
// GET .../users/{id}
// PUT /users/{id}/friends/{friendId}
// DELETE /users/{id}/friends/{friendId}
// GET /users/{id}/friends                  список пользователей, являющихся его друзьями
// GET /users/{id}/friends/common/{otherId} список друзей, общих с другим пользователем
