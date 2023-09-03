package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {

    private Long id;
    @NotBlank
    private final String name;
    @Size(max = 200)
    private final String description;
    @PastOrPresent
    private final LocalDate releaseDate;
    @Positive
    private final int duration;
    private Set<Long> userLikes = new HashSet<>(); /* добавлен список лайков */
}
