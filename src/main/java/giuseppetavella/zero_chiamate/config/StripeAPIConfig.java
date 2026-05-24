package giuseppetavella.zero_chiamate.config;

import com.stripe.Stripe;
import com.stripe.StripeClient;
import giuseppetavella.zero_chiamate.exceptions.integrations.stripe.StripeAPIException;
import giuseppetavella.zero_chiamate.integrations.stripe.StripeAPIValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("application.properties")
public class StripeAPIConfig {

    /**
     * The expected, stable Stripe API version.
     * This helps us make sure, for example, that API version mismatch 
     * does not cause silent errors during Event serialization/deserialization etc. 
     * 
     * @return the expected Stripe API version, such as "2026-04-22.dahlia"
     */
    @Bean(name = "expectedStripeAPIVersion")
    public String getExpectedStripeAPIVersion()
    {
        return "2026-04-22.dahlia";
    }
    

    /**
     * The Stripe API client.
     *
     */
    @Bean
    public StripeClient getStripeClient(
            @Qualifier("stripeAPISecretKey") String stripeAPISecretKey,
            @Qualifier("expectedStripeAPIVersion") String expectedStripeAPIVersion)
    {
        
        boolean isVersionMatch = Stripe.API_VERSION.equals(expectedStripeAPIVersion);
        
        if (!isVersionMatch) {
            throw new StripeAPIException(
                    "stripe-java library API version mismatch. " +
                            "Library version: '" + Stripe.API_VERSION + "'. " +
                            "Expected: '" + expectedStripeAPIVersion + "'. " +
                            "Either upgrade stripe-java library or update expected API version."
            );
        }
        
        return new StripeClient(stripeAPISecretKey);
    }
    
    
    /**
     * Stripe API secret key.
     */
    @Bean(name = "stripeAPISecretKey")
    public String getStripeAPISecretKey(
            @Value("${stripe-api.secret-key}") String stripeAPISecretKey)
    {
        return stripeAPISecretKey;
    }
    
    
    /**
     * Stripe API webhook secret.
     */
    @Bean(name = "stripeAPIWebhookSecret")
    public String getStripeAPIWebhookSecret(
            @Value("${stripe-api.webhook-secret}") String stripeAPIWebhookSecret)
    {
        return stripeAPIWebhookSecret;
    }



}
