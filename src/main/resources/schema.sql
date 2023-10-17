create table if not exists FILM_USER
(
    USER_ID       INTEGER auto_increment,
    USER_NAME     CHARACTER VARYING(50)  not null,
    USER_EMAIL    CHARACTER VARYING(100) not null,
    USER_LOGIN    CHARACTER VARYING(100) not null,
    USER_BIRTHDAY DATE                   not null,
    constraint "FILM_USER_pk"
        primary key (USER_ID)
);

create table if not exists FRIENDSHIP
(
    USER_1_ID    INTEGER not null,
    USER_2_ID    INTEGER not null,
    IS_CONFIRMED BOOLEAN not null,
    constraint "FRIENDSHIP_pk"
        primary key (USER_1_ID, USER_2_ID),
    constraint "FRIENDSHIP_FILM_USER_USER_ID_fk"
        foreign key (USER_2_ID) references FILM_USER,
    constraint "FRIEND_REQUEST_FILM_USER_USER_ID_fk"
        foreign key (USER_1_ID) references FILM_USER
);

create table if not exists GENRE
(
    GENRE_ID   INTEGER auto_increment,
    GENRE_NAME CHARACTER VARYING(50) not null,
    constraint "GENRE_pk"
        primary key (GENRE_ID)
);

create table if not exists MPA
(
    MPA_ID   INTEGER auto_increment,
    MPA_NAME CHARACTER VARYING(50) not null,
    constraint "RATING_pk"
        primary key (MPA_ID)
);

create table if not exists FILM
(
    FILM_ID           INTEGER auto_increment,
    FILM_NAME         CHARACTER VARYING(100) not null,
    FILM_RELEASE_DATE DATE                   not null,
    FILM_DESCRIPTION  CHARACTER VARYING(200) not null,
    FILM_DURATION     INTEGER                not null,
    MPA_ID            INTEGER                not null,
    constraint FILM_PK
        primary key (FILM_ID),
    constraint "FILM_MPA_MPA_ID_fk"
        foreign key (MPA_ID) references MPA
);

create table if not exists FILM_GENRES
(
    FILM_ID  INTEGER not null,
    GENRE_ID INTEGER not null,
    constraint "FILM_GENRES_pk"
        primary key (FILM_ID, GENRE_ID),
    constraint "FILM_GENRES_FILM_FILM_ID_fk"
        foreign key (FILM_ID) references FILM,
    constraint "FILM_GENRES_GENRE_GENRE_ID_fk"
        foreign key (GENRE_ID) references GENRE
);

create table if not exists LIKES
(
    FILM_ID INTEGER not null,
    USER_ID INTEGER not null,
    constraint "LIKES_pk"
        primary key (FILM_ID, USER_ID),
    constraint "FILM_LIKES_FILM_FILM_ID_fk"
        foreign key (FILM_ID) references FILM,
    constraint "FILM_LIKES_FILM_USER_USER_ID_fk"
        foreign key (USER_ID) references FILM_USER
);


