package ru.yandex.practicum.filmorate.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    /* добавляет фильм */
    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) throws ValidationException {
        return filmService.addFilm(film);
    }

    /* обновляет фильм или выбрасывает исключение */
    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) throws ValidationException {
        return filmService.updateFilm(film);
    }

    @GetMapping("/{id}")
    public Film findFilmById(@PathVariable Long id) {
        return filmService.findFilmById(id);
    }

    /* возвращает список сохраненных фильмов */
    @GetMapping
    public List<Film> getFilmsList() {
        return filmService.getFilmsList();
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable Long id, @PathVariable Long userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film removeLike(@PathVariable Long id, @PathVariable Long userId) {
        return filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> findPopularFilms(@RequestParam(defaultValue = "10") Long count) {
        return filmService.findPopularFilms(count);
    }


}
