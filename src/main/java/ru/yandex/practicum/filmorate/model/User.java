package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class User {

    private int id;
    @NotNull
    private String name;
    @Email
    private final String email;
    @NotBlank
    private final String login;
    @PastOrPresent
    private final LocalDate birthday;
}
