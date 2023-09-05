package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    /* UserStorage отвечает за добавление, удаление, и обновление данных юзера, его поиск по id и вывод списка друзей */

    /* добавляет юзера */
    User addUser(User user);


    /* обновляет юзера либо выбрасывает исключение */
    User updateUser(User user);

    /* возвращает  список сохраненных юзеров */
    List<User> findUsersList();

    /* возвращает список друзей юзера */
    List<User> findUserFriendList(Long id);

    /* находит юзера по id */
    User findUserById(Long id);



}
