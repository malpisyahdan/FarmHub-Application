CREATE TABLE t_users(
    id TEXT NULL DEFAULT uuid_generate_v4() CONSTRAINT t_users_pk PRIMARY KEY,
    first_name TEXT NULL,
    last_name TEXT NULL,
    username TEXT NOT NULL,
    password TEXT NOT NULL,
    role text NULL,
    created_at timestamptz NOT NULL DEFAULT now(),
    updated_at timestamptz NOT NULL DEFAULT now(),
    version int8 NOT NULL DEFAULT 0
);