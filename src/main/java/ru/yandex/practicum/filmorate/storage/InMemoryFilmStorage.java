package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.FilmValidation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private Long counter = 1L; /* счетчик id */
    private final Map<Long, Film> films = new HashMap<>(); /* мапа, где хранятся фильмы. id - ключ */

    @Override
    public Film addFilm(Film film) throws ValidationException {
        FilmValidation.validateReleaseDate(film);
        film.setId(counter);
        films.put(counter, film);
        Film newFilm = films.get(counter);
        counter++;
        return newFilm;
    }

    @Override
    public Film updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            FilmValidation.validateReleaseDate(film);
            films.put(film.getId(), film);
            return films.get(film.getId());
        } else {
            throw new FilmNotFoundException("Такого фильма нет в системе");
        }
    }

    @Override
    public Film findFilmById(Long id) {
        if (films.containsKey(id)) {
            return films.get(id);
        } else {
            throw new FilmNotFoundException("Такого фильма нет в базе данных");
        }
    }

    @Override
    public List<Film> getFilmsList() {
        return new ArrayList<>(films.values());
    }
}
