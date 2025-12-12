DROP TABLE IF EXISTS books;

CREATE TABLE books
(
    id        uuid                   PRIMARY KEY,
    title     varchar(255) NOT NULL,
    author_id uuid         NOT NULL,

    UNIQUE (title, author_id),
    FOREIGN KEY (author_id) REFERENCES authors (id) ON DELETE RESTRICT
);
