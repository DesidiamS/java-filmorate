package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User findById(Long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new NotFoundException("Пользователь не найден!");
        }
    }

    @Override
    public User create(User user) {
        user.setId(getNextId());
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        log.info("User created with id: " + user.getId());
        return user;
    }

    @Override
    public User update(User user) {
        if (user.getId() == null) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("Пользователь не найден!");
        }

        if (user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        users.put(user.getId(), user);
        log.info("User updated with id: " + user.getId());
        return user;
    }

    private Long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @Override
    public void addFriend(Long id, Long friendId) {
    }

    @Override
    public void deleteFriend(Long id, Long friendId) {
    }
}
