CREATE TABLE contract_expectations_aud (
       id                  UUID        NOT NULL,
       rev                 INTEGER     NOT NULL REFERENCES revinfo(rev),
       revtype             SMALLINT,
       expectations        TEXT,
       processed_at        TIMESTAMPTZ(6),
       state               VARCHAR(255),
       client_address_id   UUID,
       PRIMARY KEY (id, rev)
);

CREATE TABLE client_addresses_aud (
      id              UUID        NOT NULL,
      rev             INTEGER     NOT NULL REFERENCES revinfo(rev),
      revtype         SMALLINT,
      address_name    VARCHAR(255),
      address_id      UUID,
      client_id       UUID,
      PRIMARY KEY (id, rev)
);