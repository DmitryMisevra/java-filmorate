package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.MpaNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FilmorateApplicationTests {

    private final UserDbStorage userStorage;
    private final UserService userService;
    private final FilmDbStorage filmStorage;

    @Test
    public void testAddUser() {
        User user = User.builder()
                .name("dolore")
                .email("mail@mail.ru")
                .login("dolore")
                .birthday(LocalDate.of(1946, 8, 20))
                .build();

        Optional<User> userOptional = Optional.ofNullable(userStorage.addUser(user));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(us -> {
                    assertThat(us).hasFieldOrPropertyWithValue("id", 1L);
                    assertThat(us).hasFieldOrPropertyWithValue("name", user.getName());
                    assertThat(us).hasFieldOrPropertyWithValue("email", user.getEmail());
                    assertThat(us).hasFieldOrPropertyWithValue("login", user.getLogin());
                    assertThat(us).hasFieldOrPropertyWithValue("birthday", user.getBirthday());
                });
    }

    @Test
    public void testAddUserShouldThrownValidationExceptionWithFailLogin() {
        User user = User.builder()
                .email("yandex@mail.ru")
                .login("dolore ullamco")
                .birthday(LocalDate.of(1991, 9, 29))
                .build();

        assertThatThrownBy(() -> userStorage.addUser(user))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Логин должен быть без пробелов");
    }

    @Test
    public void testAddUserSetNameAsLoginWithEmptyName() {
        User user = User.builder()
                .name("")
                .email("mail@mail.ru")
                .login("dolore")
                .birthday(LocalDate.of(1946, 8, 20))
                .build();

        Optional<User> userOptional = Optional.ofNullable(userStorage.addUser(user));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(us -> assertThat(us).hasFieldOrPropertyWithValue("name", user.getLogin()));
    }

    @Test
    public void testUpdateUser() {
        User user = User.builder()
                .name("Name")
                .email("email@example.com")
                .login("login")
                .birthday(LocalDate.of(1991, 9, 29))
                .build();
        User savedUser = userStorage.addUser(user);

        User updatedUser = User.builder()
                .id(savedUser.getId())
                .name("New Name")
                .email("newemail@example.com")
                .login("newLogin")
                .birthday(LocalDate.of(1989, 12, 12))
                .build();

        Optional<User> userOptional = Optional.ofNullable(userStorage.updateUser(updatedUser));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(us -> {
                    assertThat(us).hasFieldOrPropertyWithValue("id", updatedUser.getId());
                    assertThat(us).hasFieldOrPropertyWithValue("name", updatedUser.getName());
                    assertThat(us).hasFieldOrPropertyWithValue("email", updatedUser.getEmail());
                    assertThat(us).hasFieldOrPropertyWithValue("login", updatedUser.getLogin());
                    assertThat(us).hasFieldOrPropertyWithValue("birthday", updatedUser.getBirthday());
                });
    }

    @Test
    public void testUpdateUserShouldThrowUserNotFoundExceptionWhenUnknownUser() {
        User user = User.builder()
                .id(9999)
                .name("New Name")
                .email("newemail@example.com")
                .login("newLogin")
                .birthday(LocalDate.of(1989, 12, 12))
                .build();

        assertThatThrownBy(() -> userStorage.updateUser(user))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage(String.format("Пользователя с id %s нет в системе", 9999));
    }

    @Test
    public void findUsersListTest() {
        User userOne = User.builder()
                .name("dolore")
                .email("mail@mail.ru")
                .login("dolore")
                .birthday(LocalDate.of(1946, 8, 20))
                .build();

        User userTwo = User.builder()
                .name("marcus")
                .email("yandex@mail.ru")
                .login("marcus38")
                .birthday(LocalDate.of(1995, 12, 22))
                .build();

        userStorage.addUser(userOne);
        userStorage.addUser(userTwo);

        List<User> userList = userStorage.findUsersList();


        assertThat(userList).hasSize(2);
        assertThat(userList.get(0)).hasFieldOrPropertyWithValue("name", userOne.getName());
        assertThat(userList.get(1)).hasFieldOrPropertyWithValue("name", userTwo.getName());
    }

    @Test
    public void testFindUserFriendList() {
        User userOne = User.builder()
                .name("dolore")
                .email("mail@mail.ru")
                .login("dolore")
                .birthday(LocalDate.of(1946, 8, 20))
                .build();

        User userTwo = User.builder()
                .name("marcus")
                .email("yandex@mail.ru")
                .login("marcus38")
                .birthday(LocalDate.of(1995, 12, 22))
                .build();

        userStorage.addUser(userOne);
        userStorage.addUser(userTwo);
        userService.addFriend(1, 2);

        List<User> userFriendList = userStorage.findUserFriendList(1);

        assertThat(userFriendList).hasSize(1);
        assertThat(userFriendList.get(0)).hasFieldOrPropertyWithValue("name", userTwo.getName());
    }

    @Test
    public void testFindUserById() {
        User user = User.builder()
                .name("dolore")
                .email("mail@mail.ru")
                .login("dolore")
                .birthday(LocalDate.of(1946, 8, 20))
                .build();

        userStorage.addUser(user);

        Optional<User> userOptional = Optional.ofNullable(userStorage.findUserById(1));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(usr ->
                        assertThat(usr).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void testFindUserByIdShouldThrowUserNotFoundExceptionWhenUserIdIsUnknown() {
        assertThatThrownBy(() -> userStorage.findUserById(9999))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage(String.format("Пользователя с id %s нет в системе", 9999));
    }

    @Test
    public void testFindFriendship() {
        User userOne = User.builder()
                .name("dolore")
                .email("mail@mail.ru")
                .login("dolore")
                .birthday(LocalDate.of(1946, 8, 20))
                .build();

        User userTwo = User.builder()
                .name("marcus")
                .email("yandex@mail.ru")
                .login("marcus38")
                .birthday(LocalDate.of(1995, 12, 22))
                .build();

        userStorage.addUser(userOne);
        userStorage.addUser(userTwo);
        userService.addFriend(1, 2);

        Optional<Friendship> friendshipOptional = Optional.ofNullable(userStorage.findFriendship(1, 2));
        assertThat(friendshipOptional)
                .isPresent()
                .hasValueSatisfying(f -> {
                    assertThat(f).hasFieldOrPropertyWithValue("user1Id", friendshipOptional.get().getUser1Id());
                    assertThat(f).hasFieldOrPropertyWithValue("user2Id", friendshipOptional.get().getUser2Id());
                    assertThat(f).hasFieldOrPropertyWithValue("isConfirmed",
                            friendshipOptional.get().isConfirmed());
                });
    }

    @Test
    public void testUpdateFriendship() {
        User userOne = User.builder()
                .name("dolore")
                .email("mail@mail.ru")
                .login("dolore")
                .birthday(LocalDate.of(1946, 8, 20))
                .build();

        User userTwo = User.builder()
                .name("marcus")
                .email("yandex@mail.ru")
                .login("marcus38")
                .birthday(LocalDate.of(1995, 12, 22))
                .build();

        userStorage.addUser(userOne);
        userStorage.addUser(userTwo);
        userService.addFriend(1, 2);
        userService.addFriend(2, 1);

        Optional<Friendship> friendshipOptional = Optional.ofNullable(userStorage.findFriendship(2, 1));
        assertThat(friendshipOptional)
                .isPresent()
                .hasValueSatisfying(f -> {
                    assertThat(f).hasFieldOrPropertyWithValue("user1Id", friendshipOptional.get().getUser1Id());
                    assertThat(f).hasFieldOrPropertyWithValue("user2Id", friendshipOptional.get().getUser2Id());
                    assertThat(f).hasFieldOrPropertyWithValue("isConfirmed",
                            friendshipOptional.get().isConfirmed());
                });
    }

    @Test
    public void testRemoveFriendship() {
        User userOne = User.builder()
                .name("dolore")
                .email("mail@mail.ru")
                .login("dolore")
                .birthday(LocalDate.of(1946, 8, 20))
                .build();

        User userTwo = User.builder()
                .name("marcus")
                .email("yandex@mail.ru")
                .login("marcus38")
                .birthday(LocalDate.of(1995, 12, 22))
                .build();

        userStorage.addUser(userOne);
        userStorage.addUser(userTwo);
        userService.addFriend(1, 2);
        userService.removeFriend(1, 2);

        Optional<Friendship> friendshipOptional = Optional.ofNullable(userStorage.findFriendship(1, 2));
        assertThat(friendshipOptional).isEmpty();
    }

    @Test
    public void testAddFilm() {
        Mpa mpa = Mpa.builder().id(3).build();
        Film film = Film.builder()
                .name("New Film")
                .description("Test Description")
                .releaseDate(LocalDate.parse("1999-04-30"))
                .duration(120)
                .mpa(mpa)
                .build();

        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.addFilm(film));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(us -> {
                    assertThat(us).hasFieldOrPropertyWithValue("id", 1L);
                    assertThat(us).hasFieldOrPropertyWithValue("name", film.getName());
                    assertThat(us).hasFieldOrPropertyWithValue("description", film.getDescription());
                    assertThat(us).hasFieldOrPropertyWithValue("releaseDate", film.getReleaseDate());
                    assertThat(us.getMpa().getId()).isEqualTo(film.getMpa().getId());
                });
    }

    @Test
    public void testAddFilmShouldThrowValidationExceptionWhenInvalidReleaseDate() {
        Mpa mpa = Mpa.builder().id(3).build();
        Film film = Film.builder()
                .name("New Film")
                .description("Test Description")
                .releaseDate(LocalDate.parse("1895-12-27"))
                .duration(120)
                .mpa(mpa)
                .build();


        assertThatThrownBy(() -> filmStorage.addFilm(film))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Дата релиза не может быть ранее 28 декабря 1895 года");
    }

    @Test
    public void testUpdateFilm() {
        Mpa mpa = Mpa.builder().id(3).build();
        Mpa newMpa = Mpa.builder().id(2).build();
        Film film = Film.builder()
                .name("New Film")
                .description("Test Description")
                .releaseDate(LocalDate.parse("1999-04-30"))
                .duration(120)
                .mpa(mpa)
                .build();

        Film savedFilm = filmStorage.addFilm(film);

        Film updatedFilm = Film.builder()
                .id(savedFilm.getId())
                .name("Newest Film")
                .description("Test Description 2")
                .releaseDate(LocalDate.parse("1900-12-13"))
                .duration(100)
                .mpa(newMpa)
                .build();

        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.updateFilm(updatedFilm));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(us -> {
                    assertThat(us).hasFieldOrPropertyWithValue("id", 1L);
                    assertThat(us).hasFieldOrPropertyWithValue("name", updatedFilm.getName());
                    assertThat(us).hasFieldOrPropertyWithValue("description", updatedFilm.getDescription());
                    assertThat(us).hasFieldOrPropertyWithValue("releaseDate", updatedFilm.getReleaseDate());
                    assertThat(us.getMpa().getId()).isEqualTo(updatedFilm.getMpa().getId());
                });
    }

    @Test
    public void testUpdateFilmShouldThrowInvalidExceptionWithWrongId() {
        Mpa newMpa = Mpa.builder().id(2).build();
        Film updatedFilm = Film.builder()
                .id(3)
                .name("Newest Film")
                .description("Test Description 2")
                .releaseDate(LocalDate.parse("1900-12-13"))
                .duration(100)
                .mpa(newMpa)
                .build();

        assertThatThrownBy(() -> filmStorage.updateFilm(updatedFilm))
                .isInstanceOf(FilmNotFoundException.class)
                .hasMessage(String.format("Фильма с id %s нет в системе", 3));
    }

    @Test
    public void testFindFilmById() {
        Mpa mpa = Mpa.builder().id(3).build();
        Film film = Film.builder()
                .name("New Film")
                .description("Test Description")
                .releaseDate(LocalDate.parse("1999-04-30"))
                .duration(120)
                .mpa(mpa)
                .build();

        filmStorage.addFilm(film);

        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.findFilmById(1));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(us -> {
                    assertThat(us).hasFieldOrPropertyWithValue("id", 1L);
                    assertThat(us).hasFieldOrPropertyWithValue("name", film.getName());
                    assertThat(us).hasFieldOrPropertyWithValue("description", film.getDescription());
                    assertThat(us).hasFieldOrPropertyWithValue("releaseDate", film.getReleaseDate());
                    assertThat(us.getMpa().getId()).isEqualTo(film.getMpa().getId());
                });

    }

    @Test
    public void testFindFilmByIdShouldThrowFilmNotFoundExceptionWithWrongId() {
        assertThatThrownBy(() -> filmStorage.findFilmById(9999))
                .isInstanceOf(FilmNotFoundException.class)
                .hasMessage(String.format("Фильма с id %s нет в системе", 9999));
    }


    @Test
    public void testGetFilmsList() {
        Mpa mpa = Mpa.builder().id(3).build();
        Mpa newMpa = Mpa.builder().id(2).build();
        Film filmOne = Film.builder()
                .name("New Film")
                .description("Test Description")
                .releaseDate(LocalDate.parse("1999-04-30"))
                .duration(120)
                .mpa(mpa)
                .build();

        Film filmTwo = Film.builder()
                .name("Newest Film")
                .description("Test Description 2")
                .releaseDate(LocalDate.parse("1900-12-13"))
                .duration(100)
                .mpa(newMpa)
                .build();

        filmStorage.addFilm(filmOne);
        filmStorage.addFilm(filmTwo);

        List<Film> filmList = filmStorage.getFilmsList();

        assertThat(filmList).hasSize(2);
        assertThat(filmList.get(0)).hasFieldOrPropertyWithValue("name", filmOne.getName());
        assertThat(filmList.get(1)).hasFieldOrPropertyWithValue("name", filmTwo.getName());

    }

    @Test
    public void testGetGenresList() {
        List<Genre> genresList = filmStorage.getGenresList();

        assertThat(genresList).hasSize(6);
        assertThat(genresList.get(0)).hasFieldOrPropertyWithValue("name", "Комедия");
        assertThat(genresList.get(1)).hasFieldOrPropertyWithValue("name", "Драма");
    }

    @Test
    public void findGenreById() {
        Optional<Genre> genreOptional = Optional.ofNullable(filmStorage.findGenreByid(1));

        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(g -> {
                    assertThat(g).hasFieldOrPropertyWithValue("id", 1L);
                    assertThat(g).hasFieldOrPropertyWithValue("name", "Комедия");
                });
    }

    @Test
    public void findGenreByIdShouldThrowGenreNotFoundExceptionWithWrongId() {
        assertThatThrownBy(() -> filmStorage.findGenreByid(9999))
                .isInstanceOf(GenreNotFoundException.class)
                .hasMessage(String.format("Жанра с id %s нет в системе", 9999));
    }

    @Test
    public void testGetMpaList() {
        List<Mpa> mpaList = filmStorage.getMpaList();

        assertThat(mpaList).hasSize(5);
        assertThat(mpaList.get(0)).hasFieldOrPropertyWithValue("name", "G");
        assertThat(mpaList.get(1)).hasFieldOrPropertyWithValue("name", "PG");
    }

    @Test
    public void testFindMpaByid() {
        Optional<Mpa> mpaOptional = Optional.ofNullable(filmStorage.findMpaByid(1));

        assertThat(mpaOptional)
                .isPresent()
                .hasValueSatisfying(g -> {
                    assertThat(g).hasFieldOrPropertyWithValue("id", 1L);
                    assertThat(g).hasFieldOrPropertyWithValue("name", "G");
                });

    }

    @Test
    public void testFindMpaByidShouldThrowMpaNotFoundExceptionWithWrongMpaId() {
        assertThatThrownBy(() -> filmStorage.findMpaByid(9999))
                .isInstanceOf(MpaNotFoundException.class)
                .hasMessage(String.format("Рейтинга с id %s нет в системе", 9999));

    }


}
