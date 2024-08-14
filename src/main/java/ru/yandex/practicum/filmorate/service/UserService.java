package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.HashSet;

@Service
@Slf4j
public class UserService {

    UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> allFriendsByUserId(Long id) {
        User user = userStorage.findById(id);
        Collection<User> friends = new HashSet<>();
        for (Long friendId : user.getFriends()) {
            friends.add(userStorage.findById(friendId));
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
        if (userStorage.findById(friendId) != null) {
            User user = userStorage.findById(id);
            User friend = userStorage.findById(friendId);
            user.getFriends().add(friendId);
            friend.getFriends().add(id);
        } else {
            throw new UserNotFoundException("Пользователя которого вы хотите добавить в друзья не существует!");
        }
    }

    public void deleteFriend(Long id, Long friendId) {
        User user = userStorage.findById(id);
        User friend = userStorage.findById(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(id);
    }
}
