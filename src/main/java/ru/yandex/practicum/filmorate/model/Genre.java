package ru.yandex.practicum.filmorate.model;


import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/* Genre отвечает за хранение информации о жанре */

@Getter
@EqualsAndHashCode
@Builder
public class Genre {

    private final long id;
    private String name;

    public Genre(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Genre(long id) {
        this.id = id;
    }
}
