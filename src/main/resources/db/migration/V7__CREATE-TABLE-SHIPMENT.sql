CREATE TABLE t_shipment(
    id TEXT NULL DEFAULT uuid_generate_v4() CONSTRAINT t_shipment_pk PRIMARY KEY,
    order_id TEXT NOT NULL REFERENCES "t_order"(id),
    tracking_number TEXT NOT NULL,
    courier_service TEXT NOT NULL,
    lov_status_shipment_id TEXT NOT NULL REFERENCES "t_lov_data"(id),
    created_at timestamptz NOT NULL DEFAULT now(),
    updated_at timestamptz NOT NULL DEFAULT now(),
    version int8 NOT NULL DEFAULT 0
);