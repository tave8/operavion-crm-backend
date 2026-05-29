CREATE TABLE uploaded_files (
    id                UUID            PRIMARY KEY,
    storage_key       VARCHAR(255)    NOT NULL UNIQUE,
    original_filename VARCHAR(255)    NOT NULL,
    mime_type         VARCHAR(100)    NOT NULL,
    created_at        TIMESTAMPTZ       NOT NULL DEFAULT NOW()
);