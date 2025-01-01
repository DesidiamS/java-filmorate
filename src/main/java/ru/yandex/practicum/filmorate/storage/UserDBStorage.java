package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.Objects;

@Component
@Qualifier("userDBStorage")
@Repository
@RequiredArgsConstructor
public class UserDBStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;

    @Override
    public Collection<User> findAll() {
        String query = "SELECT u.*, uf.friend_id FROM USERS u " +
                "LEFT JOIN (SELECT FRIEND_ID, USER_ID FROM USER_FRIEND " +
                "WHERE STATUS_ID = 1) uf on u.id = uf.user_id";
        return jdbcTemplate.query(query, userRowMapper);
    }

    @Override
    public User findById(Long id) {
        String query = "SELECT u.*, uf.friend_id FROM USERS u " +
                "LEFT JOIN (SELECT FRIEND_ID, USER_ID FROM USER_FRIEND " +
                "WHERE STATUS_ID = 1) uf on u.id = uf.user_id " +
                "WHERE u.id = ?";
        try {
            return jdbcTemplate.queryForObject(query, userRowMapper, id);
        } catch (Exception e) {
            throw new NotFoundException("Пользователь не найден!");
        }
    }

    @Override
    public User create(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String query = "INSERT INTO USERS (EMAIL, LOGIN, NAME, BIRTHDAY) VALUES ( ?, ?, ?, ? )";
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(query, new String[]{"id"});
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getLogin());
            statement.setString(3, user.getName());
            statement.setDate(4, Date.valueOf(user.getBirthday()));
            return statement;
        }, keyHolder);

        long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return findById(id);
    }

    @Override
    public User update(User user) {
        if (findById(user.getId()) == null) {
            throw new NotFoundException("Пользователь не найден!");
        }

        String query = "Update USERS SET EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? WHERE ID = ?";
        jdbcTemplate.update(query, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return findById(user.getId());
    }

    @Override
    public void addFriend(Long id, Long friendId) {
        String query = "Insert Into USER_FRIEND(USER_ID, FRIEND_ID, STATUS_ID) VALUES ( ?, ?, ? )";
        jdbcTemplate.update(query, id, friendId, 1);
        jdbcTemplate.update(query, friendId, id, 2);
    }

    @Override
    public void deleteFriend(Long id, Long friendId) {
        String query = "DELETE FROM USER_FRIEND WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(query, id, friendId);
    }
}
