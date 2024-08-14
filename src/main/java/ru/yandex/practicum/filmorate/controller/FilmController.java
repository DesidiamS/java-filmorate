package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ErrorResponse;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.Collection;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final InMemoryFilmStorage filmStorage;
    private final FilmService filmService;

    public FilmController(InMemoryFilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        return filmStorage.findAll();
    }

    @GetMapping(value = "/{id}")
    public Film getFilmById(@PathVariable Long id) {
        return filmStorage.findById(id);
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        return filmStorage.create(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmStorage.update(film);
    }

    @PutMapping(value = "/{id}/like/{userId}")
    public void addLikeToFilm(@PathVariable Long id,
                              @PathVariable Long userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping(value = "/{id}/like/{userId}")
    public void removeLikeFromFilm(@PathVariable Long id,
                                   @PathVariable Long userId) {
        filmService.deleteLike(id, userId);
    }

    @GetMapping(value = "/popular")
    public Collection<Film> findBestFilms(@RequestParam Integer count) {
        return filmService.findBestFilms(count);
    }

    @ExceptionHandler({FilmNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleFilmNotFound(final RuntimeException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleUserNotFound(final RuntimeException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
    }


}
