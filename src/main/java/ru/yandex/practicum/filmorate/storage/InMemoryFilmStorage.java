package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Film findById(Long id) {
        if (films.containsKey(id)) {
            return films.get(id);
        } else {
            throw new NotFoundException("Фильм не найден!");
        }
    }

    @Override
    public Film create(Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Film created with id: " + film.getId());
        return film;
    }

    @Override
    public Film update(Film film) {
        if (film.getId() == null) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("Фильм не найден!");
        }
        films.put(film.getId(), film);
        log.info("Film updated with id: " + film.getId());
        return film;
    }

    @Override
    public void addLike(Long filmId, Long userId) {

    }

    @Override
    public void deleteLike(Long filmId, Long userId) {

    }

    private Long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }


}
