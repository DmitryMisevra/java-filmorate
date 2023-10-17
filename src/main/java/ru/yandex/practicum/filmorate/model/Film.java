package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Builder
public class Film {

    /* фильм хранит данные о фильме */

    private long id;
    @NotBlank
    @Size(max = 100)
    private final String name;
    @Size(max = 200)
    private final String description;
    @PastOrPresent
    private final LocalDate releaseDate;
    @Positive
    private final int duration;
    private final Mpa mpa;
    private Long rate;

    @Builder.Default
    private Set<Genre> genres = new LinkedHashSet<>();

    @Builder.Default
    private Set<Long> userLikes = new HashSet<>();
}
