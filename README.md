# Это ER-диаграмма для проекта Java-Filmorate
![](https://github.com/DmitryMisevra/java-filmorate/blob/c775005036ca262070b1728929e8d5e9548b9abe/Screenshot%202023-10-18%20at%2000.45.28.png)

**В схеме отражены следующие сущности:**
1. _FILM_USER_. Хранит данные о пользователях
2. _FILM_. Хранит данных о фильмах
3. _GENRE_. Хранит список жанров
4. _FILM_GENRES_. Хранит соответствие фильмов и жанров между собой
5. _MPA_. Хранит список рейтингов фильма
6. _LIKES_. Хранит лайки пользовталей
7. _FRIENDSHIP_. хранит данные о дружеских связях между пользователями

**Основные операции моего приложения:**

Операции с пользователями:
1. Добавить друга _(addFriend(Long id, Long friendId))_
2. Удалить друга (removeFriend(Long id, Long friendId))
3. Вернуть список общих друзей с одним из пользователей _(findMutualFriends(Long id, Long friendId))_
4. Добавить пользователя _(addUser(User user))_
5. Обновить информацию о пользователе _(updateUser(User user))_
6. Вернуть список всех пользователей _(findUsersList())_
7. Вернуть список всех друзей _(findUserFriendList(Long id))_
8. Найти пользователя по id _(findUserById(Long id))_

Операции с фильмами:
1. Добавить лайк _(addLike(Long id, Long userId))_
2. Удалить лайк (removeLike(Long id, Long userId))
3. Вернуть список самых популярных фильмов _(findPopularFilms(Long count))_
4. Добавить фильм _(addFilm(Film film))_
5. Обновить информацию о информацию о фильме _(updateFilm(Film film))_
6. Вернуть фильм по его id _(findFilmById(Long id))_
7. Вернуть список всех фильмов _(findFilmById(Long id))_

