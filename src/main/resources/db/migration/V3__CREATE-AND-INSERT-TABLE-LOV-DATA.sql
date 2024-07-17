CREATE TABLE t_lov_data(
    id TEXT NULL DEFAULT uuid_generate_v4() CONSTRAINT t_lov_data_pk PRIMARY KEY,
    code TEXT NOT NULL,
    name TEXT NOT NULL,
    lov_type text not NULL,   
    ord_number int NOT NULL,
    is_active bool NOT NULL DEFAULT true,
    created_at timestamptz NOT NULL DEFAULT now(),
    updated_at timestamptz NOT NULL DEFAULT now(),
    version int8 NOT NULL DEFAULT 0
);

CREATE UNIQUE INDEX t_lov_data_un
ON t_lov_data (lov_type, code);

INSERT INTO t_lov_data (code, name, lov_type, ord_number, is_active)
VALUES 
  ('COD', 'Cash on Delivery', 'PAYMENT-METHOD', 1, true),
  ('CC', 'Credit Card', 'PAYMENT-METHOD', 2, true),
  ('DC', 'Direct Debit', 'PAYMENT-METHOD', 3, true),
  ('PP', 'PayPal', 'PAYMENT-METHOD', 4, true),
  ('AP', 'Apple Pay', 'PAYMENT-METHOD', 5, true),
  ('GP', 'Google Pay', 'PAYMENT-METHOD', 6, true);
  
 INSERT INTO t_lov_data (code, name, lov_type, ord_number, is_active)
VALUES 
  ('JNE', 'JNE', 'COURIER-SERVICE', 1, true),
  ('FDX', 'FedEx', 'COURIER-SERVICE', 2, true),
  ('POSIN', 'Pos Indonesia', 'COURIER-SERVICE', 3, true),
  ('JNT', 'JNT', 'COURIER-SERVICE', 4, true),
  ('NEX', 'Ninja Express', 'COURIER-SERVICE', 5, true),
  ('TIKI', 'Tiki', 'COURIER-SERVICE', 6, true),
  ('GOSEN', 'Go Send', 'COURIER-SERVICE', 7, true);
