package ru.yandex.practicum.filmorate.controllers;


import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

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

    /* обновляет юзера либо выбрасывает исключение */
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

    /* обновляет список сохраненных юзеров */
    @GetMapping
    public List<User> getUsersList() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return new ArrayList<>(users.values());
    }

    /* Очищает память контроллера. Сейчас используется для тестов */
    public void clearUserController() {
        counter = 1;
        users.clear();
    }

    /* Вспомогательный метод. Валидирует логины на отсутствие пробелов */
    private void validateLogin(User user) throws ValidationException {
        if (user.getLogin().contains(" ")) {
            log.error("в логине обнаружены пробелы: {}", user.getLogin());
            throw new ValidationException("Логин должен быть без пробелов");
        }
    }

    /* Вспомогательный метод. Если имя пустое, именем становится логин */
    private void setDefaultName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("Имени присвоено значение логина. Значение логин: {}, значенрие имени:  {}", user.getLogin(),
                    user.getName());
        }
    }
}
