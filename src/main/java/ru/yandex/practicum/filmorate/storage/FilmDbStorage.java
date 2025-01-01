package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.BadRequestException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.Collection;
import java.util.Objects;

@Component
@Qualifier("filmDBStorage")
@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmRowMapper filmRowMapper;
    private final GenreDBStorage genreDBStorage;
    private final MpaDBStorage mpaDBStorage;

    @Override
    public Collection<Film> findAll() {
        String query = "SELECT f.*, r.ID as mpa_id, r.NAME as mpa_name, fg.GENRE_ID, g2.NAME as genre_name," +
                " COALESCE(flc.likesCount, 0) as likesCount " +
                "FROM Film f " +
                "LEFT JOIN RATING R ON R.ID = f.RATINGID " +
                "LEFT JOIN FILM_GENRE FG ON f.ID = FG.FILM_ID " +
                "LEFT JOIN GENRE G2 ON G2.ID = FG.GENRE_ID " +
                "LEFT JOIN (" +
                "SELECT fl.film_id, COUNT(*) AS likesCount " +
                "FROM FILM_LIKE fl " +
                "GROUP BY fl.film_id" +
                ") flc ON f.ID = flc.film_id";
        return jdbcTemplate.query(query, filmRowMapper);
    }

    @Override
    public Film findById(Long id) {
        String query = "SELECT f.*, r.ID as mpa_id, r.NAME as mpa_name, fg.GENRE_ID, g2.NAME as genre_name," +
                " COALESCE(flc.likesCount, 0) as likesCount " +
                "FROM Film f " +
                "LEFT JOIN RATING R ON R.ID = f.RATINGID " +
                "LEFT JOIN FILM_GENRE FG ON f.ID = FG.FILM_ID " +
                "LEFT JOIN GENRE G2 ON G2.ID = FG.GENRE_ID " +
                "LEFT JOIN (" +
                "SELECT fl.film_id, COUNT(*) AS likesCount " +
                "FROM FILM_LIKE fl " +
                "GROUP BY fl.film_id" +
                ") flc ON f.ID = flc.film_id " +
                " Where f.id = ?";
        return jdbcTemplate.queryForObject(query, filmRowMapper, id);
    }

    @Override
    public Film create(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        if (film.getMpa() != null) {
            try {
                mpaDBStorage.getMpaById(film.getMpa().getId());
            } catch (Exception e) {
                throw new BadRequestException("Возрастной рейтинг не найден!");
            }
        }

        String query = "Insert Into Film(name, description, releaseDate, duration, RATINGID) values(?, ?, ?, ?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(query, new String[]{"id"});
            statement.setString(1, film.getName());
            statement.setString(2, film.getDescription());
            statement.setObject(3, film.getReleaseDate());
            statement.setLong(4, film.getDuration());
            if (film.getMpa() != null) {
                statement.setLong(5, film.getMpa().getId());
            } else {
                statement.setNull(5, Types.BIGINT);
            }
            return statement;
        }, keyHolder);

        long id = Objects.requireNonNull(keyHolder.getKey()).longValue();

        if (!film.getGenres().isEmpty()) {

            for (Genre genre : film.getGenres()) {
                try {
                    genreDBStorage.getGenreById(genre.getId());
                } catch (NotFoundException e) {
                    throw new BadRequestException(e.getMessage());
                }

                String query1 = "Insert Into Film_Genre(film_id, genre_id) Values(?, ?)";
                try {
                    jdbcTemplate.update(connection1 -> {
                        PreparedStatement preparedStatement = connection1.prepareStatement(query1);
                        preparedStatement.setLong(1, id);
                        preparedStatement.setLong(2, genre.getId());
                        return preparedStatement;
                    }, keyHolder);
                } catch (Exception ignored) {

                }
            }
        }

        return findById(id);
    }

    @Override
    public Film update(Film film) {
        if (findById(film.getId()) == null) {
            throw new NotFoundException("Фильм не найден!");
        }
        String query = "Update Film Set name = ?, description = ?, releaseDate = ?, duration = ?, ratingId = ? " +
                "Where id = ?";
        jdbcTemplate.update(query, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getMpa() != null ? film.getMpa().getId() : null, film.getId());
        return findById(film.getId());
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String query = "Insert Into FILM_LIKE(FILM_ID, USER_ID) VALUES(?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, filmId);
            preparedStatement.setLong(2, userId);
            return preparedStatement;
        }, keyHolder);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String query = "DELETE FROM FILM_LIKE WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, filmId);
            preparedStatement.setLong(2, userId);
            return preparedStatement;
        }, keyHolder);
    }
}
