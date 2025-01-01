package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

@Service
@Slf4j
public class UserService {

    UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("userDBStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> allFriendsByUserId(Long id) {
        User user = userStorage.findById(id);
        Collection<User> friends = new ArrayList<>();
        if (!user.getFriends().isEmpty()) {
            for (Long friendId : user.getFriends()) {
                friends.add(userStorage.findById(friendId));
            }
        }
        return friends;
    }

    public Collection<User> findSharedFriends(Long id, Long friendId) {
        User user = userStorage.findById(id);
        User friendUser = userStorage.findById(friendId);
        Collection<User> sharedFriends = new HashSet<>();
        for (Long friend : user.getFriends()) {
            if (friendUser.getFriends().contains(friend)) {
                sharedFriends.add(userStorage.findById(friend));
            }
        }
        return sharedFriends;
    }

    public void addFriend(Long id, Long friendId) {
        // Проверка есть ли такие пользователи
        userStorage.findById(id);
        userStorage.findById(friendId);
        userStorage.addFriend(id, friendId);
    }

    public void deleteFriend(Long id, Long friendId) {
        // Проверка есть ли такие пользователи
        userStorage.findById(id);
        userStorage.findById(friendId);
        userStorage.deleteFriend(id, friendId);
    }
}
