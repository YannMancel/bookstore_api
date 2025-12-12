DROP TABLE IF EXISTS authors;

CREATE TABLE authors
(
    id         uuid                           PRIMARY KEY,
    email      varchar(100) NOT NULL  UNIQUE,
    first_name varchar(50)  NOT NULL,
    last_name  varchar(50)  NOT NULL
);
