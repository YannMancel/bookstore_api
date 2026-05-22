INSERT INTO authors_books
(
    author_id,
    book_id
)
SELECT
    books.author_id,
    books.id
FROM books;
