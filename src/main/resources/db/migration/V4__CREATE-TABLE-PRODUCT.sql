CREATE TABLE t_product(
    id TEXT NULL DEFAULT uuid_generate_v4() CONSTRAINT t_product_pk PRIMARY KEY,
    farmer_id TEXT NOT NULL REFERENCES "t_users"(id),
    code TEXT NOT NULL,
    name TEXT NOT NULL,
    description TEXT NULL,
    price numeric NOT NULL,
    stock int NOT NULL,
    created_at timestamptz NOT NULL DEFAULT now(),
    updated_at timestamptz NOT NULL DEFAULT now(),
    version int8 NOT NULL DEFAULT 0
);

CREATE UNIQUE INDEX t_product_un
ON t_product (code);