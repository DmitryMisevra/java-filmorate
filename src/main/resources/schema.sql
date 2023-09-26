create table PUBLIC.FILM_USER
(
    USER_ID       INTEGER                not null,
    USER_NAME     CHARACTER VARYING(50)  not null,
    USER_EMAIL    CHARACTER VARYING(100) not null,
    USER_LOGIN    CHARACTER VARYING(100) not null,
    USER_BIRTHDAY DATE                   not null,
    constraint "FILM_USER_pk"
        primary key (USER_ID)
);

create table PUBLIC.FRIENDSHIP_STATUS
(
    STATUS_ID   INTEGER not null,
    STATUS_NAME CHARACTER VARYING(50),
    constraint "FRIENDSHIP_STATUS_pk"
        primary key (STATUS_ID)
);

create table PUBLIC.FRIENDSHIP
(
    FRIENDSHIP_ID INTEGER auto_increment,
    FRIEND_1_ID   INTEGER not null,
    FRIEND_2_ID   INTEGER not null,
    STATUS_ID     INTEGER not null,
    constraint "FRIENDSHIP_FILM_USER_USER_ID_fk"
        foreign key (FRIEND_1_ID) references PUBLIC.FILM_USER,
    constraint "FRIENDSHIP_FILM_USER_USER_ID_fk2"
        foreign key (FRIEND_2_ID) references PUBLIC.FILM_USER,
    constraint "FRIENDSHIP_FRIENDSHIP_STATUS_STATUS_ID_fk"
        foreign key (STATUS_ID) references PUBLIC.FRIENDSHIP_STATUS
);

create table PUBLIC.GENRE
(
    GENRE_ID   INTEGER auto_increment,
    GENRE_NAME CHARACTER VARYING(50) not null,
    constraint "GENRE_pk"
        primary key (GENRE_ID)
);

create table PUBLIC.RATING
(
    RATING_ID   INTEGER auto_increment,
    RATING_NAME CHARACTER VARYING(50) not null,
    constraint "RATING_pk"
        primary key (RATING_ID)
);

create table PUBLIC.FILM
(
    FILM_ID           INTEGER auto_increment,
    FILM_NAME         CHARACTER VARYING(100) not null,
    FILM_RELEASE_DATE DATE                   not null,
    FILM_DESCRIPTION  CHARACTER VARYING(255) not null,
    FILM_DURATION     INTEGER                not null,
    GENRE_ID          INTEGER                not null,
    RATING_ID         INTEGER                not null,
    constraint FILM_PK
        primary key (FILM_ID),
    constraint "FILM_GENRE_GENRE_ID_fk"
        foreign key (GENRE_ID) references PUBLIC.GENRE,
    constraint "FILM_RATING_RATING_ID_fk"
        foreign key (RATING_ID) references PUBLIC.RATING
);

create table PUBLIC.FILM_LIKES
(
    LIKE_ID INTEGER auto_increment,
    FILM_ID INTEGER not null,
    USER_ID INTEGER not null,
    constraint "FILM_LIKES_FILM_FILM_ID_fk"
        foreign key (FILM_ID) references PUBLIC.FILM,
    constraint "FILM_LIKES_FILM_USER_USER_ID_fk"
        foreign key (USER_ID) references PUBLIC.FILM_USER
);