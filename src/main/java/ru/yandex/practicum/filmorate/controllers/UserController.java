package ru.yandex.practicum.filmorate.controllers;


import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private int counter = 1;
    private final Map<Integer, User> users = new HashMap<>();


    @PostMapping
    public User addUser(@Valid @RequestBody User user) throws ValidationException {
        validateLogin(user);
        setDefaultName(user);
        user.setId(counter);
        users.put(counter, user);
        User newUser = users.get(counter);
        log.debug("Добавили пользователя: {}", newUser);
        counter++;
        log.debug("Состояние счетчика id пользователей: {}", counter);
        return newUser;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {
        if (users.containsKey(user.getId())) {
            validateLogin(user);
            setDefaultName(user);
            users.put(user.getId(), user);
            log.debug("Обновили пользователя: {}", users.get(user.getId()));
            return users.get(user.getId());
        } else {
            log.error("следующего id пользователя нет в списке: {}", user.getId());
            throw new ValidationException("Такого пользователя нет в системе");
        }
    }

    @GetMapping
    public List<User> getUsersList() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return new ArrayList<>(users.values());
    }

    private void validateLogin(User user) {
        if (user.getLogin().contains(" ")) {
            log.error("в логине обнаружены пробелы: {}", user.getLogin());
            throw new ValidationException("Логин должен быть без пробелов");
        }
    }

    private void setDefaultName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("Имени присвоено значение логина. Значение логин: {}, значенрие имени:  {}", user.getLogin(),
                    user.getName());
        }
    }
}
