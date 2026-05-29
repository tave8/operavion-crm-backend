CREATE TABLE addresses_aud (
       id              UUID        NOT NULL,
       rev             INTEGER     NOT NULL REFERENCES revinfo(rev),
       revtype         SMALLINT,
       display_name    VARCHAR(255),
       lat             FLOAT8,
       lon             FLOAT8,
       PRIMARY KEY (id, rev)
);