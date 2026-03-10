ALTER TABLE users
    RENAME COLUMN username TO email;

ALTER TABLE users
    ADD COLUMN username VARCHAR(255);

UPDATE users
SET username = SPLIT_PART(email, '@', 1);
