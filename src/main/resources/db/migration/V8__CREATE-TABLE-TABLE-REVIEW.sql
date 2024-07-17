CREATE TABLE t_review(
    id TEXT NULL DEFAULT uuid_generate_v4() CONSTRAINT t_review_pk PRIMARY KEY,
    umkm_id TEXT NOT NULL REFERENCES "t_users"(id),
    product_id TEXT NOT NULL REFERENCES "t_product"(id),
    rating int NOT NULL,
    comment TEXT NOT NULL,
    created_at timestamptz NOT NULL DEFAULT now(),
    updated_at timestamptz NOT NULL DEFAULT now(),
    version int8 NOT NULL DEFAULT 0
);