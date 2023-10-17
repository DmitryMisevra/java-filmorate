package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface FilmStorage {

    /* UserStorage отвечает за добавление, удаление, и обновление данных фильма, его поиск по id и вывод списка всех
    фильмов */

    /* добавляет фильм */
    Film addFilm(Film film);

    /* обновляет фильм  */
    Film updateFilm(Film film);

    /* находит фильм по id */
    Film findFilmById(long id);

    /* возвращает список сохраненных фильмов */
    List<Film> getFilmsList();

    /* возвращает список всех жанров */
    List<Genre> getGenresList();

    /* возвращает жанр по id */
    Genre findGenreByid(long id);

    /* возвращает список всех рейтингов */
    List<Mpa> getMpaList();

    /* возвращает рейтинг по id */
    Mpa findMpaByid(long id);
}
