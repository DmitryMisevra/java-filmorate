package ru.yandex.practicum.filmorate.model;

import lombok.Getter;

@Getter
public class Like {

    private final String filmId;
    private final String userId;

    public Like(String filmId, String userId) {
        this.filmId = filmId;
        this.userId = userId;
    }
}
