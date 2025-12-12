DROP TABLE IF EXISTS books;

CREATE TABLE books
(
    id        uuid         not null primary key,
    title     varchar(255) not null,
    author_id uuid         not null,
    constraint authors_fk foreign key (author_id) references authors(id)
);
