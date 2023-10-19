package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.UserValidation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/* InMemoryUserStorage отвечает ха хранение данных о пользователях в оперативной памяти */

@Component
public class InMemoryUserStorage implements UserStorage {

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
    public List<User> findUserFriendList(long id) {
        return findUserById(id).getUserFriends().stream()
                .map(this::findUserById)
                .collect(Collectors.toList());
    }

    @Override
    public User findUserById(long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new UserNotFoundException("Такого пользователя нет в системе");
        }
    }

    /* добавлены заглушки на неиспользуемые методы */

    @Override
    public Friendship findFriendship(long user1Id, long user2Id) {
        return null;
    }

    @Override
    public Friendship updateFriendship(Friendship friendship) {
        return null;
    }

    @Override
    public void removeFriendship(Friendship friendship) {
    }
}
