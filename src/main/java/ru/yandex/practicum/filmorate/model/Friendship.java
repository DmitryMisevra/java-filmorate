package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/* Friendship отвечает за хранение статусов дружбы между двумя пользователями */

@Getter
@Builder
@EqualsAndHashCode
public class Friendship {

    private final long user1Id;
    private final long user2Id;

    @Setter
    private boolean isConfirmed;


    public Friendship(long user1Id, long user2Id, boolean isConfirmed) {
        this.user1Id = user1Id;
        this.user2Id = user2Id;
        this.isConfirmed = isConfirmed;
    }
}
