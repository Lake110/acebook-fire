ALTER TABLE users ADD COLUMN avatar_style VARCHAR(50) DEFAULT 'thumbs' NOT NULL;


ALTER TABLE posts
    ALTER COLUMN updated_at DROP DEFAULT,
    ALTER COLUMN updated_at DROP NOT NULL;

UPDATE posts SET updated_at = NULL;

ALTER TABLE comments
    ALTER COLUMN updated_at DROP DEFAULT,
ALTER COLUMN updated_at DROP NOT NULL;

UPDATE comments SET updated_at = NULL;