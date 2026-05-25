-- fix: production env did not have this table
CREATE TABLE IF NOT EXISTS contract_expectations (
   id UUID NOT NULL,
   expectations TEXT NOT NULL,
   processed_at TIMESTAMPTZ NOT NULL,
   state VARCHAR(255) NULL,
   client_address_id UUID NOT NULL,

   CONSTRAINT contract_expectations_pkey
       PRIMARY KEY (id),

   CONSTRAINT contract_expectations_state_check
       CHECK (state IN ('PENDING', 'SUCCESS', 'FAILED')),

   CONSTRAINT contract_expectations_client_address_id_unique
       UNIQUE (client_address_id),

   CONSTRAINT contract_expectations_client_address_id_fk
       FOREIGN KEY (client_address_id) REFERENCES client_addresses(id)
);