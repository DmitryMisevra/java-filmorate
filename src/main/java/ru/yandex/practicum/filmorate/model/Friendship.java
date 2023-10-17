package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Friendship {

    /* Friendship отвечает за хранение статусов дружбы между двумя пользователями */

    private final long user1Id;
    private final long user2Id;
    private boolean isConfirmed;

    public Friendship(String friend1Id, String friend2Id, FriendshipStatus frienshipStatus) {
        this.friend1Id = friend1Id;
        this.friend2Id = friend2Id;
        this.friendshipStatus = frienshipStatus;
    }
}
