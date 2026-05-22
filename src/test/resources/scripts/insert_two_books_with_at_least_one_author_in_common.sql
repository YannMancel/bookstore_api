INSERT INTO authors
(id, email, first_name, last_name)
VALUES
    ('64f07a63-1c1c-415e-b2c7-6a54860e6083','alain.damasio@gmail.com','Alain','Damasio'),
    ('44e0e256-3572-40d8-9622-b39036a71363','john.doe@gmail.com','John','Doe');

INSERT INTO books
(id, title)
VALUES
    ('1955a2d7-5367-4c63-8323-31ad9bd3db31','La Horde du Contrevent'),
    ('7cf9ce87-e08a-42c5-8444-e06a15390384','Les Furtifs');

INSERT INTO authors_books
(author_id, book_id)
VALUES
    ('64f07a63-1c1c-415e-b2c7-6a54860e6083','1955a2d7-5367-4c63-8323-31ad9bd3db31'),
    ('44e0e256-3572-40d8-9622-b39036a71363','1955a2d7-5367-4c63-8323-31ad9bd3db31'),
    ('64f07a63-1c1c-415e-b2c7-6a54860e6083','7cf9ce87-e08a-42c5-8444-e06a15390384');
