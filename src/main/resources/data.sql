MERGE INTO GENRE(ID, NAME)
values (1, 'Комедия' ),
       (2, 'Драма'),
       (3, 'Мультфильм'),
       (4, 'Триллер'),
       (5, 'Документальный'),
       (6, 'Боевик');

MERGE INTO RATING (ID, NAME, NOTE)
VALUES (1, 'G', '0+' ),
       (2, 'PG', '7+'),
       (3, 'PG-13', '13+'),
       (4, 'R', '18+'),
       (5, 'NC-17', '17+');

MERGE Into FILM (id, name, description, RELEASEDATE, duration, RATINGID)
values(1, 'test', 'test', '2023-04-10 10:39:37', 2, 1),
      (2, 'test2', 'test2', '2023-04-10 10:39:37', 3, 1),
      (3, 'test3', 'test3', '2023-04-10 10:39:37', 1, 1);

MERGE INTO FILM_GENRE(film_id, genre_id)
VALUES ( 1, 1 ), (1, 2);

MERGE INTO FRIEND_STATUS(ID, NAME)
VALUES ( 1, 'Добавлен в друзья' ), (2, 'Заявка на рассмотрении')