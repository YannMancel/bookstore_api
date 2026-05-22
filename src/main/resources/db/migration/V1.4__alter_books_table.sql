ALTER TABLE books
DROP CONSTRAINT IF EXISTS books_title_author_id_key,
DROP CONSTRAINT IF EXISTS books_author_id_fkey,
DROP COLUMN author_id,
ADD UNIQUE (title);
