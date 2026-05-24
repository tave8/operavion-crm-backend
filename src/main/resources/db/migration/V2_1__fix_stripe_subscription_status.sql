ALTER TABLE companies ADD COLUMN IF NOT EXISTS stripe_customer_id VARCHAR(255) UNIQUE;

ALTER TABLE companies ADD COLUMN IF NOT EXISTS stripe_subscription_status VARCHAR(50);

UPDATE companies SET stripe_subscription_status = 'INCOMPLETE' WHERE stripe_subscription_status IS NULL;

ALTER TABLE companies ALTER COLUMN stripe_subscription_status SET NOT NULL;

DO $$
    BEGIN
        IF NOT EXISTS (
            SELECT 1 FROM pg_constraint
            WHERE conname = 'companies_stripe_subscription_status_check'
        ) THEN
            ALTER TABLE companies ADD CONSTRAINT companies_stripe_subscription_status_check
                CHECK (stripe_subscription_status IN ('INCOMPLETE', 'TRIALING', 'ACTIVE', 'PAST_DUE', 'CANCELED'));
        END IF;
    END$$;