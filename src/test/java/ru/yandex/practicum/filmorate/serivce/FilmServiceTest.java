package ru.yandex.practicum.filmorate.serivce;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class FilmServiceTest {

    private FilmService filmService;
    private Validator validator;

    @BeforeEach
    public void setup() {
        filmService = new FilmService();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void isFilmCreated() {
        Film film = new Film();
        film.setName("Тестовое название");
        film.setDescription("Описание фильма");
        film.setReleaseDate(LocalDate.of(2021, 1, 13));
        film.setDuration(Duration.ofMinutes(120));
        assertNotNull(filmService.create(film));
    }

    @Test
    public void isFilmUpdated() {
        Film film = new Film();
        film.setName("Тестовое название");
        film.setDescription("Описание фильма");
        film.setReleaseDate(LocalDate.of(2021, 1, 13));
        film.setDuration(Duration.ofMinutes(120));
        filmService.create(film);
        film.setName("Тестовое название 2");
        assertEquals("Тестовое название 2", filmService.update(film).getName());
    }

    @Test
    public void isDescriptionValidated() {
        Film film = new Film();
        film.setName("Тестовое название");
        film.setDescription("Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod" +
                " tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud" +
                " exerci tation");
        film.setReleaseDate(LocalDate.of(2021, 1, 13));
        film.setDuration(Duration.ofMinutes(120));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        // Так как аннотации игнорируются внутри тестов (но работают через Postman) приходится проверять их не
        // через сервис, а вот таким образом. Если как-то можно переделать - пожалуйста, подскажите как.
        assertFalse(violations.isEmpty());
    }

    @Test
    public void isReleaseDateValidatedWithPastDate() {
        Film film = new Film();
        film.setName("Тестовое название");
        film.setDescription("Описание фильма");
        film.setReleaseDate(LocalDate.of(1800, 1, 13));
        film.setDuration(Duration.ofMinutes(120));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void isReleaseDateValidatedWithMinimumDate() {
        Film film = new Film();
        film.setName("Тестовое название");
        film.setDescription("Описание фильма");
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(Duration.ofMinutes(120));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void isDurationValidated() {
        Film film = new Film();
        film.setName("Тестовое название");
        film.setDescription("Описание фильма");
        film.setReleaseDate(LocalDate.of(2021, 1, 13));
        film.setDuration(Duration.ofMinutes(-120));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

}
