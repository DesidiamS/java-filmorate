INSERT INTO GENRE(NAME)
values ( 'Комедия' ),
       ('Драма'),
       ('Мультфильм'),
       ('Триллер'),
       ('Документальный'),
       ('Боевик');

INSERT INTO RATING (NAME, NOTE)
VALUES ( 'G', '0+' ),
       ('PG', '7+'),
       ('PG-13', '13+'),
       ('R', '18+'),
       ('NC-17', '17+');

Insert Into FILM (name, description, RELEASEDATE, duration, RATINGID)
values('test', 'test', '2023-04-10 10:39:37', 2, 1),
      ('test2', 'test2', '2023-04-10 10:39:37', 3, 1),
      ('test3', 'test3', '2023-04-10 10:39:37', 1, 1);

INSERT INTO FILM_GENRE(film_id, genre_id)
VALUES ( 1, 1 ), (1, 2);

INSERT INTO FRIEND_STATUS(ID, NAME)
VALUES ( 1, 'Добавлен в друзья' ), (2, 'Заявка на рассмотрении')