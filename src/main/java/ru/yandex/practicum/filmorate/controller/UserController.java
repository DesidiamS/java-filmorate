package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ErrorResponse;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserStorage userStorage;

    public UserController(UserService userService, @Qualifier("userDBStorage") UserStorage userStorage) {
        this.userService = userService;
        this.userStorage = userStorage;
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        return userStorage.findAll();
    }

    @GetMapping(value = "/{id}")
    public User userById(@PathVariable Long id) {
        return userStorage.findById(id);
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return userStorage.create(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userStorage.update(user);
    }

    @PutMapping(value = "/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id,
                          @PathVariable Long friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping(value = "/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable Long id,
                             @PathVariable Long friendId) {
        userService.deleteFriend(id, friendId);
    }

    @GetMapping(value = "/{id}/friends")
    public Collection<User> getFriends(@PathVariable Long id) {
        return userService.allFriendsByUserId(id);
    }

    @GetMapping(value = "/{id}/friends/common/{otherId}")
    public Collection<User> getSharedFriends(@PathVariable Long id,
                                             @PathVariable Long otherId) {
        return userService.findSharedFriends(id, otherId);
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<ErrorResponse> handleUserNotFound(final RuntimeException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
    }

}
