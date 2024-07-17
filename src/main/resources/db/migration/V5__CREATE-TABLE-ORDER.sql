CREATE TABLE t_order(
    id TEXT NULL DEFAULT uuid_generate_v4() CONSTRAINT t_order_pk PRIMARY KEY,
    farmer_id TEXT NOT NULL REFERENCES "t_users"(id),
    umkm_id TEXT NOT NULL REFERENCES "t_users"(id),
    product_id TEXT NOT NULL REFERENCES "t_product"(id),
    quantity int NOT NULL,
    total_price numeric NOT NULL,
    status TEXT NOT NULL,
    status_payment TEXT NOT NULL,
    created_at timestamptz NOT NULL DEFAULT now(),
    updated_at timestamptz NOT NULL DEFAULT now(),
    version int8 NOT NULL DEFAULT 0
);