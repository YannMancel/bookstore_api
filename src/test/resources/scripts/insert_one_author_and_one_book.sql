INSERT INTO authors
(
    id,
    email,
    first_name,
    last_name
)
VALUES
(
    '64f07a63-1c1c-415e-b2c7-6a54860e6083',
    'alain.damasio@gmail.com',
    'Alain',
    'Damasio'
);

INSERT INTO books
(
    id,
    title,
    author_id
)
VALUES
(
    '1955a2d7-5367-4c63-8323-31ad9bd3db31',
    'La Horde du Contrevent',
    '64f07a63-1c1c-415e-b2c7-6a54860e6083'
);
