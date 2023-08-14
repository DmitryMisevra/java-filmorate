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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/* тестирование с помощью MockMVC */

@SpringBootTest
@AutoConfigureMockMvc
class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private FilmController filmController;

    @BeforeEach
    void setUp() {
        filmController.clearFilmController();
    }

    @Test
    void addFilm_ShouldAddFilmWithDescriptionLenght200() throws Exception {

        String filmJson = "{"
                + "\"name\": \"nisi eiusmod\","
                + "\"description\": \"aaaaaaaaaaaaaaaaaaaaaaaaааааааааааааааааааааааааааааааааааааааааааааааааааааа" +
                "аааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааа" +
                "ааааааааааааааааааааааааа\","
                + "\"releaseDate\": \"1895-12-28\","
                + "\"duration\": \"100\""
                + "}";

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("nisi eiusmod"))
                .andExpect(jsonPath("$.description").value("aaaaaaaaaaaaaaaaaaaaaaaaааааааааа" +
                        "ааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааа" +
                        "аааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааа"))
                .andExpect(jsonPath("$.releaseDate").value("1895-12-28"))
                .andExpect(jsonPath("$.duration").value("100"));

    }

    @Test
    void addFilm_shouldReturnValidationExceptionWithWrongReleaseDate() {

        String filmJson = "{"
                + "\"name\": \"nisi eiusmod\","
                + "\"description\": \"adipisicing\","
                + "\"releaseDate\": \"1895-08-27\","
                + "\"duration\": \"100\""
                + "}";

        Exception exception = assertThrows(ServletException.class,
                () -> mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson)));

        assertTrue(exception.getCause() instanceof ValidationException);
        assertEquals("Дата релиза не может быть ранее 28 декабря 1895 года", exception.getCause().getMessage());
    }

    @Test
    void addFilm_shouldReturnBadRequestWithDescriptionLength201() throws Exception {

        String filmJson = "{"
                + "\"name\": \"nisi eiusmod\","
                + "\"description\": \"aaaaaaaaaaaaaaaaaaaaaaaaаааааааааааааааааааааааааааааааааааааааааааааааааааааа" +
                "ааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааа" +
                "аааааааааааааааааааааааа\","
                + "\"releaseDate\": \"1995-08-27\","
                + "\"duration\": \"100\""
                + "}";

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addFilm_shouldReturnBadRequestWithEmptyName() throws Exception {

        String filmJson = "{"
                + "\"description\": \"test\","
                + "\"releaseDate\": \"1995-08-27\","
                + "\"duration\": \"100\""
                + "}";

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addFilm_shouldReturnBadRequestWithNegativeDuration() throws Exception {

        String filmJson = "{"
                + "\"description\": \"test\","
                + "\"releaseDate\": \"1995-08-27\","
                + "\"duration\": \"-1\""
                + "}";

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addFilm_shouldReturnBadRequestWithEmptyFilmJson() throws Exception {

        String filmJson = "";

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson))
                .andExpect(status().isBadRequest());
    }



    @Test
    void updateFilm() throws Exception {
        String filmJsonOne = "{"
                + "\"name\": \"test film 1\","
                + "\"description\": \"test description 1\","
                + "\"releaseDate\": \"1995-08-27\","
                + "\"duration\": \"100\""
                + "}";

        String filmJsonTwo = "{"
                + "\"id\": \"1\","
                + "\"name\": \"test film 2\","
                + "\"description\": \"test description 2\","
                + "\"releaseDate\": \"1990-07-15\","
                + "\"duration\": \"150\""
                + "}";


        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJsonOne))
                .andExpect(status().isOk());

        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJsonTwo))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("test film 2"))
                .andExpect(jsonPath("$.description").value("test description 2"))
                .andExpect(jsonPath("$.releaseDate").value("1990-07-15"))
                .andExpect(jsonPath("$.duration").value("150"));

    }

    @Test
    void updateFilm_ShouldThrowValidationExceptionWithWrongID() throws Exception {
        String filmJsonOne = "{"
                + "\"name\": \"test film 1\","
                + "\"description\": \"test description 1\","
                + "\"releaseDate\": \"1995-08-27\","
                + "\"duration\": \"100\""
                + "}";

        String filmJsonTwo = "{"
                + "\"id\": \"2\","
                + "\"name\": \"test film 2\","
                + "\"description\": \"test description 2\","
                + "\"releaseDate\": \"1990-07-15\","
                + "\"duration\": \"150\""
                + "}";


        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJsonOne))
                .andExpect(status().isOk());

        Exception exception = assertThrows(ServletException.class,
                () -> mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJsonTwo)));

        assertTrue(exception.getCause() instanceof ValidationException);
        assertEquals("Такого фильма нет в системе", exception.getCause().getMessage());

    }

    @Test
    void getFilmsList() throws Exception {
        String userJson = "{"
                + "\"name\": \"test film 1\","
                + "\"description\": \"test description 1\","
                + "\"releaseDate\": \"1995-08-27\","
                + "\"duration\": \"100\""
                + "}";


        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk());


        mockMvc.perform(get("/films"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}