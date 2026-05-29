-- drop the sequence created by SERIAL
ALTER TABLE revinfo ALTER COLUMN rev DROP DEFAULT;
DROP SEQUENCE revinfo_rev_seq;

-- point rev to the correct sequence
ALTER TABLE revinfo ALTER COLUMN rev SET DEFAULT nextval('revinfo_seq');