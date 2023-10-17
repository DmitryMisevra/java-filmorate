# Это ER-диаграмма для проекта Java-Filmorate

**В схеме отражены следующие сущности:**
1. _user_. Отвечает за хранение данных пользователей
2. _film_. Отвечает за хранение данных о фильме
3. _genre_. Хранит список жанров и отражен в film
4. _rating_. Хранит список рейтингов фильма и также отражен в film
5. _film-likes_. Хранит все лайки для всех фильмов
6. _friendship_. хранит данные о дружеских связях между пользователями
7. _friendship_status_. Хранит статусы дружбы между пользователями и отражен в таблице friendship

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

