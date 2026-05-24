ALTER TABLE companies ADD COLUMN IF NOT EXISTS stripe_subscription_status VARCHAR(50);

ALTER TABLE companies ADD COLUMN IF NOT EXISTS stripe_customer_id VARCHAR(255) UNIQUE;

UPDATE companies SET stripe_subscription_status = 'INCOMPLETE' WHERE stripe_subscription_status IS NULL;

ALTER TABLE companies ALTER COLUMN stripe_subscription_status SET NOT NULL;

