package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/* FilmService отвечает за добавление и удаление лайков + вывод списка самых популярных фильмов  */

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    private final UserStorage userStorage;

    public FilmService(@Qualifier("filmDBStorage") FilmStorage filmStorage,
                       @Qualifier("UserDBStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    /* добавляет лайк фильму */
    public Film addLike(long id, long userId) {
        Film film = filmStorage.findFilmById(id);
        User user = userStorage.findUserById(userId);

        Set<Long> filmLikes = film.getUserLikes();
        filmLikes.add(user.getId());
        film.setUserLikes(filmLikes);
        filmStorage.updateFilm(film);
        return filmStorage.findFilmById(film.getId());
    }

    /* удаляет лайк у фильма */
    public Film removeLike(long id, long userId) {
        Film film = filmStorage.findFilmById(id);
        User user = userStorage.findUserById(userId);

        Set<Long> filmLikes = film.getUserLikes();
        filmLikes.remove(user.getId());
        film.setUserLikes(filmLikes);
        filmStorage.updateFilm(film);
        return filmStorage.findFilmById(id);
    }

    /* удаляет лайк к фильму */
    public List<Film> findPopularFilms(long count) {
        return filmStorage.getFilmsList().stream()
                .sorted((Film film1, Film film2) -> Integer.compare(film2.getUserLikes().size(),
                        film1.getUserLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    /* ниже добавлены сквозные методы, которые вызывают одноименные методы из Filmtorage */
    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film findFilmById(Long id) {
        return filmStorage.findFilmById(id);
    }

    public List<Film> getFilmsList() {
        return filmStorage.getFilmsList();
    }

    public List<Genre> getGenresList() {
        return filmStorage.getGenresList();
    }

    public Genre findGenreByid(long id) {
        return filmStorage.findGenreByid(id);
    }

    public List<Mpa> getMpaList() {
        return filmStorage.getMpaList();
    }

    public Mpa findMpaByid(long id) {
        return filmStorage.findMpaByid(id);
    }
}
