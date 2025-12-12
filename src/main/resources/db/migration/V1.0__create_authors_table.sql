DROP TABLE IF EXISTS authors;

CREATE TABLE authors
(
    id         uuid         not null    primary key,
    email      varchar(100),
    first_name varchar(50)  not null,
    last_name  varchar(50)  not null
);
