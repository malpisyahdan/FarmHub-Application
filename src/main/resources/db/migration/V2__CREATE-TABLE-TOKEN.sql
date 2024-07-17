CREATE TABLE t_token (
    id TEXT NULL DEFAULT uuid_generate_v4() CONSTRAINT t_token_pk PRIMARY KEY,
    access_token TEXT,
    is_logged_out BOOLEAN,
    user_id TEXT REFERENCES "t_users"(id),
    created_at timestamptz NOT NULL DEFAULT now(),
    updated_at timestamptz NOT NULL DEFAULT now(),
    version int8 NOT NULL DEFAULT 0
);