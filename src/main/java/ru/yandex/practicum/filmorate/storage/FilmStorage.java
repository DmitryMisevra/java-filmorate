package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    /* UserStorage отвечает за добавление, удаление, и обновление данных фильма, его поиск по id и вывод списка всех
    фильмов */

    /* добавляет фильм */
    Film addFilm(Film film);

    /* обновляет фильм  */
    Film updateFilm(Film film);

    /* находит фильм по id */
    Film findFilmById(Long id);

    /* возвращает список сохраненных фильмов */
    List<Film> getFilmsList();
}
