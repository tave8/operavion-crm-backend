CREATE TABLE clients_aud (
         id                  UUID        NOT NULL,
         rev                 INTEGER     NOT NULL REFERENCES revinfo(rev),
         revtype             SMALLINT,
         email               VARCHAR(255),
         legal_name          VARCHAR(255),
         phone               VARCHAR(255),
         vat                 VARCHAR(255),
         company_id          UUID,
         legal_address_id    UUID,
         PRIMARY KEY (id, rev)
);