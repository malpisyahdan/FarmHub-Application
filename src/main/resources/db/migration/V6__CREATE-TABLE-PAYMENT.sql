CREATE TABLE t_payment(
    id TEXT NULL DEFAULT uuid_generate_v4() CONSTRAINT t_payment_pk PRIMARY KEY,
    order_id TEXT NOT NULL REFERENCES "t_order"(id),
    lov_payment_method_id TEXT NOT NULL REFERENCES "t_lov_data"(id),
    status_payment TEXT NOT NULL,
    created_at timestamptz NOT NULL DEFAULT now(),
    updated_at timestamptz NOT NULL DEFAULT now(),
    version int8 NOT NULL DEFAULT 0
);