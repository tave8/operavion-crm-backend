package giuseppetavella.zero_chiamate.integrations.stripe;

public enum StripeAPISubscriptionStatus {
    INCOMPLETE,   // just signed up, no payment yet
    TRIALING,     // in free trial
    ACTIVE,       // paying
    PAST_DUE,     // payment failed
    CANCELED;     // canceled

    public static StripeAPISubscriptionStatus fromStripe(String stripeStatus) {
        return switch (stripeStatus) {
            case "trialing" -> TRIALING;
            case "active" -> ACTIVE;
            case "past_due" -> PAST_DUE;
            case "canceled" -> CANCELED;
            default -> INCOMPLETE;
        };
    }
    
}