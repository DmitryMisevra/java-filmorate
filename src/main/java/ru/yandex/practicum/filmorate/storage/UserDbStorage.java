package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.UserValidation;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/* UserDbStorage отвечает за хранение данные о пользователях в базе SQL */

@Component
@Qualifier("UserDBStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addUser(User user) {
        UserValidation.validateLogin(user);
        UserValidation.setDefaultName(user);
        String sql = "insert into PUBLIC.FILM_USER (USER_NAME, USER_EMAIL, USER_LOGIN, " +
                "USER_BIRTHDAY) values (?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"USER_ID"});
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getLogin());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        return findUserById(Objects.requireNonNull(keyHolder.getKey()).longValue());
    }

    @Override
    public User updateUser(User user) {
        String sql = "update PUBLIC.FILM_USER set USER_NAME = ?, USER_EMAIL = ?, USER_LOGIN = ?, " +
                "USER_BIRTHDAY = ? where USER_ID = ?;";
        jdbcTemplate.update(sql, user.getName(), user.getEmail(), user.getLogin(), user.getBirthday(), user.getId());
        return findUserById(user.getId());
    }

    @Override
    public List<User> findUsersList() {
        String sql = "select * from PUBLIC.FILM_USER";
        return jdbcTemplate.query(sql, this::mapRowToUser);
    }

    @Override
    public List<User> findUserFriendList(long id) {
        return getFriendsLongList(id).stream().map(this::findUserById).collect(Collectors.toList());
    }

    @Override
    public User findUserById(long id) {
        String sql = "select * from PUBLIC.FILM_USER where USER_ID = ?";
        try {
            return jdbcTemplate.queryForObject(sql, this::mapRowToUser, id);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException(String.format("Пользователя с id %s нет в системе", id));
        }
    }

    @Override
    public Friendship findFriendship(long user1Id, long user2Id) {
        String sql = "select * from PUBLIC.FRIENDSHIP where (USER_1_ID = ? and USER_2_ID = ?)" +
                " or (USER_1_ID = ? and USER_2_ID = ?)";
        List<Friendship> friendships = jdbcTemplate.query(sql, this::mapRowToFriendship, user1Id, user2Id, user2Id,
                user1Id);
        if (friendships.isEmpty()) {
            return null;
        } else if (friendships.size() == 1) {
            return friendships.get(0);
        } else {
            jdbcTemplate.update("delete from PUBLIC.FRIENDSHIP where USER_1_ID = ? and USER_2_ID = ?",
                    user1Id, user2Id);
            return null;
        }
    }

    @Override
    public Friendship updateFriendship(Friendship friendship) {
        jdbcTemplate.update("delete from PUBLIC.FRIENDSHIP where USER_1_ID = ? and USER_2_ID = ?",
                friendship.getUser1Id(), friendship.getUser2Id());
        jdbcTemplate.update("insert into PUBLIC.FRIENDSHIP (USER_1_ID, USER_2_ID, IS_CONFIRMED) values (?, ?, ?);",
                friendship.getUser1Id(), friendship.getUser2Id(), friendship.isConfirmed());
        return findFriendship(friendship.getUser1Id(), friendship.getUser2Id());
    }

    @Override
    public void removeFriendship(Friendship friendship) {
        jdbcTemplate.update("delete from PUBLIC.FRIENDSHIP where USER_1_ID = ? and USER_2_ID = ?",
                friendship.getUser1Id(), friendship.getUser2Id());
    }

    /* Ниже методы, которые преобразуют данные из SQL в объекты */

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        User user = User.builder()
                .id(resultSet.getLong("USER_ID"))
                .name(resultSet.getString("USER_NAME"))
                .email(resultSet.getString("USER_EMAIL"))
                .login(resultSet.getString("USER_LOGIN"))
                .birthday(resultSet.getDate("USER_BIRTHDAY").toLocalDate())
                .build();
        user.setUserFriends(getFriendsLongList(user.getId()));
        return user;
    }

    private Friendship mapRowToFriendship(ResultSet resultSet, int rowNum) throws SQLException {
        return Friendship.builder()
                .user1Id(resultSet.getLong("USER_1_ID"))
                .user2Id(resultSet.getLong("USER_2_ID"))
                .isConfirmed(resultSet.getBoolean("IS_CONFIRMED"))
                .build();
    }

    /* Вспомогательный метод. Возвращает список id друзей пользователя. */
    private Set<Long> getFriendsLongList(long id) {
        String sql = "select * from PUBLIC.FRIENDSHIP where USER_2_ID = ? or (USER_1_ID = ? and IS_CONFIRMED = true);";
        List<Friendship> friendships = jdbcTemplate.query(sql, this::mapRowToFriendship, id, id);
        List<Long> longList = new ArrayList<>();
        for (Friendship friendship : friendships) {
            if (friendship.getUser1Id() == id) {
                longList.add(friendship.getUser2Id());
            } else {
                longList.add(friendship.getUser1Id());
            }
        }
        return new HashSet<>(longList);

        return friendships.stream()
                .map(friendship -> (friendship.getUser1Id() == id) ? friendship.getUser2Id() : friendship.getUser1Id())
                .collect(Collectors.toSet());
    }
}
