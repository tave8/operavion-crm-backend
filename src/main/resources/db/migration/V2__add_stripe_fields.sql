

UPDATE companies SET stripe_subscription_status = 'INCOMPLETE' WHERE stripe_subscription_status IS NULL;

ALTER TABLE companies ALTER COLUMN stripe_subscription_status SET NOT NULL;

