package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    private final UserStorage userStorage;

    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film addLike(Long id, Long userId) {
        Film film = filmStorage.findFilmById(id);
        User user = userStorage.findUserById(userId);

        Set<Long> filmLikes = film.getUserLikes();
        filmLikes.add(user.getId());
        film.setUserLikes(filmLikes);
        filmStorage.updateFilm(film);
        return filmStorage.findFilmById(film.getId());
    }

    public Film removeLike(Long id, Long userId) {
        Film film = filmStorage.findFilmById(id);
        User user = userStorage.findUserById(userId);

        Set<Long> filmLikes = film.getUserLikes();
        filmLikes.remove(user.getId());
        film.setUserLikes(filmLikes);
        filmStorage.updateFilm(film);
        return filmStorage.findFilmById(id);
    }

    public List<Film> findPopularFilms(Long count) {
        return filmStorage.getFilmsList().stream()
                .sorted((Film film1, Film film2) -> Integer.compare(film2.getUserLikes().size(),
                        film1.getUserLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

}
