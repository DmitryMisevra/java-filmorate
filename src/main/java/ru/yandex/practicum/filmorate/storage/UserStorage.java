package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User addUser(User user);

    User updateUser(User user);

    List<User> findUsersList();

    List<User> findUserFriendList(Long id);

    User findUserById(Long id);



}
