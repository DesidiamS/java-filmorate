package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.mappers.GenreRowMapper;

import java.util.Collection;

@Component
@Qualifier("genreDBStorage")
@Repository
@RequiredArgsConstructor
public class GenreDBStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreRowMapper genreRowMapper;

    public Collection<Genre> getAll() {
        String query = "Select * From Genre g";
        return jdbcTemplate.query(query, genreRowMapper);
    }

    public Genre getGenreById(Long id) {
        String query = "Select * From Genre g WHERE g.id = ?";
        try {
            return jdbcTemplate.queryForObject(query, genreRowMapper, id);
        } catch (Exception e) {
            throw new NotFoundException("Жанр не найден!");
        }
    }


}
