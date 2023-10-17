package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User {

    /* User хранит данные о пользователе */

    private long id;
    private String name;
    @Email
    private final String email;
    @NotBlank
    private final String login;
    @PastOrPresent
    private final LocalDate birthday;

    @Builder.Default
    private Set<Long> userFriends = new HashSet<>(); /* список друзей */
}
