package ru.yandex.practicum.filmorate.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FilmRowMapper implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(resultSet.getLong("id"));
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString("description"));
        film.setReleaseDate(resultSet.getDate("releaseDate").toLocalDate());
        film.setDuration(resultSet.getLong("duration"));
        film.setRate(resultSet.getLong("likesCount"));
        Mpa mpa = resultSet.getLong("id") == 0 ? null :
                new Mpa(resultSet.getLong("mpa_id"), resultSet.getString("mpa_name"));
        film.setMpa(mpa);
        List<Genre> genreList = new ArrayList<>();
        do {
            Integer genreId = (Integer) resultSet.getObject("genre_id");
            String genreName = resultSet.getString("genre_name");

            if (resultSet.wasNull()) {
                break;
            }

            Genre genre = new Genre(Long.valueOf(genreId), genreName);
            genreList.add(genre);

        } while (resultSet.next() && resultSet.getLong("id") == film.getId());
        film.setGenres(genreList);
        return film;
    }
}
