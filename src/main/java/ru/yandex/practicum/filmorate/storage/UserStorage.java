package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

/* UserStorage отвечает за добавление, удаление, и обновление данных юзера, его поиск по id и вывод списка друзей */

public interface UserStorage {

    /* добавляет юзера */
    User addUser(User user);


    /* обновляет юзера либо выбрасывает исключение */
    User updateUser(User user);

    /* возвращает  список сохраненных юзеров */
    List<User> findUsersList();

    /* возвращает список друзей юзера */
    List<User> findUserFriendList(long id);

    /* находит юзера по id */
    User findUserById(long id);

    /* возвращает статус дружбы между двумя пользователями. Используется в сервисном слое */
    Friendship findFriendship(long user1Id, long user2Id);

    /* обновляет статус дружбы между пользователями. Используется в сервисном слое */
    Friendship updateFriendship(Friendship friendship);

    /* удаляет дружбу между пользователями. Используется в сервисном слое */
    void removeFriendship(Friendship friendship);
}
