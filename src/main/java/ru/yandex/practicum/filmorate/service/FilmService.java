package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
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
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Collection<Film> findBestFilms(Integer count) {
        if (count == null) {
            count = 10;
        }

        List<Film> films = filmStorage.findAll().stream().toList();
        List<Film> bestFilms = new ArrayList<>();
        for (Film film : films) {
            if (film.getUsersLikes() != null && !film.getUsersLikes().isEmpty()) {
                bestFilms.add(film);
            }
        }
        bestFilms.sort((f1, f2) -> Integer.compare(f2.getUsersLikes().size(), f1.getUsersLikes().size()));

        if (!bestFilms.isEmpty()) {
            return bestFilms.stream().limit(count).collect(Collectors.toList());
        } else {
            return films.stream().limit(count).collect(Collectors.toList());
        }
    }

    public void addLike(Long id, Long userId) {
        Film film = filmStorage.findById(id);
        User user = userStorage.findById(userId);
        film.getUsersLikes().add(user.getId());
    }

    public void deleteLike(Long id, Long userId) {
        Film film = filmStorage.findById(id);
        User user = userStorage.findById(userId);
        if (!film.getUsersLikes().contains(user.getId())) {
            return;
        }
        film.getUsersLikes().remove(user.getId());
    }
}
