CREATE TABLE t_shipment(
    id TEXT NULL DEFAULT uuid_generate_v4() CONSTRAINT t_shipment_pk PRIMARY KEY,
    order_id TEXT NOT NULL REFERENCES "t_order"(id),
    tracking_number TEXT NOT NULL,
    lov_courier_service_id TEXT NOT NULL REFERENCES "t_lov_data"(id),
    status_shipment TEXT NOT NULL,
    created_at timestamptz NOT NULL DEFAULT now(),
    updated_at timestamptz NOT NULL DEFAULT now(),
    version int8 NOT NULL DEFAULT 0
);