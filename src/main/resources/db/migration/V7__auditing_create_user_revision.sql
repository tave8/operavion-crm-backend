CREATE TABLE users_aud (
       id                          UUID            NOT NULL,
       rev                         INTEGER         NOT NULL REFERENCES revinfo(rev),
       revtype                     SMALLINT,
       avatar_url                  VARCHAR(255),
       created_at                  TIMESTAMPTZ(6),
       email                       VARCHAR(255),
       firstname                   VARCHAR(255),
       lastname                    VARCHAR(255),
       password_change_required    BOOLEAN,
       password_changed            BOOLEAN,
       role                        VARCHAR(255),
       username                    VARCHAR(255),
       verified_email              BOOLEAN,
       verified_email_required     BOOLEAN,
       company_id                  UUID,
       PRIMARY KEY (id, rev)
);