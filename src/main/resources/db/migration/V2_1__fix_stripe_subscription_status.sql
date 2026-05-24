


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