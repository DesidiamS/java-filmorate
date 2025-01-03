create table if not exists FRIEND_STATUS
(
    ID   INTEGER auto_increment
        primary key,
    NAME CHARACTER VARYING(250)
);

create table if not exists GENRE
(
    ID   INTEGER auto_increment
        primary key,
    NAME CHARACTER VARYING(250)
);

create table if not exists RATING
(
    ID   INTEGER auto_increment
        primary key,
    NAME CHARACTER VARYING(250) not null,
    NOTE CHARACTER VARYING(250)
);

create table if not exists FILM
(
    ID           INTEGER auto_increment
        primary key,
    NAME         CHARACTER VARYING(250) not null,
    DESCRIPTION  CHARACTER VARYING(250),
    RELEASEDATE TIMESTAMP              not null,
    DURATION     NUMERIC                not null,
    RATINGID    INTEGER
        references RATING
);

create table if not exists FILM_GENRE
(
    FILM_ID  INTEGER
        references FILM
            on delete cascade,
    GENRE_ID INTEGER
        references GENRE,
    constraint FILM_GENRE_UK
        primary key (FILM_ID, GENRE_ID)
);

create table if not exists USERS
(
    ID       INTEGER auto_increment
        primary key,
    EMAIL    CHARACTER VARYING(250) not null  constraint USER_EMAIL_UK unique,
    LOGIN    CHARACTER VARYING(250) not null constraint USER_LOGIN_UK unique,
    NAME     CHARACTER VARYING(250) not null,
    BIRTHDAY TIMESTAMP
);

create table if not exists FILM_LIKE
(
    FILM_ID INTEGER not null
        references FILM,
    USER_ID INTEGER not null
);

create table if not exists USER_FRIEND
(
    USER_ID   INTEGER
        references USERS,
    FRIEND_ID INTEGER
        references USERS,
    STATUS_ID INTEGER
        references FRIEND_STATUS
);

