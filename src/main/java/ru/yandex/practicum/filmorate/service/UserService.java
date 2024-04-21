package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/* UserService отвечает за добавление и удаление друзей, вывод списка взаимных друзей с другим юзером  */

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("UserDBStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    /* добавляет друга */
    public User addFriend(long id, long friendId) {
        if (id == friendId) {
            throw new ValidationException("id пользователей не могут быть одинаковыми");
        }
        userStorage.findUserById(friendId);
        Friendship friendship = userStorage.findFriendship(id, friendId);
        if (friendship != null) {
            if (friendship.isConfirmed()) {
                throw new ValidationException(String.format("Пользователи %s и %s уже являются друзьями. Статус " +
                        "дружбы: %s", id, friendId, true));
            } else if (friendship.getUser2Id() == friendId) {
                friendship.setConfirmed(true);
                userStorage.updateFriendship(friendship);
            } else {
                throw new ValidationException(String.format("Пользователь %s уже отправил ранее запрос пользователю " +
                        "%s. Статус дружбы: %s", id, friendId, false));
            }
        } else {
            friendship = new Friendship(friendId, id, false);
            userStorage.updateFriendship(friendship);
        }

        return userStorage.findUserById(id);
    }

    /* удаляет друга */
    public User removeFriend(long id, long friendId) {
        if (id == friendId) {
            throw new ValidationException("id пользователей не могут быть одинаковыми");
        }
        userStorage.findUserById(friendId);

        Friendship friendship = userStorage.findFriendship(id, friendId);
        if (friendship != null) {
            userStorage.removeFriendship(friendship);
        } else {
            throw new ValidationException(String.format("Не найдена дружба между пользователями %s и %s", id,
                    friendId));
        }
        return userStorage.findUserById(id);
    }

    /* находит общих друзей */
    public List<User> findMutualFriends(Long id, Long friendId) {
        User userOne = userStorage.findUserById(id);
        User userTwo = userStorage.findUserById(friendId);

        Set<Long> userOneFriends = userOne.getUserFriends();
        Set<Long> userTwoFriends = userTwo.getUserFriends();

        Set<Long> mutualFriends = new HashSet<>(userOneFriends);
        mutualFriends.retainAll(userTwoFriends);
        return mutualFriends.stream().map(userStorage::findUserById).collect(Collectors.toList());
    }

    /* ниже сквозные методы UserStorage */
    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public List<User> findUsersList() {
        return userStorage.findUsersList();
    }

    public List<User> findUserFriendList(Long id) {
        return userStorage.findUserFriendList(id);
    }

    public User findUserById(Long id) {
        return userStorage.findUserById(id);
    }
}
