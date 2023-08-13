package ru.yandex.practicum.filmorate.controllers;


import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private int counter = 1;
    private final Map<Integer, User> users = new HashMap<>();


    @PostMapping
    public User addUser(@Valid @RequestBody User user) throws ValidationException {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин должен быть без пробелов");
        }
        user.setId(counter);
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(counter, user);
        User newUser = users.get(counter);
        counter++;
        return newUser;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {
        if (users.containsKey(user.getId())){
            users.put(user.getId(), user);
            return users.get(user.getId());
        } else {
            throw new ValidationException("Такого пользователя нет в системе");
        }
    }

    @GetMapping
    public List<User> getUsersList() {
        return new ArrayList<>(users.values());
    }

}
