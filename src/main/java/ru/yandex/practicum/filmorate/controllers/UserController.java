package ru.yandex.practicum.filmorate.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

/* UserController отвечает за обработку эндпойнтов по добавлению, обновлению юзеров и друзей */

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /* добавляет юзера */
    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        return userService.addUser(user);
    }

    /* обновляет юзера либо выбрасывает исключение */
    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    /* возвращает список сохраненных юзеров */
    @GetMapping
    public List<User> findUsersList() {
        return userService.findUsersList();
    }

    /* находит юзера по id */
    @GetMapping("/{id}")
    public User findUserById(@PathVariable long id) {
        return userService.findUserById(id);
    }

    /* добавляет в друзья юзера по id */
    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable long id, @PathVariable long friendId) {
        return userService.addFriend(id, friendId);
    }

    /* удаляет из друзей юзера по id */
    @DeleteMapping("/{id}/friends/{friendId}")
    public User removeFriend(@PathVariable long id, @PathVariable long friendId) {
        return userService.removeFriend(id, friendId);
    }

    /* возвращает список друзей юзера */
    @GetMapping("/{id}/friends")
    public List<User> findUserFriendList(@PathVariable long id) {
        return userService.findUserFriendList(id);
    }

    /* возвращает список общих друзей */
    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> findMutualFriends(@PathVariable long id, @PathVariable long otherId) {
        return userService.findMutualFriends(id, otherId);
    }
}
