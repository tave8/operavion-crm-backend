package giuseppetavella.zero_chiamate.integrations.stripe;

import com.stripe.Stripe;
import com.stripe.StripeClient;
import com.stripe.param.billingportal.SessionCreateParams;
import giuseppetavella.zero_chiamate.exceptions.integrations.stripe.StripeAPIException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Bean for Stripe API properties such as API version,
 * secret key, webhook secret, price ID (the product).
 */
@Component
public class StripeAPIProperties {

    // these attributes are injected via constructor
    private final String secretKey;
    private final String webhookSecret;
    private final String priceId;
    private final String expectedAPIVersion;
    // this is the only client we use to make requests to Stripe API
    private final StripeClient stripeClient;

    
    public StripeAPIProperties(
            @Value("${stripe-api.secret-key}") String secretKey,
            @Value("${stripe-api.webhook-secret}") String webhookSecret,
            @Value("${stripe-api.price-id}") String priceId,
            @Value("${stripe-api.expected-api-version}") String expectedAPIVersion) throws StripeAPIException
    {

        // check that the expected API version matches that
        // of the stripe library 
        
        boolean isVersionMatch = Stripe.API_VERSION.equals(expectedAPIVersion);

        if (!isVersionMatch) {
            throw new StripeAPIException(
                    "stripe-java library API version mismatch. " +
                            "Library version: '" + Stripe.API_VERSION + "'. " +
                            "Expected: '" + expectedAPIVersion + "'. " +
                            "Either upgrade stripe-java library or update expected API version."
            );
        }

        // all good
        
        this.secretKey = secretKey;
        this.webhookSecret = webhookSecret;
        this.priceId = priceId;
        this.expectedAPIVersion = expectedAPIVersion;
        // we initialize the Stripe client
        this.stripeClient = new StripeClient(secretKey);
        
    }
    


    /**
     * Stripe API secret key.
     */
    public String getSecretKey() { return secretKey; }

    
    /**
     * Stripe API webhook secret.
     */
    public String getWebhookSecret() { return webhookSecret; }

    
    /**
     * The price point ID of the product, which is the software itself.
     * The price ID should change based on environment. 
     */
    public String getPriceId() { return priceId; }


    /**
     * The expected, stable Stripe API version.
     * This helps us make sure, for example, that API version mismatch 
     * does not cause silent errors during Event serialization/deserialization etc. 
     *
     * @return the expected Stripe API version, such as "2026-04-22.dahlia"
     */
    public String getExpectedAPIVersion() {
        return expectedAPIVersion;
    }

    public StripeClient getStripeClient() {
        return stripeClient;
    }
    
}