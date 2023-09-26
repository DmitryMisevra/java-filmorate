package ru.yandex.practicum.filmorate.model;


import lombok.Getter;

@Getter
public class Genre {

    private final long genreId;
    private final String genreName;


    public Genre(long genreId, String genreName) {
        this.genreId = genreId;
        this.genreName = genreName;
    }
}
