DROP TABLE IF EXISTS posts;

CREATE TABLE posts (
    id         bigserial PRIMARY KEY,
    user_id    bigint REFERENCES users(id) ON DELETE CASCADE,
    content    VARCHAR(250),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE friends (
    id bigserial PRIMARY KEY,
    friender_user_id bigint NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    friendee_user_id bigint NOT NULL REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE post_tags (
    id bigserial PRIMARY KEY,
    post_id bigint NOT NULL REFERENCES posts(id) ON DELETE CASCADE,
    user_id bigint NOT NULL REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE post_likes (
    id bigserial PRIMARY KEY,
    post_id bigint NOT NULL REFERENCES posts(id) ON DELETE CASCADE,
    user_id bigint NOT NULL REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE comments (
    id bigserial PRIMARY KEY,
    post_id bigint NOT NULL REFERENCES posts(id) ON DELETE CASCADE,
    author_user_id bigint NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    content TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);