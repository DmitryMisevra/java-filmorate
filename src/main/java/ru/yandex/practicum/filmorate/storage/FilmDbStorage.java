package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.util.FilmValidation;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/* FilmDbStorage отвечает за хранение данные о фильмах в базе SQL */

@Component
@Qualifier("filmDBStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film addFilm(Film film) {
        FilmValidation.validateReleaseDate(film);
        String filmSql = "insert into PUBLIC.FILM (FILM_NAME, FILM_RELEASE_DATE, FILM_DESCRIPTION, " +
                "FILM_DURATION, MPA_ID) values (?, ?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(filmSql, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setDate(2, Date.valueOf(film.getReleaseDate()));
            stmt.setString(3, film.getDescription());
            stmt.setLong(4, film.getDuration());
            stmt.setLong(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);

        long filmId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        updateLikes(film.getUserLikes(), filmId);
        updateGenres(film.getGenres(), filmId);
        return findFilmById(filmId);
    }

    @Override
    public Film updateFilm(Film film) {
        FilmValidation.validateReleaseDate(film);
        String sql = "update PUBLIC.FILM set FILM_NAME = ?, FILM_RELEASE_DATE = ?, FILM_DESCRIPTION = ?, " +
                "FILM_DURATION = ?, MPA_ID = ? where FILM_ID = ?";
        jdbcTemplate.update(sql, film.getName(), film.getReleaseDate(), film.getDescription(),
                film.getDuration(), film.getMpa().getId(), film.getId());
        updateLikes(film.getUserLikes(), film.getId());
        updateGenres(film.getGenres(), film.getId());
        return findFilmById(film.getId());
    }

    @Override
    public Film findFilmById(long id) {
        try {
            String sql = "select * from PUBLIC.FILM where FILM_ID = ?";
            return jdbcTemplate.queryForObject(sql, this::mapRowToFilm, id);
        } catch (EmptyResultDataAccessException e) {
            throw new FilmNotFoundException(String.format("Фильма с id %s нет в системе", id));
        }
    }

    @Override
    public List<Film> getFilmsList() {
        String sql = "select * from PUBLIC.FILM";
        return jdbcTemplate.query(sql, this::mapRowToFilm);
    }

    @Override
    public List<Genre> getGenresList() {
        String sql = "select * from PUBLIC.GENRE";
        return jdbcTemplate.query(sql, this::mapRowToGenre);
    }

    @Override
    public Genre findGenreByid(long id) {
        try {
            String sql = "select * from PUBLIC.GENRE where GENRE_ID = ?";
            return jdbcTemplate.queryForObject(sql, this::mapRowToGenre, id);
        } catch (EmptyResultDataAccessException e) {
            throw new GenreNotFoundException(String.format("Жанра с id %s нет в системе", id));
        }
    }

    @Override
    public List<Mpa> getMpaList() {
        String sql = "select * from PUBLIC.MPA";
        return jdbcTemplate.query(sql, this::mapRowToMpa);
    }

    @Override
    public Mpa findMpaByid(long id) {
        try {
            String sql = "select * from PUBLIC.MPA where MPA_ID = ?";
            return jdbcTemplate.queryForObject(sql, this::mapRowToMpa, id);
        } catch (EmptyResultDataAccessException e) {
            throw new MpaNotFoundException(String.format("Рейтинга с id %s нет в системе", id));
        }
    }

    /* Вспомогательный метод. Преобразует resultSet в объект Film */
    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = Film.builder()
                .id(resultSet.getLong("FILM_ID"))
                .name(resultSet.getString("FILM_NAME"))
                .description(resultSet.getString("FILM_DESCRIPTION"))
                .releaseDate(resultSet.getDate("FILM_RELEASE_DATE").toLocalDate())
                .duration(resultSet.getInt("FILM_DURATION"))
                .mpa(findMpaByid(resultSet.getLong("MPA_ID")))
                .build();
        film.setGenres(findFilmGenresList(film));
        film.setUserLikes(findUserLikesList(film));
        return film;
    }

    /* Вспомогательный метод. Преобразует resultSet в объект Genre */
    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        Genre.GenreBuilder builder = Genre.builder()
                .id(resultSet.getLong("GENRE_ID"));
        String name = resultSet.getString("GENRE_NAME");
        if (name != null) {
            builder.name(name);
        }
        return builder.build();
    }

    /* Вспомогательный метод. Преобразует resultSet в объект Mpa */
    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        Mpa.MpaBuilder builder = Mpa.builder()
                .id(resultSet.getLong("MPA_ID"));
        String name = resultSet.getString("MPA_NAME");
        if (name != null) {
            builder.name(name);
        }
        return builder.build();
    }

    /* Вспомогательный метод. Перезаписывает в таблицу перечень жанров по конкретному фильму */
    private void updateGenres(Set<Genre> genres, long filmId) {
        jdbcTemplate.update("delete from PUBLIC.FILM_GENRES where FILM_ID = ?", filmId);
        if (genres != null && !genres.isEmpty()) {
            for (Genre genreId : genres) {
                jdbcTemplate.update("insert into PUBLIC.FILM_GENRES" +
                        " (FILM_ID, GENRE_ID) values (?, ?);", filmId, genreId.getId());
            }
        }
    }

    /* Вспомогательный метод. Перезаписывает в таблицу лайки по конкретному фильму */
    private void updateLikes(Set<Long> likes, long filmId) {
        jdbcTemplate.update("delete from PUBLIC.LIKES where FILM_ID = ?", filmId);
        if (likes != null && !likes.isEmpty()) {
            likes.forEach(userId -> jdbcTemplate.update("insert into PUBLIC.LIKES (FILM_ID, USER_ID)" +
                    " values (?, ?);", filmId, userId));
        }
    }

    /* Вспомогательный метод. Возвращает список жанров по конкретному фильму */
    private Set<Genre> findFilmGenresList(Film film) {
        String sql = "select F.GENRE_ID, G.GENRE_ID, G.GENRE_NAME from PUBLIC.FILM_GENRES F join PUBLIC.GENRE G on " +
                "F.GENRE_ID = G.GENRE_ID where F.FILM_ID = ? order by G.GENRE_ID";
        List<Genre> genreList = jdbcTemplate.query(sql, this::mapRowToGenre, film.getId());
        return new LinkedHashSet<>(genreList);
    }

    /* Вспомогательный метод. Возвращает список лайков от юзеров по конкретному фильму */
    private Set<Long> findUserLikesList(Film film) {
        String sql = "select USER_ID from PUBLIC.LIKES where FILM_ID = ?";
        List<Long> longList = jdbcTemplate.queryForList(sql, Long.class, film.getId());
        return new HashSet<>(longList);
    }
}
