DROP TABLE IF EXISTS authors_books;

CREATE TABLE authors_books
(
    author_id   uuid,
    book_id     uuid,

    PRIMARY KEY (author_id, book_id),
    FOREIGN KEY (author_id) REFERENCES authors (id) ON DELETE RESTRICT,
    FOREIGN KEY (book_id) REFERENCES books (id) ON DELETE RESTRICT
);
