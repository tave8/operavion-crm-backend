-- revinfo: shared across all audited entities
CREATE TABLE revinfo (
     rev         SERIAL  PRIMARY KEY,
     revtstmp    BIGINT  NOT NULL
);

-- companies audit table
CREATE TABLE companies_aud (
       id                          UUID            NOT NULL,
       rev                         INTEGER         NOT NULL REFERENCES revinfo(rev),
       revtype                     SMALLINT,
       created_at                  TIMESTAMPTZ(6),
       email                       VARCHAR(255),
       legal_name                  VARCHAR(255),
       stripe_customer_id          VARCHAR(255),
       stripe_subscription_status  VARCHAR(255),
       PRIMARY KEY (id, rev)
);