package ru.yandex.practicum.filmorate.model;

import lombok.Getter;

@Getter
public class Rating {

    private final long ratingId;
    private final String ratingName;

    public Rating(long ratingId, String ratingName) {
        this.ratingId = ratingId;
        this.ratingName = ratingName;
    }
}
