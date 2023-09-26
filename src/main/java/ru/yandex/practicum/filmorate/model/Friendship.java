package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Friendship {

    private final String friend1Id;
    private final String friend2Id;
    private FriendshipStatus friendshipStatus;

    public Friendship(String friend1Id, String friend2Id, FriendshipStatus frienshipStatus) {
        this.friend1Id = friend1Id;
        this.friend2Id = friend2Id;
        this.friendshipStatus = frienshipStatus;
    }
}
