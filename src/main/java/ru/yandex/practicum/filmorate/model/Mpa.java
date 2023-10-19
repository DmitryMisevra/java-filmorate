package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/* Mpa отвечает за хранение данных об id и имени рейтинга */

@Getter
@EqualsAndHashCode
@Builder
public class Mpa {

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
