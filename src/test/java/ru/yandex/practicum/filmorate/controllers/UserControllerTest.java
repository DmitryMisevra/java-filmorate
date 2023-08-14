package ru.yandex.practicum.filmorate.controllers;

import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/* тестирование с помощью MockMVC */
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserController userController;

    @BeforeEach
    void setUp() {
        userController.clearUserController();
    }

    @Test
    void addUser() throws Exception {
        String userJson = "{"
                + "\"name\": \"Nick Name\","
                + "\"email\": \"mail@mail.ru\","
                + "\"login\": \"dolore\","
                + "\"birthday\": \"1946-08-20\""
                + "}";


        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Nick Name"))
                .andExpect(jsonPath("$.email").value("mail@mail.ru"))
                .andExpect(jsonPath("$.login").value("dolore"))
                .andExpect(jsonPath("$.birthday").value("1946-08-20"));
    }

    @Test
    void addUser_shouldSetNameAsLoginWhenNameIsEmpty() throws Exception {
        String userJson = "{"
                + "\"email\": \"mail@mail.ru\","
                + "\"login\": \"dolore\","
                + "\"birthday\": \"1946-08-20\""
                + "}";


        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("dolore"))
                .andExpect(jsonPath("$.email").value("mail@mail.ru"))
                .andExpect(jsonPath("$.login").value("dolore"))
                .andExpect(jsonPath("$.birthday").value("1946-08-20"));
    }

    @Test
    void addUser_shouldThrowValidationExceptionWhenLoginContainsSpaces() {
        String userJson = "{"
                + "\"name\": \"Nick Name\","
                + "\"email\": \"mail@mail.ru\","
                + "\"login\": \"dolore dolore\","
                + "\"birthday\": \"1946-08-20\""
                + "}";


        Exception exception = assertThrows(ServletException.class,
                () -> mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)));

        assertTrue(exception.getCause() instanceof ValidationException);
        assertEquals("Логин должен быть без пробелов", exception.getCause().getMessage());
    }

    @Test
    void addUser_shouldReturnBadRequestWhenInvalidEmail() throws Exception {
        String userJson = "{"
                + "\"name\": \"Nick Name\","
                + "\"email\": \"mail mail.ru\","
                + "\"login\": \"dolore\","
                + "\"birthday\": \"1946-08-20\""
                + "}";


        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addUser_shouldReturnBadRequestWhenInvalidBirthdayDate() throws Exception {
        String userJson = "{"
                + "\"name\": \"Nick Name\","
                + "\"email\": \"mail@mail.ru\","
                + "\"login\": \"dolore\","
                + "\"birthday\": \"2023-08-20\""
                + "}";


        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addUser_shouldReturnBadRequestWithEmptyUserJson() throws Exception {
        String userJson = "";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUser() throws Exception {
        String userJsonOne = "{"
                + "\"name\": \"Nick Name\","
                + "\"email\": \"mail@mail.ru\","
                + "\"login\": \"dolore\","
                + "\"birthday\": \"1946-08-20\""
                + "}";

        String userJsonTwo = "{"
                + "\"id\": \"1\","
                + "\"name\": \"Chacky\","
                + "\"email\": \"mail@mail.ru\","
                + "\"login\": \"ChackyMan\","
                + "\"birthday\": \"1946-08-20\""
                + "}";


        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJsonOne))
                .andExpect(status().isOk());

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJsonTwo))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Chacky"))
                .andExpect(jsonPath("$.email").value("mail@mail.ru"))
                .andExpect(jsonPath("$.login").value("ChackyMan"))
                .andExpect(jsonPath("$.birthday").value("1946-08-20"));

    }

    @Test
    void updateUser_ShouldThrowValidationExceptionWithWrongId() throws Exception {
        String userJsonOne = "{"
                + "\"name\": \"Nick Name\","
                + "\"email\": \"mail@mail.ru\","
                + "\"login\": \"dolore\","
                + "\"birthday\": \"1946-08-20\""
                + "}";

        String userJsonTwo = "{"
                + "\"id\": \"5\","
                + "\"name\": \"Chacky\","
                + "\"email\": \"mail@mail.ru\","
                + "\"login\": \"ChackyMan\","
                + "\"birthday\": \"1946-08-20\""
                + "}";


        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJsonOne))
                .andExpect(status().isOk());

        Exception exception = assertThrows(ServletException.class,
                () -> mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJsonTwo)));

        assertTrue(exception.getCause() instanceof ValidationException);
        assertEquals("Такого пользователя нет в системе", exception.getCause().getMessage());
    }

    @Test
    void getUsersList() throws Exception {

        String userJson = "{"
                + "\"name\": \"Nick Name\","
                + "\"email\": \"mail@mail.ru\","
                + "\"login\": \"dolore\","
                + "\"birthday\": \"1946-08-20\""
                + "}";


        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk());


        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}