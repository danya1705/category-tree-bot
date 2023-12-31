--liquibase formatted sql

--changeset dKarpov:1
CREATE TABLE IF NOT EXISTS categories
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(25),
    path VARCHAR(50)
);

--changeset dKarpov:2
INSERT INTO categories VALUES (1, 'Наземный транспорт', '');
INSERT INTO categories VALUES (2, 'Водный транспорт', '');
INSERT INTO categories VALUES (3, 'Воздушный транспорт', '');
INSERT INTO categories VALUES (4, 'Автомобиль', '/1');
INSERT INTO categories VALUES (5, 'Мотоцикл', '/1');
INSERT INTO categories VALUES (6, 'СИМ', '/1');
INSERT INTO categories VALUES (7, 'Лодка', '/2');
INSERT INTO categories VALUES (8, 'Катер', '/2');
INSERT INTO categories VALUES (9, 'Парусник', '/2');
INSERT INTO categories VALUES (10, 'Самолёт', '/3');
INSERT INTO categories VALUES (11, 'Вертолёт', '/3');
INSERT INTO categories VALUES (12, 'Аэростат', '/3');
INSERT INTO categories VALUES (13, 'Легковой', '/1/4');
INSERT INTO categories VALUES (14, 'Грузовой', '/1/4');
INSERT INTO categories VALUES (15, 'Воздушный шар', '/3/12');
INSERT INTO categories VALUES (16, 'Дирижабль', '/3/12');
INSERT INTO categories VALUES (17, 'Велосипед', '/1/6');
INSERT INTO categories VALUES (18, 'Электросамокат', '/1/6');
INSERT INTO categories VALUES (19, 'Моноколесо', '/1/6');
