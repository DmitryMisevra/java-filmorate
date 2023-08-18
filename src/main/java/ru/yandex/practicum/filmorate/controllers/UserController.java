package ru.yandex.practicum.filmorate.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.UserValidation;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private int counter = 1; /* счетчик id */
    private final Map<Integer, User> users = new HashMap<>(); /* мапа, где хранятся фильмы. id - ключ */

    /* добавляет юзера */
    @PostMapping
    public User addUser(@Valid @RequestBody User user) throws ValidationException {
        UserValidation.validateLogin(user);
        UserValidation.setDefaultName(user);
        user.setId(counter);
        users.put(counter, user);
        User newUser = users.get(counter);
        log.debug("Добавили пользователя: {}", newUser);
        counter++;
        log.debug("Состояние счетчика id пользователей: {}", counter);
        return newUser;
    }

    /* обновляет юзера либо выбрасывает исключение */
    @PutMapping
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {
        if (users.containsKey(user.getId())) {
            UserValidation.validateLogin(user);
            UserValidation.setDefaultName(user);
            users.put(user.getId(), user);
            log.debug("Обновили пользователя: {}", users.get(user.getId()));
            return users.get(user.getId());
        } else {
            log.error("следующего id пользователя нет в списке: {}", user.getId());
            throw new ValidationException("Такого пользователя нет в системе");
        }
    }

    /* обновляет список сохраненных юзеров */
    @GetMapping
    public List<User> getUsersList() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return new ArrayList<>(users.values());
    }
}
