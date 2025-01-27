package ru.yandex.practicum.filmorate.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        User user = new User();
        user.setName(resultSet.getString("name"));
        user.setEmail(resultSet.getString("email"));
        user.setId(resultSet.getLong("id"));
        user.setBirthday(resultSet.getDate("birthday").toLocalDate());
        user.setLogin(resultSet.getString("login"));
        Set<Long> friends = new HashSet<>();
        do {
            Integer friendId = (Integer) resultSet.getObject("friend_id");
            if (resultSet.wasNull()) {
                break;
            }

            friends.add(Long.valueOf(friendId));

        } while (resultSet.next() && resultSet.getLong("id") == user.getId());
        user.setFriends(friends);
        return user;
    }
}
