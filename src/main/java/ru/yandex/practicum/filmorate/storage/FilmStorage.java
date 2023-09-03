package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    /* UserStorage отвечает за добавление, удаление, и обновление данных фильма, его поиск по id и вывод списка всех
    фильмов */

    Film addFilm(Film film);

    Film updateFilm(Film film);

    Film findFilmById(Long id);

    List<Film> getFilmsList();
}
