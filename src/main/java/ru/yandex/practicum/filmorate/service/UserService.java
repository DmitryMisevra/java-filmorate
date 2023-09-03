package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }



    public User addFriend(Long id, Long friendId) {
        User user = userStorage.findUserById(id);
        User newFriendUser = userStorage.findUserById(friendId);

        Set<Long> userFriends = user.getUserFriends();
        userFriends.add(newFriendUser.getId());
        user.setUserFriends(userFriends);
        userStorage.updateUser(user);

        Set<Long> newFriendUserFriends = newFriendUser.getUserFriends();
        newFriendUserFriends.add(user.getId());
        newFriendUser.setUserFriends(newFriendUserFriends);
        userStorage.updateUser(newFriendUser);

        return userStorage.findUserById(id);
    }

    public User removeFriend(Long id, Long friendId) {
        User user = userStorage.findUserById(id);
        User removedUser = userStorage.findUserById(friendId);

        Set<Long> userFriends = user.getUserFriends();
        userFriends.remove(removedUser.getId());
        user.setUserFriends(userFriends);
        userStorage.updateUser(user);

        Set<Long> removedFriendUserFriends = removedUser.getUserFriends();
        removedFriendUserFriends.add(user.getId());
        removedUser.setUserFriends(removedFriendUserFriends);
        userStorage.updateUser(removedUser);

        return userStorage.findUserById(id);
    }

    public List<User> findMutualFriends(Long id, Long friendId) {
        User userOne = userStorage.findUserById(id);
        User userTwo = userStorage.findUserById(friendId);

        Set<Long> userOneFriends = userOne.getUserFriends();
        Set<Long> userTwoFriends = userTwo.getUserFriends();

        Set<Long> mutualFriends = new HashSet<>(userOneFriends);
        mutualFriends.retainAll(userTwoFriends);
        return mutualFriends.stream().map(userStorage::findUserById).collect(Collectors.toList());
    }

}
