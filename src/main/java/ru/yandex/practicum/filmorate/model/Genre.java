package ru.yandex.practicum.filmorate.model;


import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@Builder
public class Genre {

    /* Genre хранит информацию о жанре */

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
