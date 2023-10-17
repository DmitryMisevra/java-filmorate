package ru.yandex.practicum.filmorate.util;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

public class UserValidation {

    /* Вспомогательный метод. Валидирует логины на отсутствие пробелов */
    public static void validateLogin(User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин должен быть без пробелов");
        }
    }

    /* Вспомогательный метод. Если имя пустое, именем становится логин */
    public static void setDefaultName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
