package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
public class Film {

    /* фильм хранит данные о фильме */

    private long id;
    @NotBlank
    private final String name;
    @Size(max = 200)
    private final String description;
    @PastOrPresent
    private final LocalDate releaseDate;
    @Positive
    private final int duration;
    @NotNull
    private final long genreid;
    @NotNull
    private final long ratingid;
}
