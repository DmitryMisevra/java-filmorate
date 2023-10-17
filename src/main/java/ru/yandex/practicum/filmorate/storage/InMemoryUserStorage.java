package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.UserValidation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {

    /* InMemoryUserStorage хранит данные о пользователях в оперативной памяти */
    private Long counter = 1L; /* счетчик id */
    private final Map<Long, User> users = new HashMap<>(); /* мапа, где хранятся фильмы. id - ключ */


    @Override
    public User addUser(User user) throws ValidationException {
        UserValidation.validateLogin(user);
        UserValidation.setDefaultName(user);
        user.setId(counter);
        users.put(counter, user);
        User newUser = users.get(counter);
        counter++;
        return newUser;
    }

    @Override
    public User updateUser(User user) throws UserNotFoundException {
        if (users.containsKey(user.getId())) {
            UserValidation.validateLogin(user);
            UserValidation.setDefaultName(user);
            users.put(user.getId(), user);
            return users.get(user.getId());
        } else {
            throw new UserNotFoundException("Такого пользователя нет в системе");
        }
    }

    @Override
    public List<User> findUsersList() {
        return new ArrayList<>(users.values());
    }

    @Override
    public List<User> findUserFriendList(Long id) {
        return findUserById(id).getUserFriends().stream()
                .map(this::findUserById)
                .collect(Collectors.toList());
    }

    @Override
    public User findUserById(Long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new UserNotFoundException("Такого пользователя нет в системе");
        }
    }
}
