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

MERGE INTO FRIEND_STATUS(ID, NAME)
VALUES ( 1, 'Добавлен в друзья' ), (2, 'Заявка на рассмотрении')