package ru.yandex.practicum.filmorate.model;


import lombok.Getter;

@Getter
public class Genre {

    /* Genre хранит информацию о жанре */

    private final long id;
    private String name;


    public Genre(long genreId, String genreName) {
        this.genreId = genreId;
        this.genreName = genreName;
    }
}
