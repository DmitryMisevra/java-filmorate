package ru.yandex.practicum.filmorate.controllers;


import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private int counter = 1; /* счетчик id */
    private final Map<Integer, Film> films = new HashMap<>(); /* мапа, где хранятся фильмы. id - ключ */


    /* добавляет фильм */
    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) throws ValidationException {
        validateReleaseDate(film);
        film.setId(counter);
        films.put(counter, film);
        Film newFilm = films.get(counter);
        log.debug("Добавили фильм: {}", newFilm);
        counter++;
        log.debug("Состояние счетчика id фильмов: {}", counter);
        return newFilm;
    }

    /* обновляет фильм или выбрасывает исключение */
    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) throws ValidationException {
        if (films.containsKey(film.getId())) {
            validateReleaseDate(film);
            films.put(film.getId(), film);
            log.debug("обновили фильм: {}", films.get(film.getId()));
            return films.get(film.getId());
        } else {
            log.error("следующего id фильма нет в списке: {}", film.getId());
            throw new ValidationException("Такого фильма нет в системе");
        }
    }

    /* возвращает список сохраненных фильмов */
    @GetMapping
    public List<Film> getFilmsList() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return new ArrayList<>(films.values());
    }

    /* Очищает память контроллера. Сейчас используется для тестов */
    public void clearFilmController() {
        counter = 1;
        films.clear();
    }
    /* Вспомогательный метод. Валидирует дату релиза или выбрасывает исключение */
    private void validateReleaseDate(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895,12,28))) {
            log.error("Некорректная дата релиза: {}", film.getReleaseDate());
            throw new ValidationException("Дата релиза не может быть ранее 28 декабря 1895 года");
        }
    }

}
