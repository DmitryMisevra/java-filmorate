package ru.yandex.practicum.filmorate.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

/* FIlmController отвечает за обработку эндпойнтов по добавлению, обновлению фильмов */

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
    public Film addFilm(@Valid @RequestBody Film film) {
        return filmService.addFilm(film);
    }

    /* обновляет фильм или выбрасывает исключение */
    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    /* находит фильм по id */
    @GetMapping("/{id}")
    public Film findFilmById(@PathVariable long id) {
        return filmService.findFilmById(id);
    }

    /* возвращает список сохраненных фильмов */
    @GetMapping
    public List<Film> getFilmsList() {
        return filmService.getFilmsList();
    }

    /* добавляет лайк к фильму */
    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable long id, @PathVariable long userId) {
        return filmService.addLike(id, userId);
    }

    /* удаляет лайк к фильму */
    @DeleteMapping("/{id}/like/{userId}")
    public Film removeLike(@PathVariable long id, @PathVariable long userId) {
        return filmService.removeLike(id, userId);
    }

    /* возвращает список самых популярных фильмов. Если не передано собственное значение, по умолчанию список
    ограничен 10 фильмами */
    @GetMapping("/popular")
    public List<Film> findPopularFilms(@RequestParam(defaultValue = "10") Long count) {
        return filmService.findPopularFilms(count);
    }
}
