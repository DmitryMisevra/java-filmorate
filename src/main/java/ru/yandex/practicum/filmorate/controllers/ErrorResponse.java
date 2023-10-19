package ru.yandex.practicum.filmorate.controllers;


import lombok.AllArgsConstructor;
import lombok.Getter;

/* ErrorResponse отвечает за возвращаемый ответ ошибки */

@Getter
@AllArgsConstructor
public class ErrorResponse {

    private final String error;

}
