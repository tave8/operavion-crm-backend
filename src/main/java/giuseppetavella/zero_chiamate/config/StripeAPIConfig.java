package giuseppetavella.zero_chiamate.config;

import com.stripe.StripeClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("application.properties")
public class StripeAPIConfig {
    

    /**
     * The Stripe API client.
     *
     */
    @Bean
    public StripeClient getStripeClient(
            @Qualifier("stripeAPISecretKey") String stripeAPISecretKey)
    {
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
