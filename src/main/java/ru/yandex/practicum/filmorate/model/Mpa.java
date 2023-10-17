package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@Builder
public class Mpa {

    /* Mpa хранит в себе рейтинг */

    private final long id;
    private String name;

    public Mpa(long id) {
        this.id = id;
    }

    public Mpa(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
