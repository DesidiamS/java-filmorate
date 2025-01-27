package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.MPARowMapper;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

@Component
@Qualifier("mpaDBStorage")
@Repository
@RequiredArgsConstructor
public class MpaDBStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MPARowMapper mpaRowMapper;

    public Collection<Mpa> getAll() {
        String query = "Select * From RATING r";
        return jdbcTemplate.query(query, mpaRowMapper);
    }

    public Mpa getMpaById(Long id) {
        String query = "Select * From RATING r WHERE r.id = ?";
        try {
            return jdbcTemplate.queryForObject(query, mpaRowMapper, id);
        } catch (Exception e) {
            throw new NotFoundException("Рейтинг не найден!");
        }
    }
}
