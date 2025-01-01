package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    FilmStorage filmStorage;
    UserStorage userStorage;

    @Autowired
    public FilmService(@Qualifier("filmDBStorage") FilmStorage filmStorage, @Qualifier("userDBStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Collection<Film> findAllFilms() {
        return filmStorage.findAll();
    }

    public Film findFilmById(Long id) {
        return filmStorage.findById(id);
    }

    public Film createFilm(Film film) {
        return filmStorage.create(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.update(film);
    }

    public Collection<Film> findBestFilms(Integer count) {

        if (count == null) {
            count = 10;
        }

        List<Film> films = new ArrayList<>(filmStorage.findAll());

        films.sort((f1, f2) -> f2.getRate().compareTo(f1.getRate()));
        return films.stream().limit(Math.min(films.size(), count)).collect(Collectors.toList());
    }

    public void addLike(Long id, Long userId) {
        Film film = filmStorage.findById(id);
        User user = userStorage.findById(userId);
        filmStorage.addLike(film.getId(), user.getId());
    }

    public void deleteLike(Long id, Long userId) {
        Film film = filmStorage.findById(id);
        User user = userStorage.findById(userId);
        filmStorage.deleteLike(film.getId(), user.getId());
    }
}
